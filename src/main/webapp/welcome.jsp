<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String currentUser = (String) session.getAttribute("user");
    if (currentUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome</title>
    <style>
        body { font-family: sans-serif; text-align: center; margin-top: 50px; }
        button { 
            padding: 10px 20px; 
            margin: 10px; 
            font-size: 16px; 
            cursor: pointer; 
            background-color: #007bff; 
            color: white; 
            border: none; 
            border-radius: 4px; 
        }
        button:hover { background-color: #0056b3; }
    </style>
</head>
<body>
    <h2>Welcome, <c:out value="${user}"/>!</h2>

    <form action="CreateUser.jsp" method="get" style="display:inline;">
        <button type="submit">Create User</button>
    </form>

    <form action="EditUserServlet" method="get" style="display:inline;">
        <input type="hidden" name="username" value="<%= java.net.URLEncoder.encode(currentUser, "UTF-8") %>" />
        <button type="submit">Edit User</button>
    </form>

    <form action="ListUsersServlet" method="get" style="display:inline;">
        <button type="submit">View Users</button>
    </form>

    <form action="logout.jsp" method="get" style="display:inline;">
        <button type="submit">Logout</button>
    </form>
</body>
</html>