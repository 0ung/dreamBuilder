package codehows.dream.dreambulider.config;

import codehows.dream.dreambulider.service.VisitedService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/*")
public class VisitedFilter implements Filter {

    @Autowired
    private VisitedService visitedService;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
            String ipAddress = httpRequest.getRemoteAddr();
            System.out.println("Visitor IP: " + ipAddress);

            // DB에 IP에 저장하기
            visitedService.saveVisit(ipAddress);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
