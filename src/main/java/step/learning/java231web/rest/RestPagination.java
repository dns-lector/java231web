package step.learning.java231web.rest;

import jakarta.servlet.http.HttpServletRequest;

/**
 * REST meta pagination data
 * @author Lector
 */
public class RestPagination {
    private long page = 1;
    private long pageSize = 10;
    private long totalPages;
    private long totalItems;
    
    public static RestPagination fromRequest(HttpServletRequest req, 
            long totalItems) throws Exception {
        long pageSize;
        String pageSizeParam = req.getParameter("pageSize");
        if(pageSizeParam != null) {
            try { pageSize = Long.parseLong(pageSizeParam); }
            catch(NumberFormatException ignore) {
                throw new Exception(
                        "Number parse error for parameter 'pageSize'");
            }
            if(pageSize <= 0) {
                 throw new Exception(
                        "Parameter 'pageSize' must not be negative or zero");
            }
        }
        else {
            pageSize = 10;
        }
        
        long totalPages = Math.ceilDiv(totalItems, pageSize);
        
        long page;
        String pageParam = req.getParameter("page");
        if(pageParam != null) {
            try { page = Long.parseLong(pageParam); }
            catch(NumberFormatException ignore) {
                throw new Exception(
                        "Number parse error for parameter 'page'");
            }
            if(page <= 0 || page > totalPages) {
                throw new Exception(
                        "Parameter 'page' must be in range 1.." + totalPages);
            }
        }
        else {
            page = 1;
        }
        return new RestPagination()
                .setTotalItems(totalItems)
                .setPage(page)
                .setPageSize(pageSize)
                .setTotalPages(totalPages);
    }

    public long getPage() {
        return page;
    }

    public RestPagination setPage(long page) {
        this.page = page;
        return this;
    }

    public long getPageSize() {
        return pageSize;
    }

    public RestPagination setPageSize(long pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public RestPagination setTotalPages(long totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public RestPagination setTotalItems(long totalItems) {
        this.totalItems = totalItems;
        return this;
    }
    
    
}
