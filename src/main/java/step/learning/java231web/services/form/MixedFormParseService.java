package step.learning.java231web.services.form;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileItem;
import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletDiskFileUpload;

/**
 * Form Parse Service Mixed: Apache + Servlet
 * Apache for multipart, Servler for urlencoded
 * @author Lector
 */
@Singleton
public class MixedFormParseService implements IFormParseService {
    private final JakartaServletDiskFileUpload upload;
    private final String tempDirectoryPath = "C:/tmp/";
    private final int  maxMemorySize = 10 * 1024 * 1024;  // 10 MB
    private final long maxFileSize   = 10 * 1024 * 1024;  // 10 MB
    private final long maxFileCount  = 10;
    private final long maxFormSize   = 20 * 1024 * 1024;  // 20 MB

    @Inject
    public MixedFormParseService() {
        DiskFileItemFactory factory = DiskFileItemFactory
                .builder()
                .setThreshold(maxMemorySize)
                .setPath(tempDirectoryPath)
                .get();
        upload = new JakartaServletDiskFileUpload(factory);
        upload.setMaxFileSize(maxFileSize);
        upload.setMaxFileCount(maxFileCount);
        upload.setMaxSize(maxFormSize);
    }   

    @Override
    public IFormParseResult parse(HttpServletRequest req) throws FormParseException {
        final Map<String, String>   fields = new HashMap<>();
        final Map<String, FileItem> files  = new HashMap<>();
        
        if(JakartaServletDiskFileUpload.isMultipartContent(req)) {
            try {
                // multipart - use Apache
                List<DiskFileItem> items = upload.parseRequest(req);
                for (FileItem item : items) {
                    if (item.isFormField()) {
                        fields.put(
                                item.getFieldName(), 
                                item.getString(StandardCharsets.UTF_8)
                        );
                    } 
                    else {
                        files.put(item.getFieldName(), item);
                    }
                }
            } 
            catch (IOException ex) {
                throw new FormParseException(ex.getMessage());
            }
        }
        else {
            // non-multipart - use Servlet API
            Enumeration<String> names = req.getParameterNames();
            while( names.hasMoreElements() ) {
                String name = names.nextElement();
                fields.put( name, req.getParameter(name) );
            }
        }
        
        return new IFormParseResult() {
            @Override
            public Map<String, String> getFields() {
                return fields;
            }

            @Override
            public Map<String, FileItem> getFiles() {
                return files;
            }
        };
    }
    
}
