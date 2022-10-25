<!DOCTYPE html>
<html lang="en">
<head>
    <%
        if(request.getCookies() != null && request.getCookies().length > 1 && request.getCookies()[1].getValue().equals("allow")){
        }else{
            response.sendRedirect("/project/login");
        }
        String uname = (String) request.getSession(false).getAttribute("username");
    %>
    <title>Hello World!</title>
    <script>
        const logout = () => {
            window.cookieStore.delete("auth");
            window.location.href = "/project/login";
        }
    </script>
</head>
<body>
    <center>
        <h2>Request dispatching and managing session with Java Servlet</h2>
        <h4>Made by Sheesh Mirza</h4>
        </br>
        <% out.println("<h4>Hello " + uname + "!</h4>"); %>
        <button onclick="logout()">Logout</button>
        </br>
        <%
            String yourIP = (String) request.getSession(false).getAttribute("ip");
            out.println("<p> Your IP Address: " + yourIP  + "</p>");
        %>
    </center>
</body>
</html>