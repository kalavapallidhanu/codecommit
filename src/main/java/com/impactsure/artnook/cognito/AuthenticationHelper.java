package com.impactsure.artnook.cognito;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/*
 *  Copyright 2013-2016 Amazon.com,
 *  Inc. or its affiliates. All Rights Reserved.
 *
 *  Licensed under the Amazon Software License (the "License").
 *  You may not use this file except in compliance with the
 *  License. A copy of the License is located at
 *
 *      http://aws.amazon.com/asl/
 *
 *  or in the "license" file accompanying this file. This file is
 *  distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *  CONDITIONS OF ANY KIND, express or implied. See the License
 *  for the specific language governing permissions and
 *  limitations under the License.
 */


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.ChallengeNameType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.RespondToAuthChallengeRequest;
import com.amazonaws.services.cognitoidp.model.RespondToAuthChallengeResult;
import com.amazonaws.util.Base64;
import com.amazonaws.util.StringUtils;
import com.impactsure.artnook.dto.CognitoUser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

/**
 * Private class for SRP client side math.
 */
		public class AuthenticationHelper {
	
			
			protected static Logger logger = LoggerFactory.getLogger(AuthenticationHelper.class);
			
		    private static final String HEX_N =
		            "FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD1"
		                    + "29024E088A67CC74020BBEA63B139B22514A08798E3404DD"
		                    + "EF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245"
		                    + "E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7ED"
		                    + "EE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3D"
		                    + "C2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F"
		                    + "83655D23DCA3AD961C62F356208552BB9ED529077096966D"
		                    + "670C354E4ABC9804F1746C08CA18217C32905E462E36CE3B"
		                    + "E39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9"
		                    + "DE2BCBF6955817183995497CEA956AE515D2261898FA0510"
		                    + "15728E5A8AAAC42DAD33170D04507A33A85521ABDF1CBA64"
		                    + "ECFB850458DBEF0A8AEA71575D060C7DB3970F85A6E1E4C7"
		                    + "ABF5AE8CDB0933D71E8C94E04A25619DCEE3D2261AD2EE6B"
		                    + "F12FFA06D98A0864D87602733EC86A64521F2B18177B200C"
		                    + "BBE117577A615D6C770988C0BAD946E208E24FA074E5AB31"
		                    + "43DB5BFCE0FD108E4B82D120A93AD2CAFFFFFFFFFFFFFFFF";
		    private static final BigInteger N = new BigInteger(HEX_N, 16);
		    private static final BigInteger g = BigInteger.valueOf(2);
		    private static final BigInteger k;
		    private static final int EPHEMERAL_KEY_LENGTH = 1024;
		    private static final int DERIVED_KEY_SIZE = 16;
		    private static final String DERIVED_KEY_INFO = "Caldera Derived Key";
		    private static final ThreadLocal<MessageDigest> THREAD_MESSAGE_DIGEST =
		            new ThreadLocal<MessageDigest>() {
		                @Override
		                protected MessageDigest initialValue() {
		                    try {
		                        return MessageDigest.getInstance("SHA-256");
		                    } catch (NoSuchAlgorithmException e) {
		                        throw new SecurityException("Exception in authentication", e);
		                    }
		                }
		            };
		    private static final SecureRandom SECURE_RANDOM;
		
		    static {
		        try {
		            SECURE_RANDOM = SecureRandom.getInstance("SHA1PRNG");
		
		            MessageDigest messageDigest = THREAD_MESSAGE_DIGEST.get();
		            messageDigest.reset();
		            messageDigest.update(N.toByteArray());
		            byte[] digest = messageDigest.digest(g.toByteArray());
		            k = new BigInteger(1, digest);
		        } catch (NoSuchAlgorithmException e) {
		            throw new SecurityException(e.getMessage(), e);
		        }
		    }
		
		    private BigInteger a;
		    private BigInteger A;
		    private String userPoolID;
		    private String clientId;
		    private String region;
		    private String cognitoAccessKey;
		    private String cognitoSecretKey;
		    private int readTimeOut=2000;
		    private int connectionTimeOut=2000;
		    private static final String COGNITO_IDENTITY_POOL_URL = "https://cognito-idp.%s.amazonaws.com/%s";
			private static final String JSON_WEB_TOKEN_SET_URL_SUFFIX = "/.well-known/jwks.json";
		
		    public int getReadTimeOut() {
				return readTimeOut;
			}
		
			public void setReadTimeOut(int readTimeOut) {
				this.readTimeOut = readTimeOut;
			}
		
			public int getConnectionTimeOut() {
				return connectionTimeOut;
			}
		
			public void setConnectionTimeOut(int connectionTimeOut) {
				this.connectionTimeOut = connectionTimeOut;
			}
		
			public String getUserPoolID() {
				return userPoolID;
			}
		
			public void setUserPoolID(String userPoolID) {
				this.userPoolID = userPoolID;
			}
		
			public String getClientId() {
				return clientId;
			}
		
			public void setClientId(String clientId) {
				this.clientId = clientId;
			}
		
			public String getRegion() {
				return region;
			}
		
			public void setRegion(String region) {
				this.region = region;
			}
		
			 public String getCognitoAccessKey() {
				return cognitoAccessKey;
			}

			public void setCognitoAccessKey(String cognitoAccessKey) {
				this.cognitoAccessKey = cognitoAccessKey;
			}

			public String getCognitoSecretKey() {
				return cognitoSecretKey;
			}

			public void setCognitoSecretKey(String cognitoSecretKey) {
				this.cognitoSecretKey = cognitoSecretKey;
			}

			public String getJwkUrl() {
			            return String.format(COGNITO_IDENTITY_POOL_URL + JSON_WEB_TOKEN_SET_URL_SUFFIX,region,userPoolID);
			 }
		
			 public String getCognitoIdentityPoolUrl() {
			      return String.format(COGNITO_IDENTITY_POOL_URL,region,userPoolID);
			}
			public AuthenticationHelper(String userPoolId,String clientId,String region,String cognitoAccessKey,String cognitoSecretKey) {
				
				this.clientId=clientId;
				this.userPoolID=userPoolId;
				this.region=region;
				this.cognitoAccessKey=cognitoAccessKey;
				this.cognitoSecretKey=cognitoSecretKey;
			
				do {
		            a = new BigInteger(EPHEMERAL_KEY_LENGTH, SECURE_RANDOM).mod(N);
		            A = g.modPow(a, N);
		        } while (A.mod(N).equals(BigInteger.ZERO));
			
		    }
		
		    private BigInteger getA() {
		        return A;
		    }
		
		    private byte[] getPasswordAuthenticationKey(String userId,
		                                                String userPassword,
		                                                BigInteger B,
		                                                BigInteger salt) {
		        // Authenticate the password
		        // u = H(A, B)
		        MessageDigest messageDigest = THREAD_MESSAGE_DIGEST.get();
		        messageDigest.reset();
		        messageDigest.update(A.toByteArray());
		        BigInteger u = new BigInteger(1, messageDigest.digest(B.toByteArray()));
		        if (u.equals(BigInteger.ZERO)) {
		            throw new SecurityException("Hash of A and B cannot be zero");
		        }
		
		        // x = H(salt | H(poolName | userId | ":" | password))
		        messageDigest.reset();
		        messageDigest.update(this.userPoolID.split("_", 2)[1].getBytes(StringUtils.UTF8));
		        messageDigest.update(userId.getBytes(StringUtils.UTF8));
		        messageDigest.update(":".getBytes(StringUtils.UTF8));
		        byte[] userIdHash = messageDigest.digest(userPassword.getBytes(StringUtils.UTF8));
		
		        messageDigest.reset();
		        messageDigest.update(salt.toByteArray());
		        BigInteger x = new BigInteger(1, messageDigest.digest(userIdHash));
		        BigInteger S = (B.subtract(k.multiply(g.modPow(x, N))).modPow(a.add(u.multiply(x)), N)).mod(N);
		
		        Hkdf hkdf;
		        try {
		            hkdf = Hkdf.getInstance("HmacSHA256");
		        } catch (NoSuchAlgorithmException e) {
		            throw new SecurityException(e.getMessage(), e);
		        }
		        hkdf.init(S.toByteArray(), u.toByteArray());
		        byte[] key = hkdf.deriveKey(DERIVED_KEY_INFO, DERIVED_KEY_SIZE);
		        return key;
		    }
		
		
		    /**
		     * Method to orchestrate the SRP Authentication
		     *
		     * @param username Username for the SRP request
		     * @param password Password for the SRP request
		     * @return the JWT token if the request is successful else null.
		     */
		    String PerformSRPAuthentication(String username, String password) {
		        String authresult = null;
		
		        InitiateAuthRequest initiateAuthRequest = initiateUserSrpAuthRequest(username);
		        try {
		            AnonymousAWSCredentials awsCreds = new AnonymousAWSCredentials();
		            AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
		                    .standard()
		                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
		                    .withRegion(Regions.fromName(this.region))
		                    .build();
		            InitiateAuthResult initiateAuthResult = cognitoIdentityProvider.initiateAuth(initiateAuthRequest);
		            
		            if (ChallengeNameType.PASSWORD_VERIFIER.toString().equals(initiateAuthResult.getChallengeName())) {
		                RespondToAuthChallengeRequest challengeRequest = userSrpAuthRequest(initiateAuthResult, password);
		                RespondToAuthChallengeResult result = cognitoIdentityProvider.respondToAuthChallenge(challengeRequest);
		                
		                if(ChallengeNameType.NEW_PASSWORD_REQUIRED.toString().equals(result.getChallengeName())) {
		                	RespondToAuthChallengeRequest  pwd=newPwdSerSrpAuthRequest(initiateAuthResult, password,"Medex@123",result.getChallengeParameters());
		                	pwd.setSession(result.getSession());
		                	
		                	 RespondToAuthChallengeResult result1 = cognitoIdentityProvider.respondToAuthChallenge(pwd);
		                    
		                     System.out.println(result1);
		                }
		                else {
		                authresult = result.getAuthenticationResult().getIdToken();
		                System.out.println("ID Token: "+authresult);
		            }
		            }
		        } catch (final Exception ex) {
		            System.out.println("Exception" + ex);
		
		        }
		        return authresult;
		    }
		
		    /**
		     * Initialize the authentication request for the first time.
		     *
		     * @param username The user for which the authentication request is created.
		     * @return the Authentication request.
		     */
		    public InitiateAuthRequest initiateUserSrpAuthRequest(String username) {
		
		        InitiateAuthRequest initiateAuthRequest = new InitiateAuthRequest();
		        initiateAuthRequest.setAuthFlow(AuthFlowType.USER_SRP_AUTH);
		        initiateAuthRequest.setClientId(this.clientId);
		        //Only to be used if the pool contains the secret key.
		        //initiateAuthRequest.addAuthParametersEntry("SECRET_HASH", this.calculateSecretHash(this.clientId,this.secretKey,username));
		        initiateAuthRequest.addAuthParametersEntry("USERNAME", username);
		        initiateAuthRequest.addAuthParametersEntry("SRP_A", this.getA().toString(16));
		        return initiateAuthRequest;
		    }
		
		
		    /**
		     * Method is used to respond to the Auth challange from the user pool
		     *
		     * @param challenge The authenticaion challange returned from the cognito user pool
		     * @param password  The password to be used to respond to the authentication challenge.
		     * @return the Request created for the previous authentication challenge.
		     */
		    public RespondToAuthChallengeRequest userSrpAuthRequest(InitiateAuthResult challenge,
		                                                             String password
		    ) {
		        String userIdForSRP = challenge.getChallengeParameters().get("USER_ID_FOR_SRP");
		        String usernameInternal = challenge.getChallengeParameters().get("USERNAME");
		
		        BigInteger B = new BigInteger(challenge.getChallengeParameters().get("SRP_B"), 16);
		        if (B.mod(AuthenticationHelper.N).equals(BigInteger.ZERO)) {
		            throw new SecurityException("SRP error, B cannot be zero");
		        }
		
		        BigInteger salt = new BigInteger(challenge.getChallengeParameters().get("SALT"), 16);
		        byte[] key = getPasswordAuthenticationKey(userIdForSRP, password, B, salt);
		
		        Date timestamp = new Date();
		        byte[] hmac = null;
		        try {
		            Mac mac = Mac.getInstance("HmacSHA256");
		            SecretKeySpec keySpec = new SecretKeySpec(key, "HmacSHA256");
		            mac.init(keySpec);
		            mac.update(this.userPoolID.split("_", 2)[1].getBytes(StringUtils.UTF8));
		            mac.update(userIdForSRP.getBytes(StringUtils.UTF8));
		            byte[] secretBlock = Base64.decode(challenge.getChallengeParameters().get("SECRET_BLOCK"));
		            mac.update(secretBlock);
		            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.US);
		            simpleDateFormat.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
		            String dateString = simpleDateFormat.format(timestamp);
		            byte[] dateBytes = dateString.getBytes(StringUtils.UTF8);
		            hmac = mac.doFinal(dateBytes);
		        } catch (Exception e) {
		            System.out.println(e);
		        }
		
		        SimpleDateFormat formatTimestamp = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.US);
		        formatTimestamp.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
		
		        Map<String, String> srpAuthResponses = new HashMap<>();
		        srpAuthResponses.put("PASSWORD_CLAIM_SECRET_BLOCK", challenge.getChallengeParameters().get("SECRET_BLOCK"));
		        srpAuthResponses.put("PASSWORD_CLAIM_SIGNATURE", new String(Base64.encode(hmac), StringUtils.UTF8));
		        srpAuthResponses.put("TIMESTAMP", formatTimestamp.format(timestamp));
		        srpAuthResponses.put("USERNAME", usernameInternal);
		
		        RespondToAuthChallengeRequest authChallengeRequest = new RespondToAuthChallengeRequest();
		        authChallengeRequest.setChallengeName(challenge.getChallengeName());
		        authChallengeRequest.setClientId(clientId);
		        authChallengeRequest.setSession(challenge.getSession());
		        authChallengeRequest.setChallengeResponses(srpAuthResponses);
		
		        return authChallengeRequest;
		    }
		
		    
		    RespondToAuthChallengeRequest newPwdSerSrpAuthRequest(InitiateAuthResult challenge,
		            String password,String newPassword,Map<String, String> srpAuthResponses) {
					String userIdForSRP = challenge.getChallengeParameters().get("USER_ID_FOR_SRP");
					String usernameInternal = challenge.getChallengeParameters().get("USERNAME");
					
					BigInteger B = new BigInteger(challenge.getChallengeParameters().get("SRP_B"), 16);
					if (B.mod(AuthenticationHelper.N).equals(BigInteger.ZERO)) {
					throw new SecurityException("SRP error, B cannot be zero");
					}
					
					BigInteger salt = new BigInteger(challenge.getChallengeParameters().get("SALT"), 16);
					byte[] key = getPasswordAuthenticationKey(userIdForSRP, password, B, salt);
					
					Date timestamp = new Date();
					byte[] hmac = null;
					try {
					Mac mac = Mac.getInstance("HmacSHA256");
					SecretKeySpec keySpec = new SecretKeySpec(key, "HmacSHA256");
					mac.init(keySpec);
					mac.update(this.userPoolID.split("_", 2)[1].getBytes(StringUtils.UTF8));
					mac.update(userIdForSRP.getBytes(StringUtils.UTF8));
					byte[] secretBlock = Base64.decode(challenge.getChallengeParameters().get("SECRET_BLOCK"));
					mac.update(secretBlock);
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.US);
					simpleDateFormat.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
					String dateString = simpleDateFormat.format(timestamp);
					byte[] dateBytes = dateString.getBytes(StringUtils.UTF8);
					hmac = mac.doFinal(dateBytes);
					} catch (Exception e) {
					System.out.println(e);
					}
					
					SimpleDateFormat formatTimestamp = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.US);
					formatTimestamp.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
					
					//Map<String, String> srpAuthResponses = new HashMap<>();
					srpAuthResponses.put("PASSWORD_CLAIM_SECRET_BLOCK", challenge.getChallengeParameters().get("SECRET_BLOCK"));
					srpAuthResponses.put("PASSWORD_CLAIM_SIGNATURE", new String(Base64.encode(hmac), StringUtils.UTF8));
					srpAuthResponses.put("TIMESTAMP", formatTimestamp.format(timestamp));
					srpAuthResponses.put("USERNAME", usernameInternal);
					srpAuthResponses.put("NEW_PASSWORD", newPassword);
					srpAuthResponses.put("userAttributes.gender", "male");
					srpAuthResponses.put("userAttributes.picture", "aa");
					srpAuthResponses.put("userAttributes.nickname", "Anu");
					srpAuthResponses.put("userAttributes.name", "Anantha Rao ");
					RespondToAuthChallengeRequest authChallengeRequest = new RespondToAuthChallengeRequest();
					authChallengeRequest.setChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED);
					authChallengeRequest.setClientId(clientId);
					authChallengeRequest.setSession(challenge.getSession());
					authChallengeRequest.setChallengeResponses(srpAuthResponses);
					
					return authChallengeRequest;
		}
		
		    
		    /**
		     * Calculate the secret hash to be sent along with the authentication request.
		     *
		     * @param userPoolClientId     : The client id of the app.
		     * @param userPoolClientSecret : The secret for the userpool client id.
		     * @param userName             : The username of the user trying to authenticate.
		     * @return Calculated secret hash.
		     */
		    private String calculateSecretHash(String userPoolClientId, String userPoolClientSecret, String userName) {
		        final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
		
		        SecretKeySpec signingKey = new SecretKeySpec(
		                userPoolClientSecret.getBytes(StandardCharsets.UTF_8),
		                HMAC_SHA256_ALGORITHM);
		        try {
		            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
		            mac.init(signingKey);
		            mac.update(userName.getBytes(StandardCharsets.UTF_8));
		            byte[] rawHmac = mac.doFinal(userPoolClientId.getBytes(StandardCharsets.UTF_8));
		            return java.util.Base64.getEncoder().encodeToString(rawHmac);
		        } catch (Exception e) {
		            throw new RuntimeException("Error while calculating ");
		        }
		    }
		
		    /**
		     * Internal class for doing the Hkdf calculations.
		     */
		    final static class Hkdf {
		        private static final int MAX_KEY_SIZE = 255;
		        private final byte[] EMPTY_ARRAY = new byte[0];
		        private final String algorithm;
		        private SecretKey prk = null;
		
		
		        /**
		         * @param algorithm REQUIRED: The type of HMAC algorithm to be used.
		         */
		        private Hkdf(String algorithm) {
		            if (!algorithm.startsWith("Hmac")) {
		                throw new IllegalArgumentException("Invalid algorithm " + algorithm
		                        + ". Hkdf may only be used with Hmac algorithms.");
		            } else {
		                this.algorithm = algorithm;
		            }
		        }
		
		        private static Hkdf getInstance(String algorithm) throws NoSuchAlgorithmException {
		
		            return new Hkdf(algorithm);
		        }
		
		        /**
		         * @param ikm REQUIRED: The input key material.
		         */
		        public void init(byte[] ikm) {
		            this.init(ikm, (byte[]) null);
		        }
		
		        /**
		         * @param ikm  REQUIRED: The input key material.
		         * @param salt REQUIRED: Random bytes for salt.
		         */
		        private void init(byte[] ikm, byte[] salt) {
		            byte[] realSalt = salt == null ? EMPTY_ARRAY : (byte[]) salt.clone();
		            byte[] rawKeyMaterial = EMPTY_ARRAY;
		
		            try {
		                final Mac e = Mac.getInstance(this.algorithm);
		                if (realSalt.length == 0) {
		                    realSalt = new byte[e.getMacLength()];
		                    Arrays.fill(realSalt, (byte) 0);
		                }
		
		                e.init(new SecretKeySpec(realSalt, this.algorithm));
		                rawKeyMaterial = e.doFinal(ikm);
		                final SecretKeySpec key = new SecretKeySpec(rawKeyMaterial, this.algorithm);
		                Arrays.fill(rawKeyMaterial, (byte) 0);
		                this.unsafeInitWithoutKeyExtraction(key);
		            } catch (final GeneralSecurityException var10) {
		                throw new RuntimeException("Unexpected exception", var10);
		            } finally {
		                Arrays.fill(rawKeyMaterial, (byte) 0);
		            }
		
		        }
		
		        /**
		         * @param rawKey REQUIRED: Current secret key.
		         * @throws InvalidKeyException
		         */
		        private void unsafeInitWithoutKeyExtraction(SecretKey rawKey) throws InvalidKeyException {
		            if (!rawKey.getAlgorithm().equals(this.algorithm)) {
		                throw new InvalidKeyException(
		                        "Algorithm for the provided key must match the algorithm for this Hkdf. Expected "
		                                + this.algorithm + " but found " + rawKey.getAlgorithm());
		            } else {
		                this.prk = rawKey;
		            }
		        }
		
		        /**
		         * @param info   REQUIRED
		         * @param length REQUIRED
		         * @return converted bytes.
		         */
		        private byte[] deriveKey(String info, int length) {
		            return this.deriveKey(info != null ? info.getBytes(StringUtils.UTF8) : null, length);
		        }
		
		        /**
		         * @param info   REQUIRED
		         * @param length REQUIRED
		         * @return converted bytes.
		         */
		        private byte[] deriveKey(byte[] info, int length) {
		            final byte[] result = new byte[length];
		
		            try {
		                this.deriveKey(info, length, result, 0);
		                return result;
		            } catch (final ShortBufferException var5) {
		                throw new RuntimeException(var5);
		            }
		        }
		
		        /**
		         * @param info   REQUIRED
		         * @param length REQUIRED
		         * @param output REQUIRED
		         * @param offset REQUIRED
		         * @throws ShortBufferException
		         */
		        private void deriveKey(byte[] info, int length, byte[] output, int offset)
		                throws ShortBufferException {
		            this.assertInitialized();
		            if (length < 0) {
		                throw new IllegalArgumentException("Length must be a non-negative value.");
		            } else if (output.length < offset + length) {
		                throw new ShortBufferException();
		            } else {
		                final Mac mac = this.createMac();
		                if (length > MAX_KEY_SIZE * mac.getMacLength()) {
		                    throw new IllegalArgumentException(
		                            "Requested keys may not be longer than 255 times the underlying HMAC length.");
		                } else {
		                    byte[] t = EMPTY_ARRAY;
		
		                    try {
		                        int loc = 0;
		
		                        for (byte i = 1; loc < length; ++i) {
		                            mac.update(t);
		                            mac.update(info);
		                            mac.update(i);
		                            t = mac.doFinal();
		
		                            for (int x = 0; x < t.length && loc < length; ++loc) {
		                                output[loc] = t[x];
		                                ++x;
		                            }
		                        }
		                    } finally {
		                        Arrays.fill(t, (byte) 0);
		                    }
		
		                }
		            }
		        }
		
		        /**
		         * @return the generates message authentication code.
		         */
		        private Mac createMac() {
		            try {
		                final Mac ex = Mac.getInstance(this.algorithm);
		                ex.init(this.prk);
		                return ex;
		            } catch (final NoSuchAlgorithmException var2) {
		                throw new RuntimeException(var2);
		            } catch (final InvalidKeyException var3) {
		                throw new RuntimeException(var3);
		            }
		        }
		
		        /**
		         * Checks for a valid pseudo-random key.
		         */
		        private void assertInitialized() {
		            if (this.prk == null) {
		                throw new IllegalStateException("Hkdf has not been initialized");
		            }
		        }
		    }
		    
		    public CognitoUser processToken(HttpServletRequest request, RespondToAuthChallengeResult result) throws ParseException, BadJOSEException, JOSEException {
					URL JWKUrl = null;
					CognitoUser user=new CognitoUser();
					try {
						JWKUrl = new URL(getJwkUrl());
					logger.info("processing token");
					ResourceRetriever resourceRetriever = new DefaultResourceRetriever(connectionTimeOut, readTimeOut);
					RemoteJWKSet remoteJWKSet = new RemoteJWKSet(JWKUrl,resourceRetriever);
					 JWT jwt = JWTParser.parse(result.getAuthenticationResult().getIdToken());
			         
			          // check if issuer is our user pool
			          if (getCognitoIdentityPoolUrl().equals(jwt.getJWTClaimsSet().getIssuer())) {
			        	  logger.info("Issuer verified.");
			        	  user.setAccessToken(result.getAuthenticationResult().getAccessToken());
			              user.setIdToken(result.getAuthenticationResult().getIdToken());
			              user.setRefreshToken(result.getAuthenticationResult().getRefreshToken());
			        	  JWSKeySelector keySelector = new JWSVerificationKeySelector(JWSAlgorithm.RS256, remoteJWKSet);
		
			              ConfigurableJWTProcessor jwtProcessor = new DefaultJWTProcessor();
			              jwtProcessor.setJWSKeySelector(keySelector);
			              JWTClaimsSet claimsSet = jwtProcessor.process(jwt, null);
			              user.setEmail((String) claimsSet.getClaim("email"));
			              user.setUserName((String) claimsSet.getClaim("cognito: username"));
			              user.setMobileNo( (String) claimsSet.getClaim("phone_number"));
			              user.setName((String) claimsSet.getClaim("name"));
			              user.setExpiryTime((Date)claimsSet.getClaim("exp"));
			              user.setIssuedAt((Date)claimsSet.getClaim("iat"));
			              user.setPicture("");
			             if(claimsSet.getClaim("cognito:groups")!=null) {
				              List<String> groups = (List<String>) claimsSet.getClaim("cognito:groups");
				             
				            //Added by Gufran to separate group and tenant name  05-DEC-2019
				              
				              if(groups!=null) {
				            	  String group=groups.get(0);
				            	  String tenant=group.substring(0, group.lastIndexOf('_'));
				        		  String userGroup=group.substring(group.lastIndexOf('_')+1);
				          		  user.setUserGroup(userGroup);
				          		  user.setTenant(tenant);
				          		  user.setGroups(groups);
				              }
				           
				              List<GrantedAuthority> authorities = new ArrayList<>();
			                  authorities.add(new SimpleGrantedAuthority(groups.get(0)));
			             }
			         	request.getSession().setAttribute("token", result.getAuthenticationResult().getAccessToken());
			          }
					} catch (MalformedURLException e) {
						logger.info("error: "+e.getMessage());
						e.printStackTrace();
					}
					catch (Exception e) {
						logger.info("exception: "+e.getMessage());
						e.printStackTrace();
					}
					return user;
		}
}