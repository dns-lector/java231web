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
import java.util.HashMap;
import java.util.Map;
import step.learning.java231web.data.dao.ProductSectionDao;
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
    private RestMeta meta = new RestMeta()
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
            pagination = RestPagination.fromRequest(
                    req, 
                    productSectionDao.getSectionsCount()
            );
            restResponse = RestResponse.Ok(
                    meta
                        .setCacheTime(1000000)
                        .setDataType("array")
                        .setPagination(pagination), 
                    productSectionDao.getSections(pagination)
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
        resp.getWriter().print( gson.toJson(restResponse) );
    }
    
    private void getSectionBySlug(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String slug = req.getPathInfo().substring(1);  // прибираємо перший '/'
        RestResponse restResponse;
        RestPagination pagination;
        try {
            pagination = RestPagination.fromRequest(
                    req, 
                    productSectionDao.getSectionsCount()
            );
            restResponse = RestResponse.Ok(
                    meta
                        .setCacheTime(1000000)
                        .setDataType("object")
                        .setPagination(pagination), 
                    slug
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
        resp.getWriter().print( gson.toJson(restResponse) );
    }
}
