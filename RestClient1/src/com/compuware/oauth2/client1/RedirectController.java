package com.compuware.oauth2.client1;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.amber.oauth2.client.OAuthClient;
import org.apache.amber.oauth2.client.URLConnectionClient;
import org.apache.amber.oauth2.client.request.OAuthClientRequest;
import org.apache.amber.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.amber.oauth2.client.response.OAuthAuthzResponse;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.types.GrantType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/redirect")
public class RedirectController {
	
	static {
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier () {

			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
			
		});
	}
	

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView handleRedirect(HttpServletRequest request,
                                       HttpServletResponse response) throws OAuthSystemException, OAuthProblemException {
    	OAuthAuthzResponse oar = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
    	String code = oar.getCode();
    	
		OAuthConfig oConfig = OAuthConfigManager.getConfig();

		OAuthClientRequest req = OAuthClientRequest
                .tokenLocation(oConfig.getTokenEndpoint())
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setClientId(oConfig.getClientId())
                .setClientSecret(oConfig.getClientSecret())
                .setRedirectURI(oConfig.getRedirectUri())
                .setCode(code)
                .buildBodyMessage();
            System.out.println(req.toString());
    	
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

            try {
	            OAuthAccessTokenResponse oAuthResponse = oAuthClient.accessToken(req);
	
	            String accessToken = oAuthResponse.getAccessToken();
	            Long expiresIn = oAuthResponse.getExpiresIn();
	            String refreshToken = oAuthResponse.getRefreshToken();
	
	            String oauthAccessToken = String.format("{\"accessToken\": \"%s\", \"issued\": %s, \"expiresIn\": %s, \"refreshToken\": " + (refreshToken != null ? "\"%s\"" : "%s") + " }", accessToken, String.valueOf(System.currentTimeMillis()), expiresIn, refreshToken);
	            System.out.println(oauthAccessToken);
	            
	            HttpSession session = request.getSession(true);
	            session.setAttribute("access_token", oauthAccessToken);
	            
	            String view = oar.getState();
	            if (view == null) {
	            	view = "redirect";
	            }
	            RedirectView redirect = new RedirectView(view, true);
	            
	            return new ModelAndView(redirect);
            } catch (OAuthSystemException ex) {
            	ex.printStackTrace();
            	throw ex;
            } catch (OAuthProblemException ex) {
            	// TODO: Handle OAuth problem exceptions more gracefully (e.g. if resource owner does not authrorize the request)
            	ex.printStackTrace();
            	throw ex;
            }
            
    }
	
}
