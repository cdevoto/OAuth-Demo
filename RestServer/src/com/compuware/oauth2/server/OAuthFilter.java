package com.compuware.oauth2.server;

import java.io.IOException;
import java.net.URLEncoder;

import javax.net.ssl.SSLContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

public class OAuthFilter implements Filter {

	
	/**
	 * An LRU cache used to store access token data retrieved from the tokenInfoEndpoint.
	 */
	private static ConcurrentLinkedHashMap<String, AccessToken> tokenCache = new ConcurrentLinkedHashMap.Builder<String, AccessToken>()
			.maximumWeightedCapacity(1000)
			.build();

	private FilterConfig config;
	private String tokenInfoEndpoint;
	private ClientConnectionManager connectionManager;

	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String authorizationHeader = request.getHeader("Authorization");
		
		String accessTokenParam = null;
		if (authorizationHeader != null) {
			int fieldDelimiter = authorizationHeader.indexOf(' ');
			if (fieldDelimiter > 1) {
				String authType = authorizationHeader.substring(0, fieldDelimiter);
				if ("Bearer".equals(authType)) {
				    accessTokenParam = authorizationHeader.substring(fieldDelimiter + 1);
				}
			}
		}
		if (accessTokenParam == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing parameter: access_token");
			// Alternatively, we can send a JSON formatted error response.
		}
		
		AccessToken accessToken = tokenCache.get(accessTokenParam);
		try {
			if (accessToken == null) {
				// Retrieve the access token info from the tokenInfoEndpoint
				HttpClient httpClient = new DefaultHttpClient(this.connectionManager);
				HttpGet httpGet = new HttpGet(this.tokenInfoEndpoint + "?access_token=" + URLEncoder.encode(accessTokenParam, "UTF-8").replace("+", "%20"));
				HttpResponse tokenInfoResponse = httpClient.execute(httpGet);
				HttpEntity entity = tokenInfoResponse.getEntity();
				if (entity != null) {
					String tokenInfo = EntityUtils.toString(entity);
					JSONObject tokenObject = new JSONObject(tokenInfo);
					String clientId = null;
					if (tokenObject.has("client_id")) {
					    clientId = tokenObject.getString("client_id");
					}
					String uid = null;
					String firstName = null;
					String lastName = null;
					String fullName = null;
					String emailAddress = null;
					Long accountId = null;
					
					if (tokenObject.has("uid")) {
						uid = tokenObject.getString("uid");
						firstName = tokenObject.getString("givenName");
						lastName = tokenObject.getString("sn");
						fullName = tokenObject.getString("cn");
						emailAddress = tokenObject.getString("mail");
						if (tokenObject.has("accountId")) {
							try {
							    accountId = tokenObject.getLong("accountId");
							} catch (JSONException ex) {
								// OK to ignore this
							}
						}
					}
					boolean read = false;
					if (tokenObject.has("read")) {
						read = tokenObject.getBoolean("read");
					}
					boolean write = false;
					if (tokenObject.has("write")) {
						write = tokenObject.getBoolean("write");
					}
					
					UserDetails userDetails = null;
					if (uid != null) {
						userDetails = UserDetails.builder(uid)
										.setFirstName(firstName)
										.setLastName(lastName)
										.setFullName(fullName)
										.setEmailAddress(emailAddress)
										.setAccountId(accountId)
										.build();
					}
					
					accessToken = AccessToken.builder(accessTokenParam)
							.setClientId(clientId)
							.setUserDetails(userDetails)
							.setRead(read)
							.setWrite(write)
							.build();
				}
				tokenCache.put(accessTokenParam, accessToken);
			}
			
			request.setAttribute("access_token", accessToken);
			chain.doFilter(req, resp);
		} catch (IOException ex) {
			throw ex;
			// Alternatively, we can send a JSON formatted error response
		} catch (ServletException ex) {
			throw ex;
			// Alternatively, we can send a JSON formatted error response
		} catch (Exception ex) {
			throw new ServletException(ex);
			// Alternatively, we can send a JSON formatted error response
		}

	}

	@Override
	public void init(FilterConfig config) throws ServletException {
        this.config = config;
        this.tokenInfoEndpoint = this.config.getInitParameter("tokenInfoEndPoint");
        if (this.tokenInfoEndpoint == null || this.tokenInfoEndpoint.trim().isEmpty()) {
        	throw new ServletException("The OAuthFilter filter component expected an initialization parameter named 'tokenInfoEndPoint' with a valid URL as a value.");
        }
        
		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("TLS");
	        sslContext.init(null, null, null);
		} catch (Exception e) {
			throw new ServletException(e);
		}

		SSLSocketFactory sf = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		Scheme httpsScheme = new Scheme("https", 443, sf);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(httpsScheme);
		this.connectionManager = new BasicClientConnectionManager(schemeRegistry);		
        
	}

	@Override
	public void destroy() {
		// No destruction logic needed
	}
}
