package com.impactsure.artnook.cognito;

import java.io.Serializable;

public class CognitoData implements Serializable {

    private String userName;
    private String tempPassword;
    private String password;
    private String confirmPassword;
    private String smsMfaCode;
    private UserAttributes userAttributes;

    private String cognitoSession;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTempPassword() {
        return tempPassword;
    }

    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getSmsMfaCode() {
        return smsMfaCode;
    }

    public void setSmsMfaCode(String smsMfaCode) {
        this.smsMfaCode = smsMfaCode;
    }

    public UserAttributes getUserAttributes() {
        return userAttributes;
    }

    public void setUserAttributes(UserAttributes userAttributes) {
        this.userAttributes = userAttributes;
    }

    public String getCognitoSession() {
        return cognitoSession;
    }

    public void setCognitoSession(String cognitoSession) {
        this.cognitoSession = cognitoSession;
    }
}
