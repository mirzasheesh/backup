<html>
    <body>
        <% String uname = (String) request.getSession(false).getAttribute("username"); %>
        <h4>Welcome back <% out.print(uname); %></h4>
    </body>
</html>