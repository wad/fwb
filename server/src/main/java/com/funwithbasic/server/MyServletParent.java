package com.funwithbasic.server;

import com.funwithbasic.Constants;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class MyServletParent extends HttpServlet {

    protected void handleError(StringBuilder html, Throwable throwable) {
        handleError(html, throwable.getMessage());
    }

    protected void handleError(StringBuilder html, String message) {
        printHeader(html, "Got an error");
        html.append("Got this error: ").append(message);
        printFooter(html);
    }

    protected void printHeader(StringBuilder html, String title) {
        html.append("<html><head><title>").append(title).append("</title></head><body>");
    }

    protected void printFooter(StringBuilder html) {
        html.append("</body></html>");
    }

    protected void wrapItUp(HttpServletResponse response, StringBuilder html) throws IOException {
        response.setContentType(Constants.DOC_TYPE_HTML);
        PrintWriter out = response.getWriter();
        out.println(html.toString());
        out.close();
    }

}
