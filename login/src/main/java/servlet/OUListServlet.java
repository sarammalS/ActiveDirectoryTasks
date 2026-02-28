package servlet;

import Model.OUModel;
import util.LdapUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/OUListServlet")
public class OUListServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp"); return;
        }

        String bindUser = (String) session.getAttribute("user");
        String bindPass = (String) session.getAttribute("userPassword");
        if (bindUser == null || bindPass == null) {
            response.sendRedirect("login.jsp"); return;
        }

        List<OUModel> ouList = LdapUtil.getOUList(bindUser, bindPass);
        request.setAttribute("ouList", ouList);
        request.getRequestDispatcher("CreateUser.jsp").forward(request, response);
    }
}