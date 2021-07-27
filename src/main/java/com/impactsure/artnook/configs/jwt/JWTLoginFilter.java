package com.impactsure.artnook.configs.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.ChallengeNameType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.RespondToAuthChallengeRequest;
import com.amazonaws.services.cognitoidp.model.RespondToAuthChallengeResult;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.impactsure.artnook.cognito.AuthenticationHelper;
import com.impactsure.artnook.cognito.CognitoData;
import com.impactsure.artnook.configs.BeanUtil;
import com.impactsure.artnook.dto.CognitoUser;
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter{
	protected static Logger logger = LoggerFactory.getLogger(JWTLoginFilter.class);
    AuthenticationHelper authenticationHelper;//=new AuthenticationHelper();
    
    public JWTLoginFilter(String url, AuthenticationManager authenticationManager)
    {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authenticationManager);
       // tokenAuthenticationService = new TokenAuthenticationService();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
    	String username=request.getParameter("username");
    	String password=request.getParameter("password");
    	authenticationHelper=BeanUtil.getBean(AuthenticationHelper.class);
    //	authenticationHelper=Application.getBean("authenticationHelper");  
    	InitiateAuthRequest initiateAuthRequest = authenticationHelper.initiateUserSrpAuthRequest(username);
		try {
		    AnonymousAWSCredentials awsCreds = new AnonymousAWSCredentials();
		    AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
		            .standard()
		            .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
		            .withRegion(Regions.fromName(authenticationHelper.getRegion()))
		            .build();
		    InitiateAuthResult initiateAuthResult = cognitoIdentityProvider.initiateAuth(initiateAuthRequest);
		    
		    if (ChallengeNameType.PASSWORD_VERIFIER.toString().equals(initiateAuthResult.getChallengeName())) {
		        RespondToAuthChallengeRequest challengeRequest = authenticationHelper.userSrpAuthRequest(initiateAuthResult, password);
		        RespondToAuthChallengeResult result = cognitoIdentityProvider.respondToAuthChallenge(challengeRequest);
		        if(result.getChallengeName()==null) {
		        	CognitoUser u= authenticationHelper.processToken(request,result);
		        	 request.getSession().setAttribute("user", u);
		        	  //UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("admin", "password");
		  	        return new JwtAuthentication(username,null);
		        }
		        else if(ChallengeNameType.NEW_PASSWORD_REQUIRED.toString().equals(result.getChallengeName())) {
		  	    
		        	 	CognitoData cd= new CognitoData();
				        cd.setUserName(username);
				        cd.setCognitoSession(result.getSession());
				        request.getSession().setAttribute("cognitoData",cd );
				        request.getSession().setAttribute("oper","new-pwd");
				        request.setAttribute("oper","new-pwd");
				        CognitoUser u=new CognitoUser();
				        u.setName(username);
				        request.getSession().setAttribute("user", u);
				        return new JwtAuthentication(username,null);
			        	  
			          
			    }
			    else if(result.getChallengeName().toString().equals(ChallengeNameType.SMS_MFA.toString()) ){
			    	CognitoData cd= new CognitoData();
			        cd.setUserName(username);
			        cd.setCognitoSession(result.getSession());
			        request.getSession().setAttribute("cognitoData",cd );
			        request.getSession().setAttribute("oper","new-pwd");
			        request.setAttribute("oper","sms-mfa");
			        CognitoUser u=new CognitoUser();
			        u.setName(username);
			        request.getSession().setAttribute("user", u);
			        return new JwtAuthentication(username,null);
			    }
		    }
		  
		}
		catch (UserNotFoundException e) {
			request.setAttribute("oper","user-not-found");
			return new JwtAuthentication(null,null);
		}
		catch (NotAuthorizedException e) {
			request.setAttribute("oper","invalid-credentials");
			return new JwtAuthentication(null,null);
		} catch (final Exception ex) {
			ex.printStackTrace();
		    request.setAttribute("oper","execption");
		    return new JwtAuthentication(null,null);
		
		}
		return new JwtAuthentication(username,null);
       // AccountCredentials credentials = new ObjectMapper().readValue(httpServletRequest.getInputStream(),AccountCredentials.class);
     
    }// end of method
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication)
            throws IOException, ServletException{
    	String oper=(String) request.getAttribute("oper");
    	logger.info("oper:"+oper);
    	if(oper!=null && oper.equals("new-pwd")) {
    		response.sendRedirect("/artnook/new-pwd");
    	}
    	else if(oper!=null && oper.equals("sms-mfa")) {
    		response.sendRedirect("/artnook/sms-mfa");
    	}
    	else if(oper!=null && oper.equals("user-not-found")) {
    		request.setAttribute("error","User not found");
    		response.sendRedirect("/artnook/login?error=Invalid Username");
    	}
    	else if(oper!=null && oper.equals("invalid-credentials")) {
    		response.sendRedirect("/artnook/login?error=Invalid Password.");
    	}
    	else if(oper!=null && oper.equals("execption")) {
    		response.sendRedirect("/artnook/login?error=Unable to login,Please Enter valid Username and Password.");
    	}
    	else {
	        response.sendRedirect("/artnook/sync-user");
    	}
    }
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
    		AuthenticationException failed) throws IOException, ServletException {
    	response.sendRedirect("/artnook/login?error="+(String)request.getAttribute("error"));
    }
    
  
}
