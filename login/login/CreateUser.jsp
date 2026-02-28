<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Create User</title>
    <style>
        .form-container { width: 400px; margin: 40px auto; padding: 20px; border: 1px solid #ccc; border-radius: 8px; background: #fff; }
        label { display: block; margin-top: 10px; font-weight: bold; }
        input { width: 100%; padding: 8px; margin-top: 5px; }
        button { margin-top: 20px; padding: 10px 20px; }
    </style>
</head>
<body>
<div class="form-container">
    <h2>Create User</h2>
    <form action="createUser" method="post">
        <label>Username:</label>
        <input type="text" name="username" required />
        <label>First Name:</label>
        <input type="text" name="firstName" required />
        <label>Last Name:</label>
        <input type="text" name="lastName" required />
        <label>Email Address:</label>
        <input type="email" name="email" required />
        <button type="submit">Create</button>
    </form>
</div>
</body>
</html>
