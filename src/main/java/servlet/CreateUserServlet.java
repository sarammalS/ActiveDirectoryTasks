package servlet;

import javax.naming.directory.*;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.NamingEnumeration;

@WebServlet("/CreateUserServlet")
public class CreateUserServlet extends HttpServlet {

    // Update BASE_DN to match your actual AD OU path. Example: OU=Employees,DC=mydomain,DC=local
	private static final String BASE_DN = "OU=Employees,DC=mydomain,DC=local";
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String fullName = request.getParameter("fullName");
        if (fullName != null) fullName = fullName.trim();
        String username = request.getParameter("username");
        String displayName = request.getParameter("displayName");
        String initials = request.getParameter("initials");
        String description = request.getParameter("description");
        String office = request.getParameter("office");
        String telephone = request.getParameter("telephone");
        String email = request.getParameter("email");
        String webpage = request.getParameter("webpage");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        boolean mustChangePwd = request.getParameter("mustChangePassword") != null;
        boolean pwdNeverExpires = request.getParameter("pwdNeverExpires") != null;
        boolean accountDisabled = request.getParameter("accountDisabled") != null;
        boolean cannotChangePassword = request.getParameter("cannotChangePassword") != null;

        // Basic validation
        if(firstName==null || lastName==null || username==null || password==null ||
                firstName.trim().isEmpty() || lastName.trim().isEmpty() || username.trim().isEmpty() ||
                password.trim().isEmpty() || !password.equals(confirmPassword)){
            request.setAttribute("error", "Required fields missing or passwords do not match.");
            forwardWithPreviousValues(request, response);
            return;
        }

        String loggedInUser = (String) session.getAttribute("user");
        String loggedInPassword = (String) session.getAttribute("userPassword");

        if (loggedInUser == null || loggedInPassword == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        DirContext ctx = null;
        try {
            ctx = util.LdapUtil.getUserContext(loggedInUser, loggedInPassword);
            if (ctx == null) {
                request.setAttribute("error", "Failed to connect to LDAP as logged-in user. Insufficient permissions or invalid credentials.");
                forwardWithPreviousValues(request, response);
                return;
            }

            Attributes attrs = new BasicAttributes(true);
            Attribute objClass = new BasicAttribute("objectClass");
            objClass.add("top");
            objClass.add("person");
            objClass.add("organizationalPerson");
            objClass.add("user");
            attrs.put(objClass);

            attrs.put("cn", fullName);
            attrs.put("givenName", firstName);
            attrs.put("sn", lastName);
            attrs.put("displayName", displayName != null ? displayName : fullName);
            attrs.put("sAMAccountName", username);

            if(email!=null && !email.isEmpty()) attrs.put("mail", email);
            if(telephone!=null && !telephone.isEmpty()) attrs.put("telephoneNumber", telephone);
            if(description!=null && !description.isEmpty()) attrs.put("description", description);
            if(office!=null && !office.isEmpty()) attrs.put("physicalDeliveryOfficeName", office);
            if(initials!=null && !initials.isEmpty()) attrs.put("initials", initials);
            if(webpage!=null && !webpage.isEmpty()) attrs.put("wWWHomePage", webpage);

            // Check if user already exists
            String userDN = "CN=" + fullName + "," + BASE_DN;
            try {
                SearchControls sc = new SearchControls();
                sc.setSearchScope(SearchControls.ONELEVEL_SCOPE);
                String filter = "(cn=" + fullName + ")";
                NamingEnumeration<SearchResult> results = ctx.search(BASE_DN, filter, sc);
                if (results.hasMore()) {
                    request.setAttribute("error", "A user with this Full Name already exists. Please choose a unique name.");
                    forwardWithPreviousValues(request, response);
                    return;
                }
            } catch (NamingException e) {
                // If search fails, continue to creation (could be permission issue)
            }

            ctx.createSubcontext(userDN, attrs);

            // Password & Account control
            ModificationItem[] mods = new ModificationItem[3];

            try {
                byte[] pwdBytes = ("\"" + password + "\"").getBytes("UTF-16LE");
                mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                        new BasicAttribute("unicodePwd", pwdBytes));
            } catch (UnsupportedEncodingException e) {
                throw new NamingException("Password encoding error: " + e.getMessage());
            }

            int userAccountControl = 512; // NORMAL_ACCOUNT
            if(accountDisabled) userAccountControl |= 0x2;
            if(pwdNeverExpires) userAccountControl |= 0x10000;

            mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("userAccountControl", Integer.toString(userAccountControl)));

            mods[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("pwdLastSet", mustChangePwd ? "0" : "-1"));

            ctx.modifyAttributes(userDN, mods);

            request.setAttribute("message", "User created successfully!");
            request.getRequestDispatcher("confirmation.jsp").forward(request, response);

        } catch (NamingException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error creating user: " + e.getMessage());
            forwardWithPreviousValues(request, response);
        } finally {
            if(ctx!=null) try { ctx.close(); } catch(Exception ignore) {}
        }
    }

    private void forwardWithPreviousValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward back to form with previous values
        request.getRequestDispatcher("CreateUser.jsp").forward(request, response);
    }
}