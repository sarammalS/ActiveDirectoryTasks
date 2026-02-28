<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String error = request.getAttribute("error") != null ? (String) request.getAttribute("error") : "";
%>
<!DOCTYPE html>
<html>
<head>
    <title>Create AD User</title>
    <style>
        body  { font-family: Arial; background: #f5f5f5; text-align: center; padding-top: 30px; }
        form  { display: inline-block; text-align: left; background: #fff; padding: 25px;
                border-radius: 8px; box-shadow: 0 0 10px #ccc; }
        input { width: 300px; padding: 6px; margin: 6px 0; box-sizing: border-box; }
        input[type=submit], input[type=reset] { width: 120px; color: white; border: none; cursor: pointer; }
        input[type=submit] { background: #4CAF50; }
        input[type=submit]:hover { background: #45a049; }
        input[type=reset]  { background: #f44336; }
        input[type=reset]:hover  { background: #d32f2f; }
        label  { display: block; font-weight: bold; margin-top: 10px; }
        .section { font-size: 1.1em; font-weight: bold; margin: 20px 0 5px; color: #333;
                   border-bottom: 1px solid #ccc; padding-bottom: 4px; }
        .error { color: red; font-weight: bold; margin-bottom: 15px; }
    </style>
    <script>
    function autoFill() {
        var first = document.getElementById("firstName").value.trim();
        var last  = document.getElementById("lastName").value.trim();
        document.getElementById("fullName").value = (first + " " + last).trim();
    }

        function validate() {
            var first    = document.getElementById("firstName").value.trim();
            var last     = document.getElementById("lastName").value.trim();
            var username = document.getElementById("username").value.trim();
            var email    = document.getElementById("email").value.trim();
            var pwd      = document.getElementById("password").value;
            var confirm  = document.getElementById("confirmPassword").value;

            if (!first || !last || !username) {
                alert("First Name, Last Name, and Username are required."); return false;
            }
            if (email && !/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(email)) {
                alert("Enter a valid email address."); return false;
            }
            if (!pwd || pwd !== confirm) {
                alert("Passwords are required and must match."); return false;
            }
            return true;
        }
    </script>
</head>
<body>
<h2>Create AD User</h2>

<% if (!error.isEmpty()) { %>
    <div class="error"><%= error %></div>
<% } %>

<form action="CreateUserServlet" method="post" onsubmit="return validate();">

    <div class="section">General</div>

    <label>First Name:</label>
    <input type="text" id="firstName" name="firstName" oninput="autoFill()" required/>

    <label>Last Name:</label>
    <input type="text" id="lastName" name="lastName" oninput="autoFill()" required/>

    <label>Full Name:</label>
    <input type="text" id="fullName" name="fullName" />

    <label>Display Name:</label>
    <input type="text" name="displayName"/>

    <label>Initials:</label>
    <input type="text" name="initials"/>

    <label>Description:</label>
    <input type="text" name="description"/>
	
    <label>Office:</label>
    <input type="text" name="office"/>

    <label>Telephone:</label>
    <input type="text" name="telephone"/>

    <label>Email:</label>
    <input type="email" id="email" name="email"/>

    <label>Web Page:</label>
    <input type="text" name="webpage"/>

    <div class="section">Account</div>

    <label>Username:</label>
    <input type="text" id="username" name="username" required/> @mydomain.local

    <label>Password:</label>
    <input type="password" id="password" name="password" required/>

    <label>Confirm Password:</label>
    <input type="password" id="confirmPassword" name="confirmPassword" required/>

    <br/>
    <label>
        <input type="checkbox" name="mustChangePassword"/> User must change password at next logon
    </label>
    <label>
        <input type="checkbox" name="cannotChangePassword"/> User cannot change password
    </label>
    <label>
        <input type="checkbox" name="pwdNeverExpires"/> Password never expires
    </label>
    <label>
        <input type="checkbox" name="accountDisabled"/> Account disabled
    </label>
    <label>Choose Organizational Unit</label>
    <select name="baseOU" required>
    <option value="">select </option>
    <c:forEach var="ou" items="${ouList}">
        <option value="${ou.dn}">
            ${ou.dn}
        </option>
    </c:forEach>
	</select>
	

    <br/>
    <input type="submit" value="Create User"/>
    <input type="reset"  value="Reset"/>
</form>
</body>
</html>