package com.funwithbasic.server.floppy;

import com.funwithbasic.server.MyServletParent;
import com.funwithbasic.server.tool.ValidationException;
import com.funwithbasic.server.tool.ValidationTool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "floppy", value = "/floppy", loadOnStartup = 1)
public class FloppyServlet extends MyServletParent {

    // get
    static final String ACTION_LIST_FILES = "list_files";
    static final String ACTION_READ_FILE = "read_file";
    static final String ACTION_LIST_FLOPPIES = "list_floppies";

    // post
    static final String ACTION_CREATE_FLOPPY = "create_floppy";
    static final String ACTION_DELETE_FLOPPY = "delete_floppy";
    static final String ACTION_CREATE_FILE = "create_file";
    static final String ACTION_UPDATE_FILE = "update_file";
    static final String ACTION_DELETE_FILE = "delete_file";

    int getUserIdFromString(String userIdAsString) throws ValidationException {
        if (userIdAsString == null || userIdAsString.length() == 0) {
            throw new ValidationException("Need to supply a userId");
        }
        int userId;
        try {
            userId = Integer.parseInt(userIdAsString);
            if (!ValidationTool.isUserIdValid(userId)) {
                throw new ValidationException("User ID is out of range. It was " + userId);
            }
            return userId;
        } catch (NumberFormatException e) {
            throw new ValidationException("User ID is invalid. Should be a number. It was this: [" + userIdAsString + "]");
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdString = request.getParameter("userId");
        String fileName = request.getParameter("fileName");
        String actionToPerform = request.getParameter("actionToPerform");

        StringBuilder html = new StringBuilder();

        try {
            if (ACTION_LIST_FILES.equals(actionToPerform)) {
                int userId = getUserIdFromString(userIdString);
                List<String> fileNamesRead = FloppyService.listFiles(userId);
                printHeader(html, "Result of " + actionToPerform);
                html.append("<h1>Here are the files:</h1>");
                html.append("<ul>");
                for (String fileNameRead : fileNamesRead) {
                    html.append("<li>").append(fileNameRead).append("</li>");
                }
                html.append("</ul>");
                printFooter(html);
            } else if (ACTION_READ_FILE.equals(actionToPerform)) {
                int userId = getUserIdFromString(userIdString);
                String fileContentRead = FloppyService.readFile(userId, fileName);
                printHeader(html, "Result of " + actionToPerform);
                html.append("<h1>Contents of ").append(fileName).append(":</h1>");
                html.append("<pre>");
                html.append(fileContentRead);
                html.append("</pre>");
                printFooter(html);
            } else if (ACTION_LIST_FLOPPIES.equals(actionToPerform)) {
                List<String> floppiesRead = FloppyService.listFloppies();
                printHeader(html, "Result of " + actionToPerform);
                html.append("<h1>Here are the floppies:</h1>");
                html.append("<ul>");
                for (String floppyNameRead : floppiesRead) {
                    html.append("<li>").append(floppyNameRead).append("</li>");
                }
                html.append("</ul>");
                printFooter(html);
            } else {
                handleError(html, "Unknown action: " + actionToPerform);
            }
        } catch (FloppyException e) {
            handleError(html, e);
        } catch (ValidationException e) {
            handleError(html, e);
        }

        wrapItUp(response, html);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdString = request.getParameter("userId");
        String fileName = request.getParameter("fileName");
        String fileContent = request.getParameter("fileContent");
        String actionToPerform = request.getParameter("actionToPerform");

        StringBuilder html = new StringBuilder();

        try {
            int userId = getUserIdFromString(userIdString);

            if (ACTION_CREATE_FLOPPY.equals(actionToPerform)) {
                FloppyService.createFloppy(userId);
                printHeader(html, "Result of " + actionToPerform);
                html.append("<h1>Floppy created for user ").append(userId).append(".</h1>");
                printFooter(html);
            } else if (ACTION_DELETE_FLOPPY.equals(actionToPerform)) {
                FloppyService.deleteFloppy(userId);
                printHeader(html, "Result of " + actionToPerform);
                html.append("<h1>Floppy deleted for user ").append(userId).append(".</h1>");
                printFooter(html);
            } else if (ACTION_CREATE_FILE.equals(actionToPerform)) {
                FloppyService.createFile(userId, fileName, fileContent);
                printHeader(html, "Result of " + actionToPerform);
                html.append("<h1>File <b>").append(fileName).append("</b> created for user ").append(userId).append(".</h1>");
                printFooter(html);
            } else if (ACTION_UPDATE_FILE.equals(actionToPerform)) {
                FloppyService.updateFile(userId, fileName, fileContent);
                printHeader(html, "Result of " + actionToPerform);
                html.append("<h1>File <b>").append(fileName).append("</b> updated for user ").append(userId).append(".</h1>");
                printFooter(html);
            } else if (ACTION_DELETE_FILE.equals(actionToPerform)) {
                FloppyService.deleteFile(userId, fileName);
                printHeader(html, "Result of " + actionToPerform);
                html.append("<h1>File <b>").append(fileName).append("</b> deleted for user ").append(userId).append(".</h1>");
                printFooter(html);
            } else {
                handleError(html, "Unknown action: " + actionToPerform);
            }
        } catch (FloppyException e) {
            handleError(html, e);
        } catch (ValidationException e) {
            handleError(html, e);
        }

        wrapItUp(response, html);
    }

}
