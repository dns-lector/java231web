package step.learning.java231web.servlets;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import step.learning.java231web.data.dao.UserDao;
import step.learning.java231web.data.dto.UserAccess;
import step.learning.java231web.data.models.UserSignupFormModel;
import step.learning.java231web.rest.RestMeta;
import step.learning.java231web.rest.RestPagination;
import step.learning.java231web.rest.RestResponse;

/**
 *
 * @author Lector
 */
@Singleton
public class UserServlet extends HttpServlet {
    private final UserDao userDao;
    private final Gson gson;
    private final Logger logger;

    @Inject
    public UserServlet(UserDao userDao, Gson gson, Logger logger) {
        this.userDao = userDao;
        this.gson = gson;
        this.logger = logger;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /* Sign Up by UserSignupFormModel
        */
        UserSignupFormModel formModel = gson.fromJson(
                req.getReader(),
                UserSignupFormModel.class
        );
        RestResponse restResponse;
        try {
            userDao.signupUser(formModel);
            restResponse = RestResponse.Ok(null, null);
        } 
        catch (SQLException ex) {
            System.getLogger(UserServlet.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            restResponse = RestResponse.ErrorInternal(null, null);
        }
        resp.getWriter().print( gson.toJson(restResponse) );
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if( pathInfo == null || "/".equals(pathInfo) ) {
            this.authenticate(req, resp);
        }
        else if( "/all".equals(pathInfo) ) {
            this.getAllUsers(req, resp);
        }
        else {
            resp.getWriter().print(
                gson.toJson(
                    RestResponse.NotFound(
                        new RestMeta()
                            .setServiceName("API service 'Users'")
                            .setDataType("string"),
                        "Path not found: " + pathInfo
            )));
        }
    }
    
    private void getAllUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Пагінація:
        // дані про сторінку (номер сторінки) - якщо є у запиті, то 
        //  беремо ці дані та перевіряємо на валідність, якщо ні, то
        //  покладаємо 1
        // дані про місткість сторінки - аналогічно, лише за замовчанням 10
        // загальна кількість елементів - береться з БД окремим запитом
        //  з усіма фільтрами, що й в основному
        RestResponse restResponse;
        long cnt;
        try {
            cnt = userDao.getUsersCount();
        } 
        catch (SQLException ex) {
            logger.log(Level.SEVERE, (String) null, ex);
            restResponse = RestResponse.ErrorInternal(null, null);
            resp.getWriter().print( gson.toJson(restResponse) );
            return;
        }
        
        long pageSize;
        String pageSizeParam = req.getParameter("pageSize");
        if(pageSizeParam != null) {
            try { pageSize = Long.parseLong(pageSizeParam); }
            catch(NumberFormatException ignore) {
                restResponse = RestResponse.BadRequest(null, 
                        "Number parse error for parameter 'pageSize'");
                resp.getWriter().print( gson.toJson(restResponse) );
                return;
            }
            if(pageSize <= 0) {
                restResponse = RestResponse.BadRequest(null, 
                        "Parameter 'pageSize' must not be negative or zero");
                resp.getWriter().print( gson.toJson(restResponse) );
                return;
            }
        }
        else {
            pageSize = 10;
        }
        
        long totalPages = Math.ceilDiv(cnt, pageSize);
        
        long page;
        String pageParam = req.getParameter("page");
        if(pageParam != null) {
            try { page = Long.parseLong(pageParam); }
            catch(NumberFormatException ignore) {
                restResponse = RestResponse.BadRequest(null, 
                        "Number parse error for parameter 'page'");
                resp.getWriter().print( gson.toJson(restResponse) );
                return;
            }
            if(page <= 0 || page > totalPages) {
                restResponse = RestResponse.BadRequest(null, 
                        "Parameter 'page' must be in range 1.." + totalPages);
                resp.getWriter().print( gson.toJson(restResponse) );
                return;
            }
        }
        else {
            page = 1;
        }
        RestPagination pagination = new RestPagination()
                .setTotalItems(cnt)
                .setPage(page)
                .setPageSize(pageSize)
                .setTotalPages(totalPages);
        try {
            restResponse = RestResponse.Ok(
                    new RestMeta()
                        .setCacheTime(1000)
                        .setDataType("array")
                        .setPagination(pagination), 
                    userDao.getUsers(pagination)
            );
        }
        catch(SQLException ex) {
            restResponse = RestResponse.ErrorInternal(null, null);
        }
        resp.getWriter().print( gson.toJson(restResponse) );
    }
    
    private void authenticate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Basic Authentication 
        // 1. Authorization header
        String authHeader = req.getHeader("Authorization");
        if(authHeader == null || authHeader.isEmpty()) {
            resp.setStatus(401);
            resp.getWriter().print( "Missing 'Authorization' header" );
            return;
        }
        String scheme = "Basic ";
        if( ! authHeader.startsWith(scheme) ) {
            resp.setStatus(401);
            resp.getWriter().print( "Authorization scheme must be " + scheme );
            return;
        }
        String credentials = authHeader.substring( scheme.length() );
        String userPass = new String(
                Base64.getDecoder().decode(credentials)
        );
        String[] parts = userPass.split(":", 2);
        if(parts.length != 2) {
            resp.setStatus(401);
            resp.getWriter().print( "User-Pass malformed " );
            return;
        }
        UserAccess ua;
        try {
            ua = userDao.authenticate( parts[0], parts[1] );
        }
        catch( SQLException ex ) {
            resp.setStatus(500);
            resp.getWriter().print( "Internal Error" );
            return;
        }
        if(ua == null) {
            resp.setStatus(401);
            resp.getWriter().print( "Credentials rejected" );
            return;
        }
        resp.getWriter().print( ua.getId().toString() );
    }
}
/*
Д.З. Перевести роботу методу UserServlet::authenticate
до стандартів RestResponse
*/
/*
http://localhost:8080/java231web/user/all?x=10&y=20  /user/  /user  /user/123/456
req.getMethod()      "GET"
req.getScheme()      "http"
req.getServerName()  "localhost"
req.getServerPort()  8080
req.getContextPath() "/java231web"
req.getServletPath() "/user"
req.getPathInfo()    "/all"                           "/"    null   "/123/456"
req.getQueryString() "x=10&y=20"

req.getRequestURI()  "/java231web/user/all"
*/