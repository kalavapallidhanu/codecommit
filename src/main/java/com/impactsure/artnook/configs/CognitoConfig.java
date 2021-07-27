package com.impactsure.artnook.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.impactsure.artnook.cognito.AuthenticationHelper;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@Configuration
@EnableEncryptableProperties
@Component
public class CognitoConfig {
	@Value("${USE_POOL_ID}")
	private String userPoolID;

	@Value("${CLIENT_ID}")
	private String clientId;

	@Value("${REGION}")
	private String region;
	
	@Value("${COGNITO_ACCESS_KEY}")
	private String cognitoAccessKey;
	
	@Value("${COGNITO_SECRET_KEY}")
	private String cognitoSecretKey;
	    
	@Bean
	public AuthenticationHelper authenticationHelper() {
		return new AuthenticationHelper(userPoolID, clientId, region, cognitoAccessKey, cognitoSecretKey);
	}
	
}
