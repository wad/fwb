<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.funwithbasic.Constants" %>
<%@ page import="com.funwithbasic.SharedConstants" %>
<html>
    <body text="#C0C0C0" bgcolor="#000000" link="#00CCE0" vlink="#0088E0" alink="#FF0000">
        <%
            String fileContent = request.getParameter(Constants.PARAM_NAME_FILE_CONTENT);
            if (fileContent == null) {
                fileContent = SharedConstants.INITIAL_PROGRAM;
            }
        %>
        <form action="<%= response.encodeURL("appletConnector") %>" method="post">
            <div>
                <label>
                    Type some code here, then click <b>Submit</b>, then click <b>Run New</b>.
                    The <a href="http://funwithbasic.com/wiki" target="_blank">wiki</a> has example programs!
                    <textarea name="<%= Constants.PARAM_NAME_FILE_CONTENT %>" rows="14" cols="95"><%= fileContent %>
                    </textarea>
                </label>
            </div>
            <input type="hidden" name="<%= Constants.PARAM_NAME_USER_ACTION %>" value="<%= Constants.USER_ACTION_RUN_PROGRAM %>">
            <div>
                <label>
                    <input type="submit" value="Submit">
                </label>
            </div>
        </form>
    </body>
</html>
