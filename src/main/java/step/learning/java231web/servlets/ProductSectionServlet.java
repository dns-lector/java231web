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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import step.learning.java231web.data.dao.ProductSectionDao;
import step.learning.java231web.data.dto.Product;
import step.learning.java231web.data.dto.ProductSection;
import step.learning.java231web.rest.RestMeta;
import step.learning.java231web.rest.RestPagination;
import step.learning.java231web.rest.RestResponse;

/**
 * REST API for ProductSection DTO
 * @author Lector
 */

@Singleton
public class ProductSectionServlet extends HttpServlet {
    private final ProductSectionDao productSectionDao;
    private final Gson gson;
    private final RestMeta meta = new RestMeta()
            .setServiceName( "API 'Product Sections'" )
            .setManipulations( new String[]{ "GET" } )
            .setLinks( new HashMap<>( Map.ofEntries(
                    Map.entry("list", "/"),
                    Map.entry("details", "/{slug}")
            )));


    @Inject
    public ProductSectionServlet(ProductSectionDao productSectionDao, com.google.gson.Gson gson) {
        this.productSectionDao = productSectionDao;
        this.gson = gson;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        meta.setServerTime( System.currentTimeMillis() );
        super.service(req, resp);
    }    

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if(pathInfo == null || "/".equals(pathInfo)) {
            getAllSections(req, resp);
        }
        else {
            getSectionBySlug(req, resp);
        }
    }
    
    private void getAllSections(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RestResponse restResponse;
        RestPagination pagination;
        try {
            String prefix = StorageServlet.getPathPrefix(req);
            List<ProductSection> sections = new ArrayList<>(
                    productSectionDao
                    .getSections()
                    .stream()
                    .map((section) -> section.setImageUrl( prefix + section.getImageUrl() ))
                    .toList()
            );      
            // Задача: перетворити "плоский" перелік груп, частина яких посилається на
            // батьківську групу, до ієрархічної структури, в якій дочірні групи
            // знаходяться безпосередньо у батьківській.
            // Алгоритм: проходимо по переліку, якщо знаходимо групу з ненульовою
            // батьківською групою, то знаходимо відповідну батьківську групу
            // та переносимо дану групу до її дочірнього переліку (а з основного
            // переліку вилучаємо)
            List<ProductSection> removeSections = new ArrayList<>();
            for(ProductSection section : sections) {
                UUID parentId = section.getParentId();
                if(parentId != null) {
                    // ProductSection parent = null;
                    for(ProductSection s : sections) {
                        if(parentId.equals(s.getId())) {
                            // s - parent of section
                            s.getChildren().add(section);
                            removeSections.add(section);
                        }
                    }
                }
            }
            for(ProductSection section : removeSections) {
                sections.remove(section);
            }
            // Робота з пагінацією змінюється - обмеження вибірки БД
            // неможливо задати через випадкове розміщення дочірних і             
            // батьківських груп у БД - Реалізуємо повну вибірку та обмежуємо
            // її засобами мови програмування.            
            pagination = RestPagination.fromRequest(
                    req, 
                    sections.size()
            );
            restResponse = RestResponse.Ok(
                    meta
                        .setCacheTime(1000000)
                        .setDataType("array")
                        .setPagination(pagination), 
                    sections.stream()
                        .skip(pagination.getPageSize() * (pagination.getPage() - 1))
                        .limit(pagination.getPageSize())
                        .toArray()
            ); 
        } 
        catch (SQLException ex) {
            restResponse = RestResponse.ErrorInternal( 
                    meta.setDataType("null"), null );
        }
        catch(Exception ex) {
            restResponse = RestResponse.BadRequest( 
                    meta.setDataType("string"), ex.getMessage() );
        }
        resp.setContentType("application/json");
        resp.getWriter().print( gson.toJson(restResponse) );
    }
    
    private void getSectionBySlug(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String slug = req.getPathInfo().substring(1);  // прибираємо перший '/'
        RestResponse restResponse;
        RestPagination pagination;
        try {
            pagination = RestPagination.fromRequest(
                    req, 
                    productSectionDao.getProductsCountBySectionSlug(slug)
            );
            ProductSection productSection = 
                    productSectionDao.getSectionBySlug(slug, pagination);
            if( productSection == null ) {                 
                restResponse = RestResponse.NotFound(
                        meta.setDataType("null"), null);
            }
            else {             
                // коригуємо посилання на зображення, оскільки у БД 
                // знаходяться лише імена файлів
                String prefix = StorageServlet.getPathPrefix(req);
                for(Product p : productSection.getProducts()) {
                    p.setImageUrl( prefix + p.getImageUrl() );
                }
                for(ProductSection s : productSection.getChildren()) {
                    s.setImageUrl( prefix + s.getImageUrl() );
                }
                restResponse = RestResponse.Ok(
                        meta
                            .setCacheTime(1000000)
                            .setDataType("object")
                            .setPagination(pagination), 
                        productSection
                );
            }
        } 
        catch (SQLException ex) {
            restResponse = RestResponse.ErrorInternal( 
                    meta.setDataType("null"), null );
        }
        catch(Exception ex) {
            restResponse = RestResponse.BadRequest( 
                    meta.setDataType("string"), ex.getMessage() );
        }
        resp.getWriter().print( gson.toJson(restResponse) );
    }
}
/*
Д.З. Реалізувати стилізацію карток підгруп товарної групи
Рекомендується відокремити картки підгруп від карток 
основних груп.
*/