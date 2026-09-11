<%-- 
    Document   : index
    Created on : 10 вер. 2026 р., 09:39:02
    Author     : samoylenko_d
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String viewName = (String) request.getAttribute("servlet");
    if(viewName == null) {
        viewName = "home.jsp";
    }
    else {
        viewName = viewName.toLowerCase() + ".jsp";
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <header>The header</header>
        <main>
            <jsp:include page="<%= viewName %>" />
        </main>
        <footer>The footer</footer>
    </body>
</html>
