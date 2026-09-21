<%@page contentType="text/html" pageEncoding="UTF-8"%>
<h1>Робота з базами даних</h1>
<p>
    JDBC - набір інструментів для взаємодії з СУБД (аналог ADO.NET).
    Послідовність роботи:
</p>
<ul>
    <li>Підключення до СУБД/БД. Основа - java.sql.Connection: 
        <%= request.getAttribute("connection") %></li>
    <li>Виконання запитів. Розрізняються запити з результатами
    та без них, а також параметричні (підготовлені) запити.</li>
</ul>
<p>
    DLL: <%= request.getAttribute("sql") %>
</p>

<p>
    DML <%= request.getAttribute("sql2") %>
</p>
<form>
    Параметричний запит: <br/>
    <input name="name"/>
    <button>Hello</button><br/>
    <%= request.getAttribute("sql3") %>
</form>