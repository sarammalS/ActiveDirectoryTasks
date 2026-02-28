<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List, Model.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    List<User> users = (List<User>) request.getAttribute("users");
    String loggedInUser = (String) session.getAttribute("user");
%>
<!DOCTYPE html>
<html>
<head>
    <title>All AD Users</title>
    <style>
        body  { font-family: Arial, sans-serif; text-align: center; margin-top: 40px; }
        table { border-collapse: collapse; margin: 20px auto; width: 70%; }
        th, td { padding: 10px; border: 1px solid #ccc; text-align: left; }
        th    { background: #f2f2f2; }
        a     { text-decoration: none; color: #007bff; }
        a:hover { text-decoration: underline; }
        .btn  { padding: 9px 18px; background: #007bff; color: white;
                border: none; border-radius: 4px; cursor: pointer; font-size: 15px; }
        .btn:hover { background: #0056b3; }
    </style>
</head>
<body>

<h2>Welcome, <c:out value="${user}"/>!</h2>
<h3>All AD Users</h3>

<table>
    <tr>
        <th>Username</th>
        <th>Full Name</th>
        <th>Email</th>
        <th>Action</th>
    </tr>
    <% if (users != null && !users.isEmpty()) {
           for (User u : users) { %>
    <tr>
        <td><%= u.getUsername() %></td>
        <td><%= u.getFirstName() %> <%= u.getLastName() %></td>
        <td><%= u.getEmail() %></td>
        <td>
            <% if (u.getUsername().equalsIgnoreCase(loggedInUser)) { %>
                <a href="EditUserServlet">Edit</a>
            <% } else { %>
                —
            <% } %>
        </td>
    </tr>
    <%  }
       } else { %>
    <tr><td colspan="4">No users found.</td></tr>
    <% } %>
</table>

<div style="margin-top: 20px;">
    <a href="welcome.jsp"><button class="btn">Back to Welcome</button></a>
    <a href="OUListServlet"><button class="btn">Create User</button></a>
    <a href="logout.jsp"><button class="btn">Logout</button></a>
</div>

</body>
</html>