<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<html>
<head>
    <title>Active Directory Users</title>
    <style>
        .table-container { position: relative; }
        .create-btn { position: absolute; top: 0; right: 0; margin: 10px; }
        table { width: 100%; border-collapse: collapse; margin-top: 40px; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background: #f2f2f2; }
    </style>
</head>
<body>
<div class="table-container">
    <button class="create-btn" onclick="location.href='createUser.jsp'">Create User</button>
    <table>
        <thead>
            <tr>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Email Address</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <% List<Map<String, String>> users = (List<Map<String, String>>)request.getAttribute("users");
               if (users != null) {
                   for (Map<String, String> user : users) { %>
            <tr>
                <td><%= user.get("firstName") %></td>
                <td><%= user.get("lastName") %></td>
                <td><%= user.get("email") %></td>
                <td><a href="viewUser?username=<%= user.get("username") %>">View</a></td>
            </tr>
            <%   }
               }
            %>
        </tbody>
    </table>
</div>
</body>
</html>