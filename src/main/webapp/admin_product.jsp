<%@page import="step.learning.java231web.data.dto.ProductSection"%>
<%@page import="step.learning.java231web.data.models.AdminProductViewModel"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    AdminProductViewModel viewModel = (AdminProductViewModel) 
            request.getAttribute("viewModel");
%>

<% if(viewModel == null) { %>
    <h1 style="color:maroon">Model transfer error</h1>
<% } else { %>
<table>
    <tbody>
        <tr>
            <td>
                <h2>Product</h2>
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
            </td>
            <td></td>
            <td>
                <h2>Product Version</h2>
                <form method="post" enctype="multipart/form-data">
                    <label>Виберіть товар:
                        <select name="product-id">
                            <% for(ProductSection ps : viewModel.getProductSections()) { %>
                            <option value="<%= ps.getId() %>" ><%= ps.getTitle() %></option>
                            <% } %>
                        </select>
                    </label><br/>
                    <label>Доповнення до назви товару:
                        <input name="product-version-title"/>
                    </label><br/>
                    <label>Доповнення до опису товару:
                        <input name="product-version-description"/>
                    </label><br/>
                    <label>Доповнення до Slug товару:
                        <input name="product-version-slug"/>
                    </label><br/>
                    <label>Ціна версії:
                        <input name="product-version-price" type="number" step="0.01" min="0.01"/>
                    </label><br/>
                    <label>Кількість на складі:
                        <input name="product-version-stock" type="number" step="1"/>
                    </label><br/>
                    <label>Порядок у переліку:
                        <input name="product-version-order" type="number" step="1"/>
                    </label><br/>
                    <label>Зображення:
                        <input name="product-version-image" type="file"/>
                    </label><br/>
                    <button>Додати</button>
                </form>
            </td>
        </tr>
    </tbody>
</table>
          
OK: <%= viewModel.getOkMessage() %>
<br/>
Ex: <%= viewModel.getExceptionMessage() %>
<% } %>