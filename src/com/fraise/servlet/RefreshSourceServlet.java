package com.fraise.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fraise.control.PoetryFeederManager;

@SuppressWarnings("serial")
public class RefreshSourceServlet extends HttpServlet {

    private ApplicationContext applicationContext = null;
    private PoetryFeederManager poetryFeederManager;

    public void setPoetryFeederManager(PoetryFeederManager poetryFeederManager) {
        this.poetryFeederManager = poetryFeederManager;
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (applicationContext == null) {
            System.out.println("setting context in get");
            applicationContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
        }
        if (applicationContext != null && applicationContext.containsBean("poetryFeederManager")) {
            poetryFeederManager = (PoetryFeederManager) applicationContext.getBean("poetryFeederManager");
        } else {
            throw new RuntimeException("ApplicationContext is null or PoetryFeederManager bean is unavailable");
        }

        poetryFeederManager.retrieveAndPersistNewPoetryLines();
    }

}
