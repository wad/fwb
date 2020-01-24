<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>FloppyTool</title>
</head>
<body>
<h1>FloppyTool</h1>

<p>The FunWithBasic application lets users store their data on "floppy disks". Of course, they aren't really floppy
    disks, just a directory on the server, one per user (the name of the directory is generated from
    the user's id). That directory is called a "floppy".</p>
<hr>

<form action="floppy" method="post">
    <p><label>Enter userId: <input type="text" name="userId"></label></p>

    <p><label>FileName:<br><input type="text" name="fileName"></label></p>

    <p><label>File content:<br><textarea name="fileContent" rows="10" cols="80"></textarea></label></p>

    <p>Choose what change you want to make:</p>

    <p>
        <label><input type="radio" name="actionToPerform" value="create_floppy"> Create floppy</label><br>
        <label><input type="radio" name="actionToPerform" value="delete_floppy"> Delete floppy</label><br>
        <label><input type="radio" name="actionToPerform" value="create_file"> Create file</label><br>
        <label><input type="radio" name="actionToPerform" value="update_file"> Update file</label><br>
        <label><input type="radio" name="actionToPerform" value="delete_file"> Delete file</label><br>
    </p>

    <p><label><input type="submit" value="Make the change"></label></p>
</form>

<hr>

<form action="floppy" method="get">
    <p><label>Enter userId: <input type="text" name="userId"></label></p>

    <p><label>FileName:<br><input type="text" name="fileName"></label></p>

    <p>What do you want to see?</p>

    <p>
        <label><input type="radio" name="actionToPerform" value="list_files"> List files</label><br>
        <label><input type="radio" name="actionToPerform" value="read_file"> Read file</label><br>
        <label><input type="radio" name="actionToPerform" value="list_floppies"> List floppies</label><br>
    </p>

    <p><input type="submit" value="Retrieve information"></p>
</form>

</body>
</html>
