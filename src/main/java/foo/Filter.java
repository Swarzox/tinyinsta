package foo;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebFilter(urlPatterns={"/upload", "/followUser"}) 
public class Filter implements javax.servlet.Filter {
	
	boolean hasSession(HttpSession session) {
		Object sessionId = session.getAttribute("id");
		if(sessionId == null) {
			return false;
		}
		return true;
	}

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession();

        if (hasSession(session)) {
			chain.doFilter(req, res);
        } else {
			System.out.println("Accès API non authentifié");
        }
    }

    @Override
    public void destroy() {
    }

}