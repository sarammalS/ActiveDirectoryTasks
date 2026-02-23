package util;

import Model.User;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class LdapUtil {

    /* ================= CONFIG ================= */
    private static final String LDAP_URL = "ldaps://WIN-GD9BC63P04I.mydomain.local:636";
    private static final String DOMAIN   = "mydomain.local";
    private static final String BASE_DN  = "DC=mydomain,DC=local";

    /* ================= AUTH ================= */
    public static DirContext getUserContext(String username, String password)
            throws NamingException {

        String userPrincipal = username + "@" + DOMAIN;

        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, LDAP_URL);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, userPrincipal);
        env.put(Context.SECURITY_CREDENTIALS, password);
        env.put(Context.SECURITY_PROTOCOL, "ssl");

        return new InitialDirContext(env);
    }

    /* ================= GET ALL USERS ================= */
    public static List<User> getAllUsers(String username, String password) {
        List<User> users = new ArrayList<>();
        DirContext ctx = null;

        try {
            ctx = getUserContext(username, password);

            SearchControls sc = new SearchControls();
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            sc.setReturningAttributes(new String[]{
                "sAMAccountName", "givenName", "sn", "cn", "mail"
            });

            NamingEnumeration<SearchResult> results = ctx.search(BASE_DN, "(objectClass=user)", sc);

            while (results.hasMore()) {
                Attributes a = results.next().getAttributes();
                users.add(mapUser(a));
            }

        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            close(ctx);
        }

        return users;
    }

    /* ================= GET USER BY USERNAME ================= */
    public static User getUserByUsername(String searchUsername,
                                         String bindUser,
                                         String bindPassword) {

        DirContext ctx = null;

        try {
            ctx = getUserContext(bindUser, bindPassword);

            String filter = "(sAMAccountName=" + escapeLDAP(searchUsername) + ")";

            SearchControls sc = new SearchControls();
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration<SearchResult> results = ctx.search(BASE_DN, filter, sc);

            if (results.hasMore()) {
                Attributes a = results.next().getAttributes();
                return mapUser(a);
            }

        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            close(ctx);
        }

        return null;
    }

    /* ================= UPDATE USER ================= */
    public static boolean updateUserAttributes(
            String username,
            String firstName,
            String lastName,
            String fullName,
            String email,
            String displayName,
            String initials,
            String description,
            String office,
            String telephone,
            String webpage,
            String bindUser,
            String bindPassword) {

        DirContext ctx = null;

        try {
            ctx = getUserContext(bindUser, bindPassword);

            String filter = "(sAMAccountName=" + escapeLDAP(username) + ")";
            SearchControls sc = new SearchControls();
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration<SearchResult> results = ctx.search(BASE_DN, filter, sc);

            if (!results.hasMore())
                return false;

            String dn = results.next().getNameInNamespace();

            List<ModificationItem> mods = new ArrayList<>();

            addReplace(mods, "givenName", firstName);
            addReplace(mods, "sn", lastName);
            addReplace(mods, "cn", fullName);
            addReplace(mods, "mail", email);
            addReplace(mods, "displayName", displayName);
            addReplace(mods, "initials", initials);
            addReplace(mods, "description", description);
            addReplace(mods, "physicalDeliveryOfficeName", office);
            addReplace(mods, "telephoneNumber", telephone);
            addReplace(mods, "wWWHomePage", webpage);

            if (!mods.isEmpty()) {
                ctx.modifyAttributes(dn, mods.toArray(new ModificationItem[0]));
            }

            return true;

        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            close(ctx);
        }

        return false;
    }

    /* ================= MAPPING ================= */
    private static User mapUser(Attributes a) throws NamingException {
        return new User(
                getAttr(a, "sAMAccountName"),
                getAttr(a, "givenName"),
                getAttr(a, "sn"),
                getAttr(a, "cn"),
                getAttr(a, "mail"),
                getAttr(a, "displayName"),
                getAttr(a, "initials"),
                getAttr(a, "description"),
                getAttr(a, "physicalDeliveryOfficeName"),
                getAttr(a, "telephoneNumber"),
                getAttr(a, "wWWHomePage")
        );
    }

    /* ================= HELPERS ================= */
    private static void addReplace(List<ModificationItem> mods,
                                   String attr,
                                   String value) {

        if (value != null) {
            if (value.trim().isEmpty()) {
                mods.add(new ModificationItem(
                        DirContext.REMOVE_ATTRIBUTE,
                        new BasicAttribute(attr)));
            } else {
                mods.add(new ModificationItem(
                        DirContext.REPLACE_ATTRIBUTE,
                        new BasicAttribute(attr, value)));
            }
        }
    }

    private static String getAttr(Attributes attrs, String name) throws NamingException {
        return attrs.get(name) != null ? attrs.get(name).get().toString() : "";
    }

    private static void close(DirContext ctx) {
        if (ctx != null) {
            try { ctx.close(); } catch (Exception ignored) {}
        }
    }

    private static String escapeLDAP(String value) {
        return value.replace("\\", "\\5c")
                    .replace("*", "\\2a")
                    .replace("(", "\\28")
                    .replace(")", "\\29")
                    .replace("\u0000", "\\00");
    }

    /* ================= AUTHENTICATE ================= */
    public static boolean authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return false;
        }

        DirContext ctx = null;
        try {
            ctx = getUserContext(username, password);
            return true; // valid credentials
        } catch (NamingException e) {
            return false; // invalid credentials
        } finally {
            close(ctx);
        }
    }
}