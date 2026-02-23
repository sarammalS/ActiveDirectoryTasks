<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="util.LdapUtil.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String currentUser = (String) session.getAttribute("user");
    if (currentUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    List<User> users = (List<User>) request.getAttribute("users");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>All AD Users</title>
    <style>
        body { font-family: sans-serif; text-align: center; margin-top: 50px; }
        table { border-collapse: collapse; margin: 20px auto; width: 70%; }
        th, td { padding: 10px; border: 1px solid #ccc; }
        th { background-color: #f2f2f2; }
        a { text-decoration: none; color: #007bff; }
        a:hover { text-decoration: underline; }
        button { padding: 10px 20px; margin: 10px; font-size: 16px; cursor: pointer; background-color: #007bff; color: white; border: none; border-radius: 4px; }
        button:hover { background-color: #0056b3; }
        .btn-container { margin-top: 20px; }
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
    </tr>
    <% if (users != null && !users.isEmpty()) {
           for (User u : users) { %>
        <tr>
            <td><a href="EditUserServlet?username=<%= java.net.URLEncoder.encode(u.getUsername(), "UTF-8") %>"><c:out value="<%= u.getUsername() %>"/></a></td>
            <td><c:out value="<%= u.getFirstName() %>" /> <c:out value="<%= u.getLastName() %>"/></td>
            <td><c:out value="<%= u.getEmail() %>"/></td>
        </tr>
    <%   } 
       } else { %>
        <tr><td colspan="3">No users found.</td></tr>
    <% } %>
</table>

</body>
</html>