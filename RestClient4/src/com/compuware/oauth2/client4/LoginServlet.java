package com.compuware.oauth2.client4;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;

public class LoginServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private String defaultRedirectUrl;
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		defaultRedirectUrl = getInitParameter("defaultRedirectUrl");
		if (defaultRedirectUrl == null) {
			throw new ServletException("LoginServlet expected an initialization parameter named 'defaultRedirectUrl'");
		}

	}
	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestDispatcher dispatch = req.getServletContext().getNamedDispatcher("Login View");
		dispatch.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String redirectUrl = req.getParameter("goto");
		String userName = req.getParameter("userName");
		String password = req.getParameter("password");
		
		if (redirectUrl == null) {
			redirectUrl = defaultRedirectUrl;
		}
		
		Credentials credentials = new Credentials(userName, password);
		boolean successfulLogin = false;
		if (userName != null && password != null) {
			try {
				String accessToken = AccessTokenManager.retrieveAccessToken(credentials);
				HttpSession session = req.getSession(true);
				session.setAttribute("access_token", accessToken);
				session.setAttribute("credentials", credentials);
				successfulLogin = true;
			} catch (OAuthSystemException e) {
				throw new ServletException(e);
			} catch (OAuthProblemException e) {
				// Login failed!
				e.printStackTrace();
			}
		}
		
		if (successfulLogin) {
			resp.sendRedirect(redirectUrl);
		} else {
			req.setAttribute("credentials", credentials);
			req.setAttribute("error", "Invalid username and/or password");
			RequestDispatcher dispatch = req.getServletContext().getNamedDispatcher("Login View");
			dispatch.forward(req, resp);
			
		}
	}

}
