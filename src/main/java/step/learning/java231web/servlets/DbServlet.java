package step.learning.java231web.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import step.learning.java231web.data.dao.ProductDao;
import step.learning.java231web.data.dao.ProductSectionDao;
import step.learning.java231web.data.dao.ProductVersionDao;
import step.learning.java231web.data.dao.UserDao;
import step.learning.java231web.services.db.IDbService;

/**
 * Робота з БД
 * @author Lector
 */
@Singleton
public class DbServlet extends HttpServlet{
    private final UserDao userDao;
    private final IDbService dbService;
    private final ProductSectionDao productSectionDao;
    private final ProductDao productDao;
    private final ProductVersionDao productVersionDao; 

    @Inject
    public DbServlet(IDbService dbService,ProductVersionDao productVersionDao,ProductDao productDao, UserDao userDao, step.learning.java231web.data.dao.ProductSectionDao productSectionDao) {
        this.dbService = dbService;
        this.userDao = userDao;
        this.productSectionDao = productSectionDao;
        this.productDao = productDao;
        this.productVersionDao = productVersionDao;
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = null;
        try {
            connection = dbService.getConnection();
            req.setAttribute("connection", "Ok");  
        }
        catch( SQLException ex ) {
            req.setAttribute("connection", ex.getMessage() );  
        }
        
        if(connection != null) {
            String attr = "";
            try {
                userDao.createTable();
                attr += "Create Tables OK";
                userDao.seedData();
                attr += ", Seed Data OK";                
                productSectionDao.createTable();
                attr += "<br/> productSection: Create Tables OK ";
                productDao.createTable();
                attr += "<br/> product: Create Tables OK ";
                productVersionDao.createTable();
                attr += "<br/> productVersion: Create Tables OK ";
                
                productSectionDao.seedData();
                attr += ", Seed Data OK";          
            }
            catch( SQLException ex ) {
                attr += ex.getMessage();  
            }      
            req.setAttribute("sql", attr );  
            
            // запити з результатами
            String sql = "select current_timestamp, current_date " +
            "union all " +
            "select current_timestamp, current_date";
            try( Statement statement = connection.createStatement();
                 ResultSet rs = statement.executeQuery(sql);
            ) {                
                StringBuilder sb = new StringBuilder();
                while( rs.next() ) {
                    sb.append(
                        rs.getTimestamp(1)  // JDBC починає відлік з 1
                            .toInstant().toString()
                    )
                    .append(" ")
                    .append(
                            rs.getDate(2)
                            .toString())
                    .append("<br/>");
                }
                req.setAttribute("sql2", sb.toString() );  
            }
            catch( SQLException ex ) {
                req.setAttribute("sql2", ex.getMessage() );  
            }
            // req.getParameter - одержання даних, що передаються
            // з форми, у т.ч. в тілі запиту
            String name = req.getParameter("name");
            if(name != null) {
                // підготовлені запити дозволяють розділяти SQL код
                // та дані, що в ньому вживаються. Для цього в запит
                // вставляються плейсхолдери (?), команда створюється
                // connection.prepareStatement(sql) - на базі запиту
                sql = "SELECT CONCAT('Hello, ', ?)"; 
                try(PreparedStatement prep = connection.prepareStatement(sql)) {
                    // параметри задаються за номерами плейсхолдерів
                    // із зазначенням типу даних, що передаватимуться
                    prep.setString(1, name);
                    try ( // !! при виконанні SQL не передається
                            ResultSet rs = prep.executeQuery()) {
                        rs.next();
                        req.setAttribute("sql3", rs.getString(1));
                    }
                }
                catch( SQLException ex ) {
                    req.setAttribute("sql3", ex.getMessage() );  
                }
            }
        }
        
        req.setAttribute("servlet", "db");        
        req.getRequestDispatcher("index.jsp")
                .forward(req, resp);
    }
    
}
/*
Д.З. Скласти SQL запит на формування UUID, результати
вивести у складі сторінки ~/db, додати скріншоти.
*/