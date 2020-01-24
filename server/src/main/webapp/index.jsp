<%@ page import="com.funwithbasic.Constants" %>
<%@ page import="com.funwithbasic.SharedConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Fun With Basic</title>
    <link rel="shortcut icon" href="images/favicon.ico">
</head>

<body text="#C0C0C0" bgcolor="#000000" link="#00CCE0" vlink="#0088E0" alink="#FF0000">

<div style="text-align: center;">
    <img src="images/logo_600x70.png" alt="logo"><br>*BETA*
</div>

<div style="text-align: center;">
    <iframe name="applet_connection"
            src="applet_connector.jsp"
            frameborder="0"
            scrolling="auto"
            width="800"
            height="300"
            marginwidth="0"
            marginheight="0"></iframe>
    <br>

    <table align="center">
        <tr>
            <td align="left">Your program's output text shows up here:</td>
        </tr>
        <tr>
            <td>
                <object type="application/x-java-applet" width="<%= Constants.APPLET_WIDTH %>" height="<%= Constants.APPLET_HEIGHT %>">
                    <param name="archive" value="/<%= Constants.APPLICATION_CONTEXT_PATH %>runner.jar"/>
                    <param name="code" value="com.funwithbasic.runner.FWBRunner"/>
                    <param name="<%= SharedConstants.SESSION_ID_PARAMETER_NAME %>" value="<%= session.getId() %>" />
                    <table align="center" border=10>
                        <tr align="center">
                            <td align="center">
                                <b>Whoops!</b>
                                You need the Java plugin, but it seems to be missing.<br>
                                You can get it at <a href="http://java.com">java.com</a>.
                            </td>
                        </tr>
                    </table>
                </object>
            </td>
        </tr>
    </table>
</div>

<div style="text-align: right;">
    <span style="font-size: 70%; ">
        Version: <%= SharedConstants.VERSION %><br>
        Copyright © 2013, <a href="http://wadhome.org/~wad" target="_blank">Eric Wadsworth</a>
    </span>
</div>

</body>
</html>
