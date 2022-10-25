import java.io.IOException;

import jakarta.servlet.*;

public class Config extends GenericServlet
{
    private ServletConfig config;
    private ServletContext context;

    @Override
    public void init(ServletConfig config)
    {
        this.config = config;
        this.context = config.getServletContext();
    }

    @Override
    public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException
    {
        resp.getWriter().write("Username: " + this.config.getInitParameter("username") + "\nPassword: " + this.config.getInitParameter("password"));
        resp.getWriter().write("\nDefined by: " + this.context.getInitParameter("user"));
    }
}