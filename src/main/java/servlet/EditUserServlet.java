package servlet;

import util.LdapUtil;
import Model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/EditUserServlet")
public class EditUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null ||
            session.getAttribute("user") == null ||
            session.getAttribute("userPassword") == null) {

            response.sendRedirect("login.jsp");
            return;
        }

        String bindUser = (String) session.getAttribute("user");
        String bindPass = (String) session.getAttribute("userPassword");

        String username = request.getParameter("username");

        if (username == null || username.trim().isEmpty()) {
            response.sendRedirect("ListUsersServlet");
            return;
        }

        User user = LdapUtil.getUserByUsername(username, bindUser, bindPass);

        if (user == null) {
            response.sendRedirect("ListUsersServlet");
            return;
        }

        request.setAttribute("userAttrs", user);
        request.getRequestDispatcher("/EditUser.jsp")
               .forward(request, response);
    }

    // =============================
    // UPDATE USER (POST)
    // =============================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null ||
            session.getAttribute("user") == null ||
            session.getAttribute("userPassword") == null) {

            response.sendRedirect("login.jsp");
            return;
        }

        String bindUser = (String) session.getAttribute("user");
        String bindPass = (String) session.getAttribute("userPassword");

        String username    = request.getParameter("username");
        String firstName   = request.getParameter("firstName");
        String lastName    = request.getParameter("lastName");
        String displayName = request.getParameter("displayName");
        String initials    = request.getParameter("initials");
        String description = request.getParameter("description");
        String office      = request.getParameter("office");
        String telephone   = request.getParameter("telephone");
        String email       = request.getParameter("email");
        String webpage     = request.getParameter("webpage");

        if (username == null || username.trim().isEmpty()) {
            response.sendRedirect("ListUsersServlet");
            return;
        }

        boolean updated = LdapUtil.updateUserAttributes(
                username,
                firstName,
                lastName,
                displayName,
                initials,
                description,
                office,
                telephone,
                email,
                webpage,
                bindUser,
                bindPass
        );

        if (updated) {
            response.sendRedirect("ListUsersServlet");
        } else {
            request.setAttribute("error", "Failed to update user");
            doGet(request, response);
        }
    }
}