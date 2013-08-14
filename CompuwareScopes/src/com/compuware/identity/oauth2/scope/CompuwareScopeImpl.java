package com.compuware.identity.oauth2.scope;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.forgerock.openam.ext.cts.repo.DefaultOAuthTokenStoreImpl;
import org.forgerock.openam.oauth2.model.CoreToken;
import org.forgerock.openam.oauth2.provider.OAuth2TokenStore;
import org.forgerock.openam.oauth2.provider.Scope;
import org.forgerock.openam.oauth2.provider.impl.ScopeImpl;
import org.forgerock.openam.oauth2.utils.OAuth2Utils;

import com.sun.identity.idm.AMIdentity;

/**
 * This class is identical in all respects to the default ScopeImpl class.
 * The only exceptions is in the retrieveTokenInfoEndPoint method end point
 * where the client id is appended to the output which is returned when the
 * tokenInforEndpoint is invoked.
 */
// TODO:  Use the Decorator pattern to wrap ScopeImpl!
public class CompuwareScopeImpl implements Scope {
    private static final String MULTI_ATTRIBUTE_SEPARATOR = ",";
	
    private OAuth2TokenStore store = null;
    private AMIdentity id = null;
    private ScopeImpl delegate = null;

    public CompuwareScopeImpl(){
        this.store = new DefaultOAuthTokenStoreImpl();
        this.id = null;
        this.delegate = new ScopeImpl(this.store, this.id);
    }

    /**
     * {@inheritDoc}
     */
    public Set<String> scopeToPresentOnAuthorizationPage(Set<String> requestedScope, Set<String> availableScopes, Set<String> defaultScopes){

        return delegate.scopeToPresentOnAuthorizationPage(requestedScope, availableScopes, defaultScopes);
    }

    /**
     * {@inheritDoc}
     */
    public Set<String> scopeRequestedForAccessToken(Set<String> requestedScope, Set<String> availableScopes, Set<String> defaultScopes){

         return delegate.scopeRequestedForAccessToken(requestedScope, availableScopes, defaultScopes);
    }

    /**
     * {@inheritDoc}
     */
    public Set<String> scopeRequestedForRefreshToken(Set<String> requestedScope,
                                                     Set<String> availableScopes,
                                                     Set<String> allScopes,
                                                     Set<String> defaultScopes){

        return delegate.scopeRequestedForRefreshToken(requestedScope, availableScopes, allScopes, defaultScopes);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Object> evaluateScope(CoreToken token){
        Map<String, Object> map = new HashMap<String, Object>();
        Set<String> scopes = token.getScope();
        String resourceOwner = token.getUserID();
        String clientId = token.getClientID();

        map.put("client_id", clientId);
        String[] permissions = {"read", "write"};
        
        for (String scope : permissions) {
            if (scopes.contains(scope)) {
               map.put(scope, true);
            } else {
                map.put(scope, false);
            }
        }

        if ((resourceOwner != null)){
        	String [] resourceOwnerAttributes = {"uid", "givenName", "sn", "cn", "mail", "accountId"};
            AMIdentity id = null;
            try {

                if (this.id == null){
                    id = OAuth2Utils.getIdentity(resourceOwner, token.getRealm());
                } else {
                    id = this.id;
                }
            } catch (Exception e){
                OAuth2Utils.DEBUG.error("Unable to get user identity", e);
            }
            if (id != null){
                for (String scope : resourceOwnerAttributes){
                    try {
                        Set<String> attributes = id.getAttribute(scope);
                        if (attributes != null || !attributes.isEmpty()) {
                            Iterator<String> iter = attributes.iterator();
                            StringBuilder builder = new StringBuilder();
                            while (iter.hasNext()) {
                                builder.append(iter.next());
                                if (iter.hasNext()) {
                                    builder.append(MULTI_ATTRIBUTE_SEPARATOR);
                                }
                            }
                            map.put(scope, builder.toString());
                        }
                    } catch (Exception e){
                        OAuth2Utils.DEBUG.error("Unable to get attribute", e);
                    }
                }
            }
        }

        return map;

    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Object> extraDataToReturnForTokenEndpoint(Map<String, String> parameters, CoreToken token){
        return delegate.extraDataToReturnForTokenEndpoint(parameters, token);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, String> extraDataToReturnForAuthorizeEndpoint(Map<String, String> parameters, Map<String, CoreToken> tokens){
        return delegate.extraDataToReturnForAuthorizeEndpoint(parameters, tokens);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String,Object> getUserInfo(CoreToken token){


        return delegate.getUserInfo(token);
    }


}
