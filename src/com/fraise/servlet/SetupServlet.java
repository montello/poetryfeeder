package com.fraise.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fraise.persistence.PMF;
import com.fraise.source.twitter.AccountAccessToken;
import com.fraise.source.twitter.ApplicationToken;

@SuppressWarnings("serial")
public class SetupServlet extends HttpServlet {

    private PersistenceManager pm;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<html>");
        out.println("<head><title>Setup PoetryFeeder</title></head>");
        out.println("<body>");
        out.println("<form method=\"POST\" action=\"setup\">");
        out.println("<p>applicationName<input type=\"text\" name=\"applicationName\" value=\"poetryfeeder\"></p>");
        out.println("<p>applicationToken<input type=\"text\" name=\"applicationToken\"></p>");
        out.println("<p>applicationTokenSecret<input type=\"text\" name=\"applicationTokenSecret\">");
        out.println("<br/>");
        out.println("<p>accountName<input type=\"text\" name=\"accountName\" value=\"poetryfeeder\"></p>");
        out.println("<p>accountToken<input type=\"text\" name=\"accountToken\"></p>");
        out.println("<p>accountTokenSecret<input type=\"text\" name=\"accountTokenSecret\">");
        out.println("<input type=\"submit\" value=\"Submit\" />");
        out.println("</form>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        PersistenceManagerFactory pmf = PMF.get();
        pm = pmf.getPersistenceManager();

        String applicationName = req.getParameter("applicationName");
        String applicationToken = req.getParameter("applicationToken");
        String applicationTokenSecret = req.getParameter("applicationTokenSecret");

        String accountName = req.getParameter("accountName");
        String accountToken = req.getParameter("accountToken");
        String accountTokenSecret = req.getParameter("accountTokenSecret");

        if (!applicationExists(applicationName)) {
            ApplicationToken appToken = new ApplicationToken(applicationName, applicationToken, applicationTokenSecret);
            pm.makePersistent(appToken);
        }

        if (!accountExists(accountName)) {
            AccountAccessToken accountAccessToken = new AccountAccessToken(accountName, accountToken,
                    accountTokenSecret);
            pm.makePersistent(accountAccessToken);
        }

        PrintWriter out = resp.getWriter();

        out.println("<html>");
        out.println("<head><title>Done</title></head>");
        out.println("<body>");
        out.println("<p>Application/Account setup complete</p>");
        out.println("</body></html>");
    }

    private boolean accountExists(String accountName) {
        AccountAccessToken account = null;
        try {
            account = pm.getObjectById(AccountAccessToken.class, accountName);
        } catch (Exception e) {
            //
        }
        return account != null;
    }

    private boolean applicationExists(String applicationName) {
        ApplicationToken application = null;
        try {
            application = pm.getObjectById(ApplicationToken.class, applicationName);
        } catch (Exception e) {
            //
        }
        return application != null;
    }
}
