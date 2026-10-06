package step.learning.java231web.data.models;

import java.util.List;
import step.learning.java231web.data.dto.ProductSection;

/**
 * /admin/product View Model
 * @author Lector
 */
public class AdminProductViewModel {
    private List<ProductSection> productSections;
    private String exceptionMessage;
    private String okMessage;

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public AdminProductViewModel setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
        return this;
    }

    public String getOkMessage() {
        return okMessage;
    }

    public AdminProductViewModel setOkMessage(String okMessage) {
        this.okMessage = okMessage;
        return this;
    }
    

    public List<ProductSection> getProductSections() {
        return productSections;
    }

    public AdminProductViewModel setProductSections(List<ProductSection> productSections) {
        this.productSections = productSections;
        return this;
    }
    
}
