import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; 
import jakarta.servlet.ServletException;

@WebServlet("/login")
public class Login extends HttpServlet
{
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        req.getSession(false).setAttribute("ip", String.valueOf(req.getRemoteAddr()));

        if(req.getCookies() != null && req.getCookies().length > 1 && req.getCookies()[1].getValue().equals("allow"))
        {
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
        else
        {
            if(req.getSession().getAttribute("username") != null)
            {
                req.getRequestDispatcher("welcome.jsp").include(req, resp);
            }
            req.getRequestDispatcher("login.html").include(req, resp);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String username = String.valueOf(req.getParameter("username"));
        String password = String.valueOf(req.getParameter("password"));

        if(!(username.equals("")) && password.equals("admin"))
        {
            Cookie cookie = new Cookie("auth","allow");
            cookie.setMaxAge(120);
            cookie.setPath("/");
            resp.addCookie(cookie);
            req.getSession().setAttribute("username", username);
            resp.sendRedirect("/project");
        }
        else
        {
            req.getRequestDispatcher("incorrect.html").include(req, resp);
            req.getRequestDispatcher("login.html").include(req, resp);
        }
    }
}