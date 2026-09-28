package step.learning.java231web.rest;

/**
 *
 * @author Lector
 */
public class RestResponse {
    private RestStatus status;
    private RestMeta meta;
    private Object data;

    public RestResponse() {
    }

    public RestResponse(RestStatus status, RestMeta meta, Object data) {
        this.status = status;
        this.meta = meta;
        this.data = data;
    }
    
    public static RestResponse Ok(RestMeta meta, Object data) {
        return new RestResponse(RestStatus.Ok, meta, data);
    }
    public static RestResponse BadRequest(RestMeta meta, Object data) {
        return new RestResponse(RestStatus.BadRequest, meta, data);
    }
    public static RestResponse NotFound(RestMeta meta, Object data) {
        return new RestResponse(RestStatus.NotFound, meta, data);
    }
    public static RestResponse ErrorInternal(RestMeta meta, Object data) {
        return new RestResponse(RestStatus.ErrorInternal, meta, data);
    }

    public RestStatus getStatus() {
        return status;
    }

    public RestResponse setStatus(RestStatus status) {
        this.status = status;
        return this;
    }

    public RestMeta getMeta() {
        return meta;
    }

    public RestResponse setMeta(RestMeta meta) {
        this.meta = meta;
        return this;
    }

    public Object getData() {
        return data;
    }

    public RestResponse setData(Object data) {
        this.data = data;
        return this;
    }
    
    
}
/*
REST
Віддзеркалюємо статус з метаданими до самих даних, що
передаються у відповіді АРІ. Нормальною стає ситуація,
коли з позитивним НТТР-статусом приходять дані, що 
містять помилковий статус
HTTP/1.1 200 OK
Time: 1234567890111
...
{
    status: { isOk: false, code: 404, message: "Not Found"},
    meta: {
        service: "Products API",
        serverTime: 1234567890000,
        cacheTime: 86400000,
        dataType: "string",
        links: {
            self: "/product/123",
            parent: "/product",
            details: "/product/123/info",
            images: "/product/123/images",
        },
        manipulations: ["GET", "PATCH"],

pagination: {
    page: 1,
    pageSize: 10,
    totalPages: 3,
    totalItems: 25
},
parameters: {
    id: 123
}
    },
    data: "Requested Product not found for id='123'" 
}
*/