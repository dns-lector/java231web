<%@page import="step.learning.java231web.data.dto.ProductSection"%>
<%@page import="step.learning.java231web.data.models.AdminProductViewModel"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    AdminProductViewModel viewModel = (AdminProductViewModel) 
            request.getAttribute("viewModel");
%>

<h2>Section</h2>

<% if(viewModel == null) { %>
    <h1 style="color:maroon">Model transfer error</h1>
<% } else { %>
<form method="post" enctype="multipart/form-data">
    <label>Виберіть батьківську товарну групу:
        <select name="section-parent-id">
            <option value="" >TOP LEVEL</option>
            <% for(ProductSection ps : viewModel.getProductSections()) { %>
            <option value="<%= ps.getId() %>" ><%= ps.getTitle() %></option>
            <% } %>
        </select>
    </label><br/>
    <label>Назва розділу:
        <input name="section-title"/>
    </label><br/>
    <label>Опис розділу:
        <input name="section-description"/>
    </label><br/>
    <label>Slug розділу:
        <input name="section-slug"/>
    </label><br/>
    <label>Порядок у переліку:
        <input name="section-order" type="number" step="1"/>
    </label><br/>
    <label>Головне зображення:
        <input name="section-image" type="file"/>
    </label><br/>
    <button>Додати</button>
</form>
            
OK: <%= viewModel.getOkMessage() %>
<br/>
Ex: <%= viewModel.getExceptionMessage() %>
<% } %>