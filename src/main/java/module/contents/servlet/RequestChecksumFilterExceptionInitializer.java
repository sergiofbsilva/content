package module.contents.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter.ChecksumPredicate;

@WebListener
public class RequestChecksumFilterExceptionInitializer implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        RequestChecksumFilter.registerFilterRule(new ChecksumPredicate() {

            @Override
            public boolean shouldFilter(HttpServletRequest request) {
                final String requestURI = request.getRequestURI();
                final String queryString = request.getQueryString();
                return !(requestURI.contains("pageVersioning.do") && queryString.startsWith("method=viewPage"));
            }
        });
    }
}
