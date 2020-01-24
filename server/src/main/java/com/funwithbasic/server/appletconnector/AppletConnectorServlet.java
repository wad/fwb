package com.funwithbasic.server.appletconnector;

import com.funwithbasic.server.MyServletParent;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

import static com.funwithbasic.Constants.*;

public class AppletConnectorServlet extends MyServletParent {

    // This is called when the user clicks the RUN button
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userAction = request.getParameter(PARAM_NAME_USER_ACTION);

        SessionStatus sessionStatus = SessionStatus.determineFromString(request.getParameter(SESSION_PARAM_NAME_SESSION_STATUS));

        if (USER_ACTION_RUN_PROGRAM.equals(userAction)) {
            String fileContent = request.getParameter(PARAM_NAME_FILE_CONTENT);
            HttpSession session = request.getSession();
            session.setAttribute(SESSION_PARAM_NAME_PROGRAM, fileContent);
            session.setAttribute(SESSION_PARAM_NAME_SESSION_STATUS, SessionStatus.userRequestedRunProgram);
            String messageForUser;
            switch (sessionStatus) {
                case appletRunningProgram:
                    messageForUser = "Applet already running, saved signal to run this new program";
                    break;
                case appletWaiting:
                case userRequestedRunProgram:
                case unknown:
                default:
                    messageForUser = "Applet waiting, saved signal to run program (" + sessionStatus.getParamValue() + ")";
            }
            session.setAttribute(SESSION_PARAM_NAME_MESSAGE_FROM_SERVER, messageForUser);
            String url = "/applet_connector.jsp";
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(url);
            requestDispatcher.forward(request, response);
        } else {
            StringBuilder html = new StringBuilder();
            handleError(html, "Unknown action: " + userAction);
            wrapItUp(response, html);
        }
    }

    // This is called by the applet periodically when it's idle, to get a new program to run.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userAction = request.getParameter(PARAM_NAME_USER_ACTION);
        String fileContent = "";
        if (USER_ACTION_GET_PROGRAM.equals(userAction)) {
            String programFromSession = (String) session.getAttribute(SESSION_PARAM_NAME_PROGRAM);
            if (programFromSession != null) {
                fileContent = programFromSession;
            }
        }
        response.setContentType(DOC_TYPE_TEXT);
        PrintWriter out = response.getWriter();
        out.println(fileContent);
        out.close();

        // clear the program out of the session, so it won't keep triggering a load on the applet side
        session.setAttribute(SESSION_PARAM_NAME_PROGRAM, "");
    }

}
