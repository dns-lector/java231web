package step.learning.java231web.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import step.learning.java231web.data.dao.ProductDao;
import step.learning.java231web.data.dao.ProductSectionDao;
import step.learning.java231web.data.dao.ProductVersionDao;
import step.learning.java231web.data.dto.Product;
import step.learning.java231web.data.dto.ProductVersion;
import step.learning.java231web.data.models.AdminProductViewModel;
import step.learning.java231web.services.form.FormParseException;
import step.learning.java231web.services.form.IFormParseResult;
import step.learning.java231web.services.form.IFormParseService;
import step.learning.java231web.services.storage.IStorageService;

/**
 *
 * @author Lector
 */
@Singleton
public class AdminServlet extends HttpServlet {
    private final ProductSectionDao productSectionDao;
    private final ProductDao productDao;
    private final ProductVersionDao productVersionDao; 
    private final IStorageService storageService;
    private final IFormParseService formParseService;
    
    
    @Inject
    public AdminServlet(ProductVersionDao productVersionDao, ProductDao productDao, ProductSectionDao productSectionDao, step.learning.java231web.services.storage.IStorageService storageService, step.learning.java231web.services.form.IFormParseService formParseService) {
        this.productSectionDao = productSectionDao;
        this.productDao = productDao;
        this.productVersionDao = productVersionDao;
        this.storageService = storageService;
        this.formParseService = formParseService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("servlet", "admin");   
        String pathInfo = req.getPathInfo();
        if(pathInfo == null || "/".equals(pathInfo)) {
            homeView(req, resp);
        }
        else if("/product".equals(pathInfo)) {
            productView(req, resp);
        }
        /*
        Д.З. Реалізувати сторінку 404 для внутрішніх маршрутів
        адміністративної сторінки.
        */
    }
    
    private void homeView(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pathInfo", "home"); 
        String pathInfo = req.getPathInfo();
        (pathInfo == null 
                ? req.getRequestDispatcher("/index.jsp")
                : req.getRequestDispatcher("../index.jsp")
        ).forward(req, resp);
    }
    
    private void productView(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pathInfo", "product"); 
        try {
            req.setAttribute("viewModel", 
                    new AdminProductViewModel()
                    .setProductSections(productSectionDao.getSections())
            );
        }
        catch(SQLException ignore) { }
        
        req.getRequestDispatcher("../index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("servlet", "admin");   
        String pathInfo = req.getPathInfo();
        if(pathInfo == null || "/".equals(pathInfo)) {
            homeView(req, resp);
        }
        else if("/product".equals(pathInfo)) {            
            // обробляємо дані форми, що передається
            processProductForm(req, resp);
        }
    }
    
    private void processProductForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       IFormParseResult form;
       AdminProductViewModel viewModel = new AdminProductViewModel();
       try {
           viewModel.setProductSections(productSectionDao.getSections());
           form = formParseService.parse(req);
           /*
           Д.З. Реалізувати валідацію даних
           форми додавання нового товару
           з відображенням помилок на сторінці форми.
           */
            String filename = storageService.save(
                    form.getFiles().get("product-image")
            );
            Map<String,String> fields = form.getFields();
            Product product = new Product()
                   .setTitle(fields.get("product-title"))
                   .setDescription(fields.get("product-description"))
                   .setSectionId(UUID.fromString(fields.get("product-section-id")))
                   .setSlug(fields.get("product-slug"))
                   .setImageUrl(filename)
                   .setOrderInPrice(Long.parseLong(fields.get("product-order")));
            ProductVersion productVersion = new ProductVersion()
                    .setOrderInPrice(1)
                    .setPrice(Double.parseDouble(fields.get("product-price")))
                    .setStock(Long.parseLong(fields.get("product-stock")));
            UUID productId = productDao.addProduct(product);
            productVersion.setProductId(productId);
            UUID productVersionId = productVersionDao.addProductVersion(productVersion);
            viewModel.setOkMessage(
                    "New IDs: " + productId + ", " + productVersionId);
       }
       catch(IOException | SQLException | FormParseException ex) {
           viewModel.setExceptionMessage(ex.getMessage());
       }
       req.setAttribute("viewModel", viewModel);
       req.setAttribute("pathInfo", "product");
       req.getRequestDispatcher("../index.jsp").forward(req, resp);
    }
}
