<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>FunWithBasic Owner Tool</title>
</head>
<body>
<h1>FunWithBasic Owner Tool</h1>

<h2>This will destroy the entire application, along with all user data!</h2>

<form action="owner" method="post">
    <p><label>Enter owner password: <input type="password" name="ownerPassword"></label></p>
    <label><input type="radio" name="ownerAction" value="action_install"> Install FunWithBasic</label>
    <br>
    <label><input type="radio" name="ownerAction" value="action_uninstall"> Uninstall FunWithBasic</label>

    <p><label><input type="submit" value="Perform Owner Action"></label></p>
</form>

</body>
</html>
