package servlet;

import Model.User;
import util.LdapUtil;
import Model.OUModel;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/CreateUserServlet")
public class CreateUserServlet extends HttpServlet {

    private static final String DOMAIN = "mydomain.local";

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect("login.jsp"); return;
        }
        String bindUser = (String) session.getAttribute("user");
        String bindPass = (String) session.getAttribute("userPassword");
        req.setAttribute("ouList", util.LdapUtil.getOUList(bindUser, bindPass));
        req.getRequestDispatcher("CreateUser.jsp").forward(req, res);
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect("login.jsp"); return;
        }
        String selectedOU= req.getParameter("baseOU");
        String username= req.getParameter("username");
        String firstName= req.getParameter("firstName");
        String lastName= req.getParameter("lastName");
        String fullName= req.getParameter("fullName");
        String displayName = req.getParameter("displayName");
        String initials = req.getParameter("initials");
        String description = req.getParameter("description");
        String office= req.getParameter("office");
        String telephone= req.getParameter("telephone");
        String email= req.getParameter("email");
        String webpage = req.getParameter("webpage");
        String password = req.getParameter("password");
        String confirmPw= req.getParameter("confirmPassword");

        boolean mustChangePwd   = req.getParameter("mustChangePassword")  != null;
        boolean pwdNeverExpires = req.getParameter("pwdNeverExpires")     != null;
        boolean accountDisabled = req.getParameter("accountDisabled")     != null;
        boolean cannotChangePwd = req.getParameter("cannotChangePassword") != null;
        String homePhone= req.getParameter("homePhone");
        String pager = req.getParameter("pager");
        String mobile  = req.getParameter("mobile");
        String fax= req.getParameter("fax");
        String ipPhone= req.getParameter("ipPhone");
        String notes= req.getParameter("notes");
        String street = req.getParameter("street");
        String poBox = req.getParameter("poBox");
        String city= req.getParameter("city");
        String state = req.getParameter("state");
        String zip = req.getParameter("zip");
        String country= req.getParameter("country");
        String jobTitle = req.getParameter("jobTitle");
        String department= req.getParameter("department");
        String company= req.getParameter("company");
        String manager= req.getParameter("manager"); 
        if (isEmpty(username) || isEmpty(firstName) || isEmpty(lastName)|| isEmpty(password) || !password.equals(confirmPw) || isEmpty(selectedOU)) {
            req.setAttribute("error", "Required fields missing or passwords do not match.");
            return;
        }

        String bindUser = (String) session.getAttribute("user");
        String bindPass = (String) session.getAttribute("userPassword");

        String cn     = !isEmpty(fullName) ? fullName.trim() : (firstName.trim() + " " + lastName.trim());
        String userDN = "CN=" + cn + "," + selectedOU;

        DirContext ctx = null;
        try {
            ctx = util.LdapUtil.getUserContext(bindUser, bindPass);

            Attributes attrs = new BasicAttributes(true);
            Attribute oc = new BasicAttribute("objectClass");
            oc.add("top"); oc.add("person");
            oc.add("organizationalPerson"); oc.add("user");
            attrs.put(oc);
            attrs.put("cn",cn);
            attrs.put("sAMAccountName", username);
            attrs.put("userPrincipalName", username + "@" + DOMAIN);
            attrs.put("givenName", firstName);
            attrs.put("sn",lastName);
            if (!isEmpty(displayName))
            	attrs.put("displayName", displayName);
            if (!isEmpty(initials))  
            	attrs.put("initials",initials);
            if (!isEmpty(description))
            	attrs.put("description",description);
            if (!isEmpty(office))    
            	attrs.put("physicalDeliveryOfficeName", office);
            if (!isEmpty(telephone))  
            	attrs.put("telephoneNumber", telephone);
            if (!isEmpty(email))    
            	attrs.put("mail", email);
            if (!isEmpty(webpage)) 
            	attrs.put("wWWHomePage",webpage);
            if (!isEmpty(homePhone)) 
            	attrs.put("homePhone", homePhone);
            if (!isEmpty(pager))     
            	attrs.put("pager",  pager);
            if (!isEmpty(mobile))   
            	attrs.put("mobile", mobile);
            if (!isEmpty(fax))    
            	attrs.put("faxx",fax);
            if (!isEmpty(ipPhone))  
            	attrs.put("ipPhone",  ipPhone);
            if (!isEmpty(notes))    
            	attrs.put("info",notes);
            if (!isEmpty(street))   
            	attrs.put("streetAddress",street);
            if (!isEmpty(poBox))   
            	attrs.put("postOfficeBox",poBox);
            if (!isEmpty(city))    
            	attrs.put("l", city);
            if (!isEmpty(state))   
            	attrs.put("st",state);
            if (!isEmpty(zip))      
            	attrs.put("postalCode",zip);
            if (!isEmpty(country))  
            	attrs.put("co",country);
            if (!isEmpty(jobTitle)) 
            	attrs.put("title",jobTitle);
            if (!isEmpty(department)) 
            	attrs.put("department", department);
            if (!isEmpty(company))  
            	attrs.put("company", company);
            if (!isEmpty(manager)) {
                try {
                    SearchControls sc = new SearchControls();
                    sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
                    sc.setReturningAttributes(new String[]{"distinguishedName"});

                    String baseDN = "DC=" + DOMAIN.replace(".", ",DC=");
                    String filter = "(&(objectClass=user)(sAMAccountName=" + manager.trim() + "))";

                    NamingEnumeration<SearchResult> managerResults = ctx.search(baseDN, filter, sc);
                    if (managerResults.hasMore()) {
                        String managerDN = managerResults.next().getNameInNamespace();
                        attrs.put("manager", managerDN);
                    } else {
                        req.setAttribute("error", "Manager not found: " + manager);
                       
                        return;
                    }
                } catch (NamingException e) {
                    req.setAttribute("error", "Error looking up manager: " + e.getMessage());
                  
                    return;
                }
            }
            attrs.put("userAccountControl", "514");

            
            ctx.createSubcontext(userDN, attrs);

            byte[] pwd = ("\"" + password + "\"").getBytes(StandardCharsets.UTF_16LE);
            ctx.modifyAttributes(userDN, new ModificationItem[]{
                new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("unicodePwd", pwd))
            });

            int uac = 512;
            if (accountDisabled)  uac |= 2;
            if (pwdNeverExpires)  uac |= 65536;
            if (cannotChangePwd)  uac |= 64;
            ctx.modifyAttributes(userDN, new ModificationItem[]{
                new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("userAccountControl", Integer.toString(uac)))
            });

            if (mustChangePwd && !pwdNeverExpires) {
                ctx.modifyAttributes(userDN, new ModificationItem[]{
                    new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                        new BasicAttribute("pwdLastSet", "0"))
                });
            }

            req.setAttribute("message", "User '" + cn + "' created successfully.");
            req.getRequestDispatcher("confirmation.jsp").forward(req, res);

        } catch (NamingException e) {
          
            try { if (ctx != null) ctx.destroySubcontext(userDN); } catch (Exception ignored) {}
            e.printStackTrace();
            req.setAttribute("error", "Active Directory error: " + e.getMessage());
            
        } finally {
            if (ctx != null)
            	try { ctx.close(); 
            	} 
            catch (Exception ignored) {}
        }
    }


    private boolean isEmpty(String s) { 
    	return s == null || s.trim().isEmpty(); 
    	}
}
