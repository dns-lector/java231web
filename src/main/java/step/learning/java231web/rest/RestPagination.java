package step.learning.java231web.rest;

/**
 * REST meta pagination data
 * @author Lector
 */
public class RestPagination {
    private long page = 1;
    private long pageSize = 10;
    private long totalPages;
    private long totalItems;

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
