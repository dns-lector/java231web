package step.learning.java231web.services.form;

import java.util.Map;
import org.apache.commons.fileupload2.core.FileItem;

/**
 * Form Parse Result interface for segregation
 * fields and files
 * @author Lector
 */
public interface IFormParseResult {
    Map<String, String> getFields();
    Map<String, FileItem> getFiles();
}
