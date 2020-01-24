package com.funwithbasic.server.owner;

import com.funwithbasic.server.MyServletParent;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "owner", value = "/owner")
public class OwnerServlet extends MyServletParent {

    static final String ACTION_INSTALL = "action_install";
    static final String ACTION_UNINSTALL = "action_uninstall";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ownerAction = request.getParameter("ownerAction");
        String ownerPassword = request.getParameter("ownerPassword");

        StringBuilder html = new StringBuilder();

        Connection connection = null;
        try {
            connection = OwnerService.getConnection();
            if (ACTION_INSTALL.equals(ownerAction)) {
                String resultMessage = OwnerService.install(connection, ownerPassword);
                printHeader(html, "Result of " + ownerAction);
                html.append("<h1>Here is the result of ").append(ownerAction).append(":</h1>");
                html.append("<pre>").append(resultMessage).append("</pre>");
                printFooter(html);
            } else if (ACTION_UNINSTALL.equals(ownerAction)) {
                String resultMessage = OwnerService.uninstall(connection, ownerPassword);
                printHeader(html, "Result of " + ownerAction);
                html.append("<h1>Here is the result of ").append(ownerAction).append(":</h1>");
                html.append("<pre>").append(resultMessage).append("</pre>");
                printFooter(html);
            } else {
                handleError(html, "unknown action: " + ownerAction);
            }
        } catch (SQLException e) {
            handleError(html, e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    // do nothing
                }
            }
        }

        wrapItUp(response, html);
    }

}
