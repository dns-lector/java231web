<%
    String contextPath = request.getContextPath();
    String pathInfo = (String) request.getAttribute("pathInfo");
    String subView = "admin_" + pathInfo + ".jsp";
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<h1>Admin Page <%= pathInfo %></h1>
<nav>
    <a href="<%=contextPath%>/admin/">Home</a>
    <a href="<%=contextPath%>/admin/product">Product</a>
    <a href="<%=contextPath%>/admin/section">Section</a>
</nav>
<jsp:include page="<%= subView %>" />