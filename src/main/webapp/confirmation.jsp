<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>User Creation Confirmation</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f5f5f5; text-align: center; padding-top: 50px; }
        .box { background: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 0 10px #ccc; display: inline-block; }
        .success { color: #4CAF50; font-size: 1.3em; margin-bottom: 20px; }
        a { display: block; margin-top: 20px; color: #0078d4; text-decoration: none; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <div class="box">
        <div class="success">User created successfully!</div>
        <a href="welcome.jsp">Back to Welcome</a>
        <a href="CreateUser.jsp">Create Another User</a>
        <a href="logout.jsp">Logout</a>
    </div>
</body>
</html>
