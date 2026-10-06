package step.learning.java231web.services.form;

import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * @author Lector
 */
public interface IFormParseService {
    IFormParseResult parse(HttpServletRequest req) throws FormParseException;
}
