package servlet;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.LdapContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/CreateUserServlet")
public class CreateUserServlet extends HttpServlet {

    private static final String DOMAIN  = "mydomain.local";

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
    	 String BASE_DN = req.getParameter("baseOU");
    	   
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect("login.jsp");
            return;
        }
        String username     = req.getParameter("username");
        String firstName    = req.getParameter("firstName");
        String lastName     = req.getParameter("lastName");
        String displayName  = req.getParameter("displayName");
        String email        = req.getParameter("email");
        String initials     = req.getParameter("initials");
        String description  = req.getParameter("description");
        String office       = req.getParameter("office");
        String telephone    = req.getParameter("telephone");
        String webpage      = req.getParameter("webpage");
        String password     = req.getParameter("password");
        String confirmPw    = req.getParameter("confirmPassword");

        if (isEmpty(username) || isEmpty(firstName) || isEmpty(lastName)
                || isEmpty(password) || !password.equals(confirmPw)) {

            req.setAttribute("error",
                    "Required fields missing or passwords do not match.");
            req.getRequestDispatcher("CreateUser.jsp")
                    .forward(req, res);
            return;
        }

        String bindUser = (String) session.getAttribute("user");
        String bindPass = (String) session.getAttribute("userPassword");

        DirContext ctx = null;
        String userDN = "CN=" + username + "," + BASE_DN;

        try {
           
            ctx = util.LdapUtil.getUserContext(bindUser, bindPass);

          
            Attributes attrs = new BasicAttributes(true);

            Attribute oc = new BasicAttribute("objectClass");
            oc.add("top");
            oc.add("person");
            oc.add("organizationalPerson");
            oc.add("user");
            attrs.put(oc);      
            attrs.put("cn", username);
            attrs.put("sAMAccountName", username);
            attrs.put("userPrincipalName", username + "@" + DOMAIN);
            attrs.put("givenName", firstName);
            attrs.put("sn", lastName);
            if (!isEmpty(displayName)) attrs.put("displayName", displayName);
            if (!isEmpty(email)) attrs.put("mail", email);
            if (!isEmpty(initials)) attrs.put("initials", initials);
            if (!isEmpty(description)) attrs.put("description", description);
            if (!isEmpty(office)) attrs.put("physicalDeliveryOfficeName", office);
            if (!isEmpty(telephone)) attrs.put("telephoneNumber", telephone);
            if (!isEmpty(webpage)) attrs.put("wWWHomePage", webpage);
            attrs.put("userAccountControl", "544");
            ctx.createSubcontext(userDN, attrs);
            byte[] pwd = ("\"" + password + "\"")
                    .getBytes(StandardCharsets.UTF_16LE);
            ModificationItem[] pwdMods = new ModificationItem[]{ new ModificationItem ( DirContext.REPLACE_ATTRIBUTE,
                            new BasicAttribute("unicodePwd", pwd)
                    )
            };

            ctx.modifyAttributes(userDN, pwdMods);

     
            ModificationItem enableAccount = new ModificationItem(
                    DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("userAccountControl", "512")
            );

            ctx.modifyAttributes(userDN,
                    new ModificationItem[]{enableAccount});

            req.setAttribute("message",
                    "User created successfully.");
            req.getRequestDispatcher("confirmation.jsp")
                    .forward(req, res);

        } catch (NamingException e) {

           try {
                if (ctx != null) {
                    ctx.destroySubcontext(userDN);
                }
            } catch (Exception ignored) {}

            e.printStackTrace();
            req.setAttribute("error","Active Directory error: " + e.getMessage());
            req.getRequestDispatcher("CreateUser.jsp").forward(req, res);
        } finally {
            if (ctx != null) {
                try { ctx.close(); } catch (Exception ignored) {}
            }
        }
    }
    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}