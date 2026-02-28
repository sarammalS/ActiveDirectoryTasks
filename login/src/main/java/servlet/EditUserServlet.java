package servlet;

import util.LdapUtil;
import Model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/EditUserServlet")
public class EditUserServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp"); return;
        }

        String loggedInUser = (String) session.getAttribute("user");
        String bindPass     = (String) session.getAttribute("userPassword");

        User user = LdapUtil.getUserByUsername(loggedInUser, loggedInUser, bindPass);
        request.setAttribute("userAttrs", user);
        request.getRequestDispatcher("/EditUser.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp"); return;
        }

        String loggedInUser = (String) session.getAttribute("user");
        String bindPass     = (String) session.getAttribute("userPassword");

        boolean updated = LdapUtil.updateUser(
            loggedInUser,
            request.getParameter("firstName"),
            request.getParameter("lastName"),
            request.getParameter("displayName"),
            request.getParameter("initials"),
            request.getParameter("description"),
            request.getParameter("office"),
            request.getParameter("telephone"),
            request.getParameter("email"),
            request.getParameter("webpage"),
            loggedInUser, bindPass
        );

        if (updated) {
            response.sendRedirect("ListUsersServlet");
        } else {
            request.setAttribute("error", "Failed to update.");
            request.getRequestDispatcher("/EditUser.jsp").forward(request, response);
        }
    }
}