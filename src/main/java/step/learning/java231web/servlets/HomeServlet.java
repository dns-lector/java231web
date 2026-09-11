package step.learning.java231web.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author samoylenko_d
 */
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // закладаємо до req додатковий атрибут, який дасть можливість
        // контролю факту проходження запиту через сервлет
        req.setAttribute("servlet", "Home");
        
        // передаємо управління на представлення (~return View("index.jsp"))
        req.getRequestDispatcher("index.jsp")
                .forward(req, resp);
    }
    
}
