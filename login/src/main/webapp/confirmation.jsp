<%@ page contentType="text/html;charset=UTF-8" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String message = request.getAttribute("message") != null
                   ? (String) request.getAttribute("message")
                   : "User created successfully!";
%>
<!DOCTYPE html>
<html>
<head>
    <title>Confirmation</title>
    <style>
        body { font-family: Arial; background: #f5f5f5; text-align: center; padding-top: 60px; }
        .box { background: #fff; padding: 35px 50px; border-radius: 8px;
               box-shadow: 0 0 10px #ccc; display: inline-block; }
        .success { color: #4CAF50; font-size: 1.3em; margin-bottom: 20px; }
        a { display: block; margin-top: 15px; color: #0078d4; text-decoration: none; font-size: 1em; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <div class="box">
        <div class="success"> <%= message %></div>
        <a href="welcome.jsp">Back to Welcome</a>
        <a href="CreateUser.jsp"> Create Another User</a>
        <a href="ListUsersServlet">View All Users</a>
        <a href="logout.jsp">Logout</a>
    </div>
</body>
</html>