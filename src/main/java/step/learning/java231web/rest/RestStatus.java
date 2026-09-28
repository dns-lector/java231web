package step.learning.java231web.rest;

/**
 *
 * @author Lector
 */
public class RestStatus {
    private boolean isOk;
    private int code;
    private String message;

    public RestStatus() {
    }

    public RestStatus(boolean isOk, int code, String message) {
        this.isOk = isOk;
        this.code = code;
        this.message = message;
    }
    
    public static final RestStatus Ok = new RestStatus(true, 200, "OK");
    public static final RestStatus BadRequest = new RestStatus(false, 400, "Bad Request");
    public static final RestStatus NotFound = new RestStatus(false, 404, "Not Found");
    public static final RestStatus ErrorInternal = new RestStatus(false, 500, "Internal Server Error");
    // ----- NON STANDARD ---------
    public static final RestStatus HeaderRequired = new RestStatus(false, 440, "Header Required");
    public static final RestStatus HeaderMalformed = new RestStatus(false, 441, "Header Malformed");
    public static final RestStatus QueryParameterRequired = new RestStatus(false, 442, "Query Parameter Required");

    public boolean isOk() {
        return isOk;
    }

    public RestStatus setOk(boolean isOk) {
        this.isOk = isOk;
        return this;
    }

    public int getCode() {
        return code;
    }

    public RestStatus setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public RestStatus setMessage(String message) {
        this.message = message;
        return this;
    }
    
}
/*
Д.З. Реалізувати набір статичних полів класу RestStatus
для стандартних статусів, запропонувати поля для нестандартних
варіантів.
*/