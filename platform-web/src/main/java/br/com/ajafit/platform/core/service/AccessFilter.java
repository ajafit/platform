package br.com.ajafit.platform.core.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

@WebFilter("/platform/service/*")
public class AccessFilter implements Filter {

	Logger log = Logger.getLogger(AccessFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

		log.info("init em filter");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		log.info("doing filter");
		HttpServletRequest req = (HttpServletRequest) request;
		if (req.getMethod().equals("OPTIONS")) {
			log.info("options..");
			return;
		}
		try {
			String token = getToken(req);
			log.info("token: " + token);
			chain.doFilter(request, response);
		} catch (NoSuchElementException e) {
			log.info("nao tem cookie, redireciona para login");
			HttpServletResponse resp = (HttpServletResponse) response;
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.getWriter().write("not logged..");
			resp.flushBuffer();

		}

	}

	private String getToken(HttpServletRequest request) {
		String token = request.getHeader("authorization");
		if (token == null) {

			if (request.getCookies() == null) {
				throw new NoSuchElementException();
			}
			Cookie c = Arrays.asList(request.getCookies()).stream()
					.filter((Cookie cc) -> cc.getName().equals("authorization")).findAny().get();
			token = c.getValue();

		}
		return token;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
