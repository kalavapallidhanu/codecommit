package com.impactsure.artnook.configs;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.impactsure.artnook.configs.jwt.JwtAuthenticationProvider;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;


@Configuration
@EnableEncryptableProperties
public class WebMvcConfig implements WebMvcConfigurer  {
	
	
	@Bean
	public JwtAuthenticationProvider jwtAuthenticationProvider() {
		return new JwtAuthenticationProvider();
	}
	
	@Bean
	public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		
		    RestTemplate restTemplate = new RestTemplate();
		    return restTemplate;
	 }
}

