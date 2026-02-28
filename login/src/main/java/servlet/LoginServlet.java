package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import util.LdapUtil;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, res);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (isEmpty(username) || isEmpty(password)) {
            req.setAttribute("errorMessage", "Username and password are required.");
            req.getRequestDispatcher("/login.jsp").forward(req, res);
            return;
        }
        

        if (LdapUtil.authenticate(username, password)) {
            HttpSession session = req.getSession();
            session.setAttribute("user", username);
            session.setAttribute("userPassword", password);
            res.sendRedirect(req.getContextPath() + "/welcome.jsp");
        } else {
            req.setAttribute("errorMessage", "Invalid credentials.");
            req.getRequestDispatcher("/login.jsp").forward(req, res);
        }
    }

    private boolean isEmpty(String s) { return s == null || s.trim().isEmpty(); }
}