<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Map" %>
<html>
<head>
    <title>Edit User</title>
    <style>
        .form-container { width: 400px; margin: 40px auto; padding: 20px; border: 1px solid #ccc; border-radius: 8px; background: #fff; }
        label { display: block; margin-top: 10px; font-weight: bold; }
        input { width: 100%; padding: 8px; margin-top: 5px; }
        button { margin-top: 20px; padding: 10px 20px; }
    </style>
</head>
<body>
<%
    Map<String, String> user = (Map<String, String>)request.getAttribute("user");
%>
<div class="form-container">
    <h2>Edit User</h2>
    <form action="editUser" method="post">
        <input type="hidden" name="username" value="<%= user != null ? user.get("username") : "" %>" />
        <label>First Name:</label>
        <input type="text" name="firstName" value="<%= user != null ? user.get("firstName") : "" %>" required />
        <label>Last Name:</label>
        <input type="text" name="lastName" value="<%= user != null ? user.get("lastName") : "" %>" required />
        <label>Email Address:</label>
        <input type="email" name="email" value="<%= user != null ? user.get("email") : "" %>" required />
        <button type="submit">Update</button>
    </form>
</div>
</body>
</html>
