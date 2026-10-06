<%@page import="step.learning.java231web.data.dto.ProductSection"%>
<%@page import="step.learning.java231web.data.models.AdminProductViewModel"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    AdminProductViewModel viewModel = (AdminProductViewModel) 
            request.getAttribute("viewModel");
%>

<h2>Product</h2>

<% if(viewModel == null) { %>
    <h1 style="color:maroon">Model transfer error</h1>
<% } else { %>
<form method="post" enctype="multipart/form-data">
    <label>Виберіть товарну групу:
        <select name="product-section-id">
            <% for(ProductSection ps : viewModel.getProductSections()) { %>
            <option value="<%= ps.getId() %>" ><%= ps.getTitle() %></option>
            <% } %>
        </select>
    </label><br/>
    <label>Назва товару:
        <input name="product-title"/>
    </label><br/>
    <label>Опис товару:
        <input name="product-description"/>
    </label><br/>
    <label>Slug товару:
        <input name="product-slug"/>
    </label><br/>
    <label>Ціна товару:
        <input name="product-price" type="number" step="0.01" min="0.01"/>
    </label><br/>
    <label>Кількість на складі:
        <input name="product-stock" type="number" step="1"/>
    </label><br/>
    <label>Порядок у переліку:
        <input name="product-order" type="number" step="1"/>
    </label><br/>
    <label>Головне зображення:
        <input name="product-image" type="file"/>
    </label><br/>
    <button>Додати</button>
</form>
            
OK: <%= viewModel.getOkMessage() %>
<br/>
Ex: <%= viewModel.getExceptionMessage() %>
<% } %>
<img src="http://localhost:8080/java231web/storage/28ea2bd2-9d13-42fc-b9bd-2797c07c2b74.webp" />