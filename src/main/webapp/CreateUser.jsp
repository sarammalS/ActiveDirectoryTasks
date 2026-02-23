<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String user = (String) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String firstName = request.getAttribute("firstName") != null ? (String) request.getAttribute("firstName") : "";
    String lastName = request.getAttribute("lastName") != null ? (String) request.getAttribute("lastName") : "";
    String fullName = request.getAttribute("fullName") != null ? (String) request.getAttribute("fullName") : firstName + " " + lastName;
    String username = request.getAttribute("username") != null ? (String) request.getAttribute("username") : "";
    String initials = request.getAttribute("initials") != null ? (String) request.getAttribute("initials") : "";
    String displayName = request.getAttribute("displayName") != null ? (String) request.getAttribute("displayName") : fullName;
    String description = request.getAttribute("description") != null ? (String) request.getAttribute("description") : "";
    String office = request.getAttribute("office") != null ? (String) request.getAttribute("office") : "";
    String telephone = request.getAttribute("telephone") != null ? (String) request.getAttribute("telephone") : "";
    String email = request.getAttribute("email") != null ? (String) request.getAttribute("email") : "";
    String webpage = request.getAttribute("webpage") != null ? (String) request.getAttribute("webpage") : "";
    String error = request.getAttribute("error") != null ? (String) request.getAttribute("error") : "";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create AD User</title>
    <style>
        body { font-family: Arial; background: #f5f5f5; text-align: center; padding-top: 30px; }
        form { display: inline-block; text-align: left; background: #fff; padding: 25px; border-radius: 8px; box-shadow: 0 0 10px #ccc; }
        input, select { width: 300px; padding: 6px; margin: 8px 0; }
        input[type=submit], input[type=reset] { width: 120px; color: white; border: none; cursor: pointer; margin-right: 10px; }
        input[type=submit] { background: #4CAF50; }
        input[type=submit]:hover { background: #45a049; }
        input[type=reset] { background-color: #f44336; }
        input[type=reset]:hover { background-color: #d32f2f; }
        label { display: block; font-weight: bold; margin-top: 10px; }
        .tab { font-size: 1.1em; font-weight: bold; margin-top: 20px; color: #333; }
        .checkbox-group { margin-top: 10px; }
        .error { color: red; font-weight: bold; margin-bottom: 15px; }
    </style>
    <script>
        function autoFillFullName() {
            var first = document.getElementById("firstName").value.trim();
            var last = document.getElementById("lastName").value.trim();
            var full = document.getElementById("fullName");
            if(full.value.trim() === "") {
                full.value = first + " " + last;
            }
        }

        function validateForm() {
            var first = document.getElementById("firstName").value.trim();
            var last = document.getElementById("lastName").value.trim();
            var username = document.getElementById("username").value.trim();
            var email = document.getElementById("email").value.trim();
            var pwd = document.getElementById("password").value;
            var confirmPwd = document.getElementById("confirmPassword").value;

            if(!first || !last || !username) {
                alert("First Name, Last Name, and Username are required.");
                return false;
            }
            if(email && !/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(email)) {
                alert("Enter a valid email address.");
                return false;
            }
            if(!pwd || pwd !== confirmPwd) {
                alert("Passwords are required and must match.");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
<h2>Create AD User</h2>

<c:if test="${not empty error}">
    <div class="error"><c:out value="${error}"/></div>
</c:if>

<form action="CreateUserServlet" method="post" onsubmit="return validateForm();">
    <div class="tab">General</div>
    <label>First Name:</label>
    <input type="text" id="firstName" name="firstName" value="<%= firstName %>" onblur="autoFillFullName();" required/>

    <label>Last Name:</label>
    <input type="text" id="lastName" name="lastName" value="<%= lastName %>" onblur="autoFillFullName();" required/>

    <label>Full Name:</label>
    <input type="text" id="fullName" name="fullName" value="<%= fullName %>"/>

    <label>Display Name:</label>
    <input type="text" name="displayName" value="<%= displayName %>"/>

    <label>Initials:</label>
    <input type="text" name="initials" value="<%= initials %>"/>

    <label>Description:</label>
    <input type="text" name="description" value="<%= description %>"/>

    <label>Office:</label>
    <input type="text" name="office" value="<%= office %>"/>

    <label>Telephone Number:</label>
    <input type="text" name="telephone" value="<%= telephone %>"/>

    <label>Email:</label>
    <input type="email" name="email" value="<%= email %>"/>

    <label>Web Page:</label>
    <input type="text" name="webpage" value="<%= webpage %>"/>

    <div class="tab">Account</div>
    <label>User Logon Name:</label>
    <input type="text" id="username" name="username" value="<%= username %>" required/> @mydomain.local

    <label>Password:</label>
    <input type="password" id="password" name="password" required/>

    <label>Confirm Password:</label>
    <input type="password" id="confirmPassword" name="confirmPassword" required/>

    <div class="checkbox-group">
        <input type="checkbox" name="mustChangePassword" /> User must change password at next logon<br/>
        <input type="checkbox" name="cannotChangePassword"/> User cannot change password<br/>
        <input type="checkbox" name="pwdNeverExpires" /> Password never expires<br/>
        <input type="checkbox" name="accountDisabled"/> Account disabled<br/>
    </div>

    <br/>
    <input type="submit" value="Create User"/>
    <input type="reset" value="Cancel"/>
</form>
</body>
</html>