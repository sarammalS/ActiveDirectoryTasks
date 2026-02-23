<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Map" %>
<html>
<head>
    <title>User Details</title>
    <style>
        .user-details { margin: 40px auto; width: 400px; border: 1px solid #ccc; padding: 20px; border-radius: 8px; }
        .user-details h2 { margin-top: 0; }
        .user-details label { display: block; margin-top: 10px; font-weight: bold; }
        .user-details span { display: block; margin-bottom: 10px; }
    </style>
</head>
<body>
<%
    Map<String, String> user = (Map<String, String>)request.getAttribute("user");
%>
<div class="user-details">
    <h2>User Details</h2>
    <label>Username:</label>
    <span><%= user != null ? user.get("username") : "" %></span>
    <label>First Name:</label>
    <span><%= user != null ? user.get("firstName") : "" %></span>
    <label>Last Name:</label>
    <span><%= user != null ? user.get("lastName") : "" %></span>
    <label>Email Address:</label>
    <span><%= user != null ? user.get("email") : "" %></span>
    <a href="userList">Back to User List</a>
</div>
</body>
</html>
