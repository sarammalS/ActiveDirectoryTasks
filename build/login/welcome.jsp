<%@ page session="true" %>
    <% Object user=session.getAttribute("user"); if (user==null) { response.sendRedirect("login.jsp"); return; } %>
        <html>

        <head>
            <title>Welcome</title>
        </head>

        <body>
            <h2>Welcome, <%= user %>!</h2>
            <p>You have successfully logged in.</p>
            <a href="logout.jsp">Logout</a>
        </body>

        </html>