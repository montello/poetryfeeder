package com.fraise.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fraise.control.PoetryFeederManager;
import com.fraise.domain.Language;
import com.fraise.domain.Paging;
import com.fraise.domain.PoetryTweet;
import com.fraise.domain.SortOrder;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class PoetryfeederServlet extends HttpServlet {

    private ApplicationContext applicationContext = null;
    private PoetryFeederManager poetryFeederManager;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (applicationContext == null) {
            System.out.println("setting context in get");
            applicationContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
        }
        if (applicationContext != null && applicationContext.containsBean("poetryFeederManager")) {
            poetryFeederManager = (PoetryFeederManager) applicationContext.getBean("poetryFeederManager");
        } else {
            throw new RuntimeException("ApplicationContext is null or PoetryFeederManager bean is unavailable");
        }

        /** TODO clean this out. **/
        poetryFeederManager.retrieveAndPersistNewPoetryLines();

        UserService userService = UserServiceFactory.getUserService();

        String thisURL = req.getRequestURI();
        if (req.getUserPrincipal() != null) {
            resp.getWriter().println(
                    "<p>Hello, " + req.getUserPrincipal().getName() + "!  You can <a href=\""
                            + userService.createLogoutURL(thisURL) + "\">sign out</a>.</p>");

            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("<html>");
            out.println("<head><title>Done</title></head>");
            out.println("<body>");
            out.println("<form method=\"POST\" action=\"/poetryfeeder\">");
            out.println("<p>Author<input type=\"radio\" name=\"option\" value=\"author\">");
            // out.println("<input type=\"text\" name=\"author\" value=\"\"></p>");
            out.println("<p>Alphabetically<input type=\"radio\" name=\"option\" value=\"alphabetical\">");
            // out.println("<input type=\"text\" name=\"letter\" value=\"\"></p>");
            out.println("<p>Chronological<input type=\"radio\" name=\"option\" value=\"chronological\">");
            out.println("<p><input type=\"submit\" value=\"Filter\" /></p>");
            out.println("</form>");
            List<PoetryTweet> lines = poetryFeederManager.getAllLinesOfPoetry();
            renderPoetry(out, lines);

        } else {
            resp.getWriter().println(
                    "<p>Please <a href=\"" + userService.createLoginURL(thisURL) + "\">sign in</a>.</p>");
        }

    }

    private void renderPoetry(PrintWriter out, List<PoetryTweet> lines) {
        out.println("<p>Poetry Tweets:</p>");

        out.println("<table>");
        out.println("<tr><td><b>tweet</b></td><td><b>author</b></td><td><b>date</b></td>");
        out.println("</tr>");
        for (PoetryTweet line : lines) {
            out.println("<tr>");
            out.println("<td>" + line.getText() + "</td><td>" + line.getAuthor() + "</td><td>"
                    + line.getAuthoredOn().toString() + "</td>");
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sortBy = req.getParameter("option");
        int poemSize = 14;
        Paging paging = new Paging(poemSize);

        List<PoetryTweet> lines = null;
        if (sortBy.equals("author")) {
            lines = poetryFeederManager.getRecentSortedBy(SortOrder.AUTHOR, paging, Language.EN);
        } else if (sortBy.equals("alphabetical")) {
            lines = poetryFeederManager.getRecentSortedBy(SortOrder.ALPHABETICAL, paging, Language.EN);
        } else if (sortBy.equals("chronological")) {
            lines = poetryFeederManager.getRecentSortedBy(SortOrder.CHRONOLOGICAL, paging, Language.EN);
        }

        PrintWriter out = resp.getWriter();
        renderPoetry(out, lines);
    }
}
