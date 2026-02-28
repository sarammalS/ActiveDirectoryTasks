package servlet;
import util.LdapUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import Model.User;
import java.io.IOException;
import java.util.List;

@WebServlet("/ListUsersServlet")
public class ListUsersServlet extends HttpServlet {
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

        try {
            List<User> users = LdapUtil.getAllUsers(bindUser, bindPass);
            request.setAttribute("users", users);
            request.setAttribute("user", bindUser);
            request.getRequestDispatcher("/listUsers.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to retrieve users.");
            request.getRequestDispatcher("/listUsers.jsp").forward(request, response); // add this
        }
    }
}