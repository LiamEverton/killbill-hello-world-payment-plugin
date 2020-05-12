package org.ambit;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jooby.mvc.GET;
import org.jooby.mvc.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Singleton
@Path("/")
public class HelloWorldServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldServlet.class);

    public HelloWorldServlet() {
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Hello World");
    }
}