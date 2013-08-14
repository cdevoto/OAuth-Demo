package com.compuware.oauth2.client3;

import java.io.IOException;

import javax.net.ssl.SSLContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/test")
public class TestController {
	
	private ClientConnectionManager connectionManager;

	public TestController () throws ServletException {
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
	
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView handleRedirect(HttpServletRequest request,
                                       HttpServletResponse response) throws ServletException, ClientProtocolException, IOException, JSONException {
    	// Obtain the access token
    	 String accessToken = AccessTokenManager.getAccessToken();
    	 JSONObject tokenObject  = new JSONObject(accessToken);
    	 String accessTokenValue = tokenObject.getString("accessToken");
    	 
    	 // Invoke the REST API and attach the result to the request
		 HttpClient httpClient = new DefaultHttpClient(this.connectionManager);
		 HttpGet httpGet = new HttpGet("https://localhost:9443/RestServer/api/get_data");
		 httpGet.addHeader("Authorization", "Bearer " + accessTokenValue);
		 HttpResponse tokenInfoResponse = httpClient.execute(httpGet);
		 HttpEntity entity = tokenInfoResponse.getEntity();
		 String data = EntityUtils.toString(entity);
		 request.setAttribute("data", data);

	     return new ModelAndView("test");
	     
            
    }
	
}
