package util;

import Model.OUModel;
import Model.User;
import javax.naming.*;
import javax.naming.directory.*;
import java.util.*;

public class LdapUtil {

    private static final String LDAP_URL = "ldaps://WIN-GD9BC63P04I.mydomain.local:636";
    private static final String DOMAIN   = "mydomain.local";
    private static final String BASE_DN  = "DC=mydomain,DC=local";
    public static DirContext getUserContext(String username, String password) throws NamingException {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL,LDAP_URL);
        env.put(Context.SECURITY_AUTHENTICATION,"simple");
        env.put(Context.SECURITY_PRINCIPAL, username + "@" + DOMAIN);
        env.put(Context.SECURITY_CREDENTIALS, password);
        env.put(Context.SECURITY_PROTOCOL,"ssl");
        env.put("java.naming.referral","ignore");
        return new InitialDirContext(env);
    }

    public static boolean authenticate(String username, String password) {
        try {
            getUserContext(username, password).close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static final String[] ATTRS = {
        "sAMAccountName", "givenName", "sn", "cn", "mail",
        "displayName", "initials", "description",
        "physicalDeliveryOfficeName", "telephoneNumber", "wWWHomePage",
        "homePhone", "pager", "mobile", "TelephoneNumber", "ipPhone", "info",
        "streetAddress", "postOfficeBox", "l", "st", "postalCode", "co",
        "title", "department", "company", "manager"
    };
    public static List<OUModel> getOUList(String bindUser, String bindPassword) {
        List<OUModel> ouList = new ArrayList<>();
        DirContext ctx = null;
        try {
            ctx = getUserContext(bindUser, bindPassword);
            SearchControls sc = new SearchControls();
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            sc.setReturningAttributes(new String[]{"name", "distinguishedName"});
            NamingEnumeration<SearchResult> results = ctx.search(
                BASE_DN, "(objectClass=organizationalUnit)", sc);
            while (true) {
                try {
                    if (!results.hasMore()) break;
                    Attributes attrs = results.next().getAttributes();
                    String name = get(attrs, "name");
                    String dn   = get(attrs, "distinguishedName");
                    if (!name.isEmpty()) ouList.add(new OUModel(name, dn));
                } catch (PartialResultException e) {
                    break;
                }
            }
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            close(ctx);
        }
        return ouList;
    }

  
  
    public static List<User> getAllUsers(String username, String password) {
        List<User> users = new ArrayList<>();
        DirContext ctx = null;
        try {
            ctx = getUserContext(username, password);
            SearchControls sc = new SearchControls();
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            sc.setReturningAttributes(ATTRS);
            NamingEnumeration<SearchResult> results = ctx.search(
                BASE_DN, "(&(objectClass=user)(objectCategory=person))", sc);
            while (results.hasMore()) {
                users.add(mapUser(results.next().getAttributes()));
            }
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            close(ctx);
        }
        return users;
    }

    public static User getUserByUsername(String searchUsername, String bindUser, String bindPassword) {
        DirContext ctx = null;
        try {
            ctx = getUserContext(bindUser, bindPassword);
            SearchControls sc = new SearchControls();
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            sc.setReturningAttributes(ATTRS);
            NamingEnumeration<SearchResult> results = ctx.search(
                BASE_DN, "(sAMAccountName=" + escape(searchUsername) + ")", sc);
            if (results.hasMore()) return mapUser(results.next().getAttributes());
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            close(ctx);
        }
        return null;
    }

   
    public static boolean updateUser(String username,
            String firstName, String lastName, String displayName,
            String initials, String description, String office,
            String telephone, String email, String webpage,
            String homePhone, String pager, String mobile,
            String fax, String ipPhone, String notes,
            String street, String poBox, String city,
            String state, String zip, String country,
            String jobTitle, String department, String company,
            String manager, 
            String bindUser, String bindPassword) {

        DirContext ctx = null;
        try {
            ctx = getUserContext(bindUser, bindPassword);
            SearchControls sc = new SearchControls();
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> results = ctx.search(
                BASE_DN, "(sAMAccountName=" + escape(username) + ")", sc);
            if (!results.hasMore()) return false;

            String dn = results.next().getNameInNamespace();
            List<ModificationItem> mods = new ArrayList<>();

           
            addMod(mods, "givenName",  firstName);
            addMod(mods, "sn", lastName);
            addMod(mods, "displayName", displayName);
            addMod(mods, "initials", initials);
            addMod(mods, "description", description);
            addMod(mods, "physicalDeliveryOfficeName", office);
            addMod(mods, "telephoneNumber", telephone);
            addMod(mods, "mail", email);
            addMod(mods, "wWWHomePage", webpage);
            addMod(mods, "homePhone", homePhone);
            addMod(mods, "pager",pager);
            addMod(mods, "mobile", mobile);
            addMod(mods, "TelephoneNumber",fax);
            addMod(mods, "ipPhone", ipPhone);
            addMod(mods, "info",  notes);
          
            addMod(mods, "streetAddress",street);
            addMod(mods, "postOfficeBox",poBox);
            addMod(mods, "l",city);
            addMod(mods, "st",state);
            addMod(mods, "postalCode",zip);
            addMod(mods, "co", country);
            addMod(mods, "title", jobTitle);
            addMod(mods, "department",   department);
            addMod(mods, "company", company);
            addMod(mods,"manager",manager);
        
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            close(ctx);
        }
        return false;
    }

  
    private static User mapUser(Attributes a) throws NamingException {
        return new User(
            get(a,"sAMAccountName"),
            get(a,"givenName"),
            get(a,"sn"),
            get(a,"cn"),
            get(a,"mail"),
            get(a,"displayName"),
            get(a,"initials"),
            get(a,"description"),
            get(a,"physicalDeliveryOfficeName"),
            get(a,"telephoneNumber"),
            get(a,"wWWHomePage"),
            get(a,"homePhone"),
            get(a,"pager"),
            get(a,"mobile"),
            get(a,"TelephoneNumber"),
            get(a,"ipPhone"),
            get(a,"info"),
            get(a,"streetAddress"),
            get(a,"postOfficeBox"),
            get(a,"l"),
            get(a,"st"),
            get(a,"postalCode"),
            get(a,"co"),
            get(a,"title"),
            get(a,"department"),
            get(a,"company"),
            get(a,"manager")
            
        );
    }

    private static void addMod(List<ModificationItem> mods, String attr, String value) {
        if (value == null) return;
        if (value.trim().isEmpty())
            mods.add(new ModificationItem(DirContext.REMOVE_ATTRIBUTE,  new BasicAttribute(attr)));
        else
            mods.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(attr, value)));
    }

    private static String get(Attributes a, String name) throws NamingException {
        return a.get(name) != null ? a.get(name).get().toString() : "";
    }

    private static void close(DirContext ctx) {
        if (ctx != null) try { ctx.close(); } catch (Exception ignored) {}
    }

    private static String escape(String v) {
        return v.replace("\\","\\5c").replace("*","\\2a")
                .replace("(","\\28").replace(")","\\29")
                .replace("\u0000","\\00");
    }
}