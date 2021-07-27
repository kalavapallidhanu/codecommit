package com.impactsure.artnook.configs.jwt;


import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.impactsure.artnook.CurrentTenantId;
import com.impactsure.artnook.cognito.AuthenticationHelper;
import com.impactsure.artnook.configs.BeanUtil;
import com.impactsure.artnook.dto.CognitoUser;

public class JWTAuthenticationFilter extends GenericFilterBean{
	protected static Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
	
	@Autowired
	AuthenticationHelper authenticationHelper;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
       HttpServletRequest req=(HttpServletRequest)request;
       		CognitoUser user=(CognitoUser) req.getSession().getAttribute("user");
       		logger.info("uri: "+req.getRequestURI());
       		if(user!=null) {
    		   Authentication authentication=new JwtAuthentication(user.getUserName(),null);
    		   SecurityContextHolder.getContext().setAuthentication(authentication);
    		   CurrentTenantId.set(user.getTenant());
    		   long currentTime=System.currentTimeMillis();
    		  // logger.info("Token Expiry Time: "+user.getExpiryTime().getTime());
    		  // logger.info("Current Time     : "+currentTime);
    		   if(user.getExpiryTime()!=null && user.getExpiryTime().getTime() < currentTime && !user.isTokenExpired() )
    		   {
    			   logger.info("Token Expiry Time: "+user.getExpiryTime().getTime());
    			   try {
    			   user.setTokenExpired(true);
    			   req.getSession().setAttribute("user", user);
    			   logger.info("Session Expired, Trying to get new Token.");
    			 //  AuthenticationHelper authenticationHelper=new AuthenticationHelper();
    			   authenticationHelper=BeanUtil.getBean(AuthenticationHelper.class);
    			   AnonymousAWSCredentials awsCreds = new AnonymousAWSCredentials();
    				    AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
    				            .standard()
    				            .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
    				            .withRegion(Regions.fromName(authenticationHelper.getRegion()))
    				            .build();
    					 InitiateAuthRequest initiateAuthRequest = new InitiateAuthRequest();
    				     initiateAuthRequest.setAuthFlow(AuthFlowType.REFRESH_TOKEN_AUTH);
    				     initiateAuthRequest.setClientId(authenticationHelper.getClientId());
    				     initiateAuthRequest.addAuthParametersEntry("REFRESH_TOKEN",user.getRefreshToken());
    				     InitiateAuthResult initiateAuthResult = cognitoIdentityProvider.initiateAuth(initiateAuthRequest);
    				     user.setAccessToken(initiateAuthResult.getAuthenticationResult().getAccessToken());
    				     user.setIdToken(initiateAuthResult.getAuthenticationResult().getIdToken());
    				     logger.info("Previous Token Expiry Time:"+user.getExpiryTime());
    				     Calendar gcal = new GregorianCalendar();
    				     gcal.setTime(user.getExpiryTime());
    				     gcal.add(Calendar.HOUR, 1);
    				     user.setExpiryTime(gcal.getTime());
    				     logger.info("New Token Expiry Time:"+user.getExpiryTime());
    				     user.setTokenExpired(false);
    				     req.getSession().setAttribute("user", user);
    			   }
    			   catch (Exception e) {
    				   System.out.println("Token Expired:"+e.getMessage());
					// TODO: handle exception
				}
    		   }
    	   }
    	   filterChain.doFilter(request,response);
		  
    }
}
