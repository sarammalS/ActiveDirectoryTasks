<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Edit User</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f6f9;
        }
        .edit-box {
            width: 400px;
            margin: 60px auto;
            padding: 25px;
            background: #ffffff;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        }
        h2 {
            text-align: center;
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
        }
        input[type="text"],
        input[type="email"] {
            width: 100%;
            padding: 8px;
            margin-top: 5px;
            border-radius: 4px;
            border: 1px solid #ccc;
        }
        input[type="submit"] {
            width: 100%;
            margin-top: 20px;
            padding: 10px;
            border: none;
            background-color: #007bff;
            color: white;
            font-weight: bold;
            border-radius: 4px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #0056b3;
        }
        .success {
            color: green;
            text-align: center;
            margin-bottom: 10px;
        }
        .error {
            color: red;
            text-align: center;
            margin-bottom: 10px;
        }
        .back-link {
            text-align: center;
            margin-top: 10px;
        }
    </style>
</head>
<body>

<div class="edit-box">
    <h2>Edit User</h2>

    <!-- Success or Error Message -->
    <c:if test="${not empty message}">
        <div class="success">
            <c:out value="${message}" />
        </div>
    </c:if>

    <form action="EditUserServlet" method="post">

        <label>Username</label>
        <input type="text" name="username"
               value="${userAttrs.username}" readonly />

        <label>First Name</label>
        <input type="text" name="firstName"
               value="${userAttrs.firstName}" required />

        <label>Last Name</label>
        <input type="text" name="lastName"
               value="${userAttrs.lastName}" required />

        <label>Email</label>
        <input type="email" name="email"
               value="${userAttrs.email}" required />
            
		
      
       <label>Display Name</label>
		<input type="text" name="displayName"
      		 value="${userAttrs.displayName}" required />

		<label>Initials</label>
		<input type="text" name="initials"
     	  value="${userAttrs.initials}" /required>

		<label>Description</label>
		<input type="text" name="description"
       		value="${userAttrs.description}" / >

		<label>Office</label>
		<input type="text" name="office"
       		value="${userAttrs.office}" / >

        <label>Phone Number</label>
        <input type="text" name="telephone"
       value="${userAttrs.phoneNumber}" / >

        <label>Web Page</label>
        <input type="text" name="webPage"
        value="${userAttrs.webPage}" / >
            
         
		
        <input type="submit" value="Save Changes" />
    </form>

    <div class="back-link">
        <a href="ListUsersServlet">Back to User List</a>
    </div>
</div>

</body>
</html>