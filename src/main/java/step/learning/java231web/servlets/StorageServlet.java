package step.learning.java231web.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import step.learning.java231web.services.storage.IStorageService;

/**
 *
 * @author Lector
 */
@Singleton
public class StorageServlet extends HttpServlet {
    public static final String SERVLET_PATH = "storage";
    public static String getPathPrefix(HttpServletRequest req) {        
        // http://localhost:8080/java231web/storage/  {filename}
        return String.format(
                "%s://%s:%d%s/%s/",
                req.getScheme(),
                req.getServerName(),
                req.getServerPort(),
                req.getContextPath(),
                SERVLET_PATH
        );
    }
    
    private final IStorageService storageService;

    @Inject
    public StorageServlet(IStorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        // MIME type of file
        int dotPosition = pathInfo.lastIndexOf('.');
        if(dotPosition < 0) {
            resp.setStatus(400);
            return;
        }
        String contentType;
        switch(pathInfo.substring(dotPosition)) {
            case ".jpg" :
            case ".jpeg": contentType = "image/jpeg"; break;
            case ".png" : contentType = "image/png" ; break;
            case ".webp": contentType = "image/webp"; break;
            default: contentType = "application/octet-stream"; 
        }
        try(InputStream inputStream = storageService.getStream(pathInfo)) {
            resp.setHeader("Content-Type", contentType);
            // piping
            inputStream.transferTo( resp.getOutputStream() );
        }
        catch(IOException ignore) {
            resp.setStatus(404);
        }
    }
    
}
/*
Сервіс завантаження (збереження) файлів.
Проблема: робота опублікованого застосунку (Web-App) здійснюється
у контексті сервера, який (контекст) руйнується після того, як
публікується оновлена версія застосунку.
Сервіс має надати взаємодію з оточенням поза контекстом сервера:
- у файловій системі ПК
- у мережі (локальній або глобальній)
- за допомогою сторонніх сервісів

Передача файлів може здійснюватись або за ftp, або за http - 
протоколами. Для фронтенд-форм такий поділ є поширеним - частина
даних передається по ftp, а частина по http. Для форм JSP
традиційно всі дані передаються одним пакетом - multipart формою
POST ...
headers
роздільник: asjhg1412aoi
--asjhg1412aoi
part-headers...

body
--asjhg1412aoi
Content-Type: form-data
Field-Name: user-name

Petrovich
--asjhg1412aoi
Content-Type: image/jpeg
Field-Name: user-avatar

a;hg9q34hvae98hb
--asjhg1412aoi--

Розбір таких пакетів, особливо, універсальний, що підтримує
інші типи форм, часто перекладається на сторонні модулі.
http://commons.apache.org/proper/commons-fileupload/using.html
*/
