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
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body  { font-family: Arial, sans-serif;
         background: #f5f5f5;
          text-align: center; 
          padding: 30px; 
          }
        h2    { margin-bottom: 20px;
         color: #333; }

        .box  {
            display: inline-block;
             text-align: left;
            background: #fff;
             border-radius: 8px;
            box-shadow: 0 0 10px #ccc; 
            width: 500px;

        }

       
        .tabs {
            display: flex;
            border-bottom: 2px solid#0078d4;
            background: #f8f9fa;
        }
        .tab-btn {
            flex: 1; 
            padding: 10px 4px;
            border: none; 
            background: none;
            cursor: pointer;
             font-size: 0.82em;
            font-weight: bold; 
            color: #555;
            border-bottom: 3px solid transparent;
            transition: all 0.2s;
        }
        .tab-btn:hover  { background: #e8f5e9; color:#0078d4; }
        .tab-btn.active { color:#0078d4; border-bottom: 3px solid#0078d4; background: #fff; }

        .tab-content { display: none; padding: 18px 22px; }
        .tab-content.active { display: block; }

        label {
            display: block; font-weight: bold;
            font-size: 0.88em; margin-top: 11px; color: #333;
        }
        input[type="text"],
        input[type="email"],
        input[type="password"],
        textarea, select {
            width: 100%; padding: 7px 9px; margin-top: 3px;
            border: 1px solid #ccc; border-radius: 4px;
            font-size: 0.92em; font-family: Arial, sans-serif;
        }
        textarea { resize: vertical;
         min-height: 65px;
          }
        input:focus, textarea:focus, select:focus {
            border-color:#0078d4; outline: none;
            box-shadow: 0 0 0 2px rgb(0, 0, 255);
        }

        

        .checkbox-group label {
            font-weight: normal;
            display: flex; align-items: center;
            gap: 8px; margin-top: 8px; cursor: pointer;
        }
        .checkbox-group input[type="checkbox"] { width: auto; 
        margin: 0;
         }

        .form-footer {
            padding: 15px 22px;
             border-top: 1px solid #eee;
            display: flex; 
            gap: 10px;
        }
        .btn-create {
            flex:1;
             padding: 10px;
             background:#0078d4;
            color: white;
             font-weight: bold;
              font-size: 0.95em;
            border: none;
             border-radius: 4px;
             
        }
        .btn-create:hover { background: rgb(0, 0, 255); }
        .btn-reset {
            padding: 10px 18px; background: #f44336;
            color: white; font-weight: bold; font-size: 0.95em;
            border: none; border-radius: 4px; cursor: pointer;
        }
        .btn-reset:hover { background: #d32f2f; }
        .btn-back {
            padding: 10px 14px; background: #f0f0f0;
            color: #333; font-weight: bold;
            border: 1px solid #ccc; border-radius: 4px;
            cursor: pointer; text-decoration: none;
            display: flex; align-items: center; font-size: 0.95em;
        }
        .btn-back:hover { background: #e0e0e0; }

        .msg-error {
            background: #fdecea; color: #c62828;
            border: 1px solid #ef9a9a; border-radius: 4px;
            padding: 9px 14px; margin: 12px 22px 0; font-size: 0.9em;
        }
    </style>

    <script>
        function autoFill() {
            var first = document.getElementById("firstName").value.trim();
            var last  = document.getElementById("lastName").value.trim();
            var full  = document.getElementById("fullName");
            if (full.dataset.auto !== "false") {
                full.value = (first + " " + last).trim();
            }
        }

        function validate() {
            var first   = document.getElementById("firstName").value.trim();
            var last    = document.getElementById("lastName").value.trim();
            var user    = document.getElementById("username").value.trim();
            var email   = document.getElementById("email").value.trim();
            var pwd     = document.getElementById("password").value;
            var confirm = document.getElementById("confirmPassword").value;
            var ou      = document.getElementById("baseOU").value;

            if (!first || !last || !user) {
                alert("First Name, Last Name and Username are required.");
                showTab('general', document.querySelectorAll('.tab-btn')[0]);
                return false;
            }
            if (!ou) {
                alert("Please select an Organizational Unit.");
                showTab('account', document.querySelectorAll('.tab-btn')[4]);
                return false;
            }
            if (email && !/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(email)) {
                alert("Enter a valid email address.");
                return false;
            }
            if (!pwd || pwd !== confirm) {
                alert("Passwords are required and must match.");
                showTab('account', document.querySelectorAll('.tab-btn')[4]);
                return false;
            }
            return true;
        }

        function showTab(name, btn) {
            document.querySelectorAll('.tab-content').forEach(t => t.classList.remove('active'));
            document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
            document.getElementById('tab-' + name).classList.add('active');
            btn.classList.add('active');
        }
    </script>
</head>
<body>
<h2>Create AD User</h2>

<div class="box">

    <% if (!error.isEmpty()) { %>
        <div class="msg-error">✖ <%= error %></div>
    <% } %>
    <div class="tabs">
        <button type="button" class="tab-btn active" onclick="showTab('general',this)">General</button>
        <button type="button" class="tab-btn" onclick="showTab('address',this)">Address</button>
        <button type="button" class="tab-btn" onclick="showTab('telephones',this)">Telephones</button>
        <button type="button" class="tab-btn" onclick="showTab('organization',this)">Organization</button>
        <button type="button" class="tab-btn" onclick="showTab('account',this)">Account</button>
    </div>

    <form action="CreateUserServlet" method="post" onsubmit="return validate();">
        <div id="tab-general" class="tab-content active">

            <div class="row2">
                <div>
                    <label>First Name *</label>
                    <input type="text" id="firstName" name="firstName" oninput="autoFill()" required/>
                </div>
                <div>
                    <label>Last Name *</label>
                    <input type="text" id="lastName" name="lastName" oninput="autoFill()" required/>
                </div>
            </div>

            <label>Full Name</label>
            <input type="text" id="fullName" name="fullName" data-auto="true"
                   onfocus="this.dataset.auto='false'"/>

            <label>Display Name</label>
            <input type="text" name="displayName"/>

            <label>Initials</label>
            <input type="text" name="initials" style="width:80px"/>

            <label>Description</label>
            <input type="text" name="description"/>

            <label>Office</label>
            <input type="text" name="office"/>

            <label>Telephone</label>
            <input type="text" name="telephone"/>

            <label>Email</label>
            <input type="email" id="email" name="email"/>

            <label>Web Page</label>
            <input type="text" name="webpage"/>
        </div>
        <div id="tab-address" class="tab-content">

            <label>Street</label>
            <textarea name="street"></textarea>

            <label>P.O. Box</label>
            <input type="text" name="poBox"/>

            <label>City</label>
            <input type="text" name="city"/>

            <div class="row2">
                <div>
                    <label>State / Province</label>
                    <input type="text" name="state"/>
                </div>
                <div>
                    <label>Zip / Postal Code</label>
                    <input type="text" name="zip"/>
                </div>
            </div>

            <label>Country / Region</label>
            <select name="country">
                <option value="">-- Select --</option>
                <option value="India">India</option>
                <option value="United States">United States</option>
                <option value="United Kingdom">United Kingdom</option>
                <option value="Australia">Australia</option>
                <option value="Canada">Canada</option>
                <option value="Germany">Germany</option>
                <option value="France">France</option>
                <option value="Singapore">Singapore</option>
            </select>
        </div>
        <div id="tab-telephones" class="tab-content">

            <label>Home</label>
            <input type="text" name="homePhone"/>

            <label>Pager</label>
            <input type="text" name="pager"/>

            <label>Mobile</label>
            <input type="text" name="mobile"/>

            <label>Fax</label>
            <input type="text" name="fax"/>

            <label>IP Phone</label>
            <input type="text" name="ipPhone"/>

            <label>Notes</label>
            <textarea name="notes"></textarea>
        </div>

        <div id="tab-organization" class="tab-content">

            <label>Job Title</label>
            <input type="text" name="jobTitle"/>

            <label>Department</label>
            <input type="text" name="department"/>

            <label>Company</label>
            <input type="text" name="company"/>

            <label>Manager</label>
            <input type="text" name="manager" placeholder="e.g. jsmith"/>
        </div>
        <div id="tab-account" class="tab-content">

            <label>Username *</label>
            <div style="display:flex; align-items:center; gap:6px; margin-top:3px;">
                <input type="text" id="username" name="username" style="flex:1" required/>
                <span style="color:#666; font-size:0.9em; white-space:nowrap;">@mydomain.local</span>
            </div>

            <label>Password *</label>
            <input type="password" id="password" name="password" required/>

            <label>Confirm Password *</label>
            <input type="password" id="confirmPassword" name="confirmPassword" required/>

            <label style="margin-top:14px; color:#0078d4 font-size:0.8em; text-transform:uppercase; letter-spacing:0.5px;">
                Password Options
            </label>
            <div class="checkbox-group">
                <label><input type="checkbox" name="mustChangePassword"/> User must change password at next logon</label>
                <label><input type="checkbox" name="cannotChangePassword"/> User cannot change password</label>
                <label><input type="checkbox" name="pwdNeverExpires"/> Password never expires</label>
                <label><input type="checkbox" name="accountDisabled"/> Account disabled</label>
            </div>

            <label style="margin-top:14px; color:#0078d4; font-size:0.8em; text-transform:uppercase; letter-spacing:0.5px;">
                Organizational Unit *
            </label>
            <select id="baseOU" name="baseOU" required>
                <option value="">-- Select OU --</option>
                <c:forEach var="ou" items="${ouList}">
                    <option value="${ou.dn}">${ou.dn}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-footer">
            <a href="welcome.jsp" class="btn-back"> Back</a>
            <button type="reset"  class="btn-reset">Reset</button>
            <button type="submit" class="btn-create">Create User</button>
        </div>

    </form>
</div>
</body>
</html>
