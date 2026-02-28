<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Edit User</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f4f6f9; }
        .box { width: 420px; margin: 50px auto; padding: 25px;
               background: #fff; border-radius: 8px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); }
        h2   { text-align: center; margin-bottom: 20px; }
        label { display: block; margin-top: 12px; font-weight: bold; }
        input[type="text"],
        input[type="email"] { width: 100%; padding: 8px; margin-top: 4px;
                              border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        input[readonly] { background: #f0f0f0; color: #666; }
        input[type="submit"] { width: 100%; margin-top: 20px; padding: 10px;
                               background: #007bff; color: white; font-weight: bold;
                               border: none; border-radius: 4px; cursor: pointer; }
        input[type="submit"]:hover { background: #0056b3; }
        .error   { color: red;   text-align: center; margin-bottom: 10px; }
        .success { color: green; text-align: center; margin-bottom: 10px; }
        .back    { text-align: center; margin-top: 12px; }
        .back a  { color: #007bff; text-decoration: none; }
        .back a:hover { text-decoration: underline; }
    </style>
</head>
<body>
<div class="box">
    <h2>Edit User</h2>

    <c:if test="${not empty error}">
        <div class="error"><c:out value="${error}"/></div>
    </c:if>
    <c:if test="${not empty message}">
        <div class="success"><c:out value="${message}"/></div>
    </c:if>

    <form action="EditUserServlet" method="post">
        <label>Username</label>
        <input type="text" name="username" value="${userAttrs.username}" readonly/>

        <label>First Name</label>
        <input type="text" name="firstName" value="${userAttrs.firstName}" required/>

        <label>Last Name</label>
        <input type="text" name="lastName" value="${userAttrs.lastName}" required/>
        <label>Email</label>
        <input type="email" name="email" value="${userAttrs.email}"/>
        

        <label>Display Name</label>
        <input type="text" name="displayName" value="${userAttrs.displayName}"/>

        <label>Initials</label>
        <input type="text" name="initials" value="${userAttrs.initials}"/>

        <label>Description</label>
        <input type="text" name="description" value="${userAttrs.description}"/>

        <label>Office</label>
        <input type="text" name="office" value="${userAttrs.office}"/>

        <label>Telephone</label>
        <input type="text" name="telephone" value="${userAttrs.telephone}"/>

        
        <label>Web Page</label>
        <input type="text" name="webpage" value="${userAttrs.webpage}"/>

        <input type="submit" value="Save Changes"/>
    </form>

    <div class="back">
        <a href="ListUsersServlet"> Back to User List</a>
    </div>
</div>
</body>
</html>