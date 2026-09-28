package step.learning.java231web.rest;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author Lector
 */
public class RestMeta {
    private String serviceName = null;
    private String dataType = null;
    private long serverTime = new Date().getTime(); 
    private long cacheTime = 0;
    private String[] manipulations = null;
    private Map<String, String> links = null;
    private Map<String, Object> parameters = null;
    private RestPagination pagination = null;

    public String getServiceName() {
        return serviceName;
    }

    public RestMeta setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public String getDataType() {
        return dataType;
    }

    public RestMeta setDataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    public long getServerTime() {
        return serverTime;
    }

    public RestMeta setServerTime(long serverTime) {
        this.serverTime = serverTime;
        return this;
    }

    public long getCacheTime() {
        return cacheTime;
    }

    public RestMeta setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
        return this;
    }

    public String[] getManipulations() {
        return manipulations;
    }

    public RestMeta setManipulations(String[] manipulations) {
        this.manipulations = manipulations;
        return this;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public RestMeta setLinks(Map<String, String> links) {
        this.links = links;
        return this;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public RestMeta setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
        return this;
    }

    public RestPagination getPagination() {
        return pagination;
    }

    public RestMeta setPagination(RestPagination pagination) {
        this.pagination = pagination;
        return this;
    }
    
    
}
