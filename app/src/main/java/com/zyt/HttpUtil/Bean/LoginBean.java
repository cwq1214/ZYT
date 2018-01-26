package com.zyt.HttpUtil.Bean;

/**
 * Created by chenbifeng on 16/7/25.
 */
public class LoginBean {
    public String userID;
    public String tokenSession;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTokenSession() {
        return tokenSession;
    }

    public void setTokenSession(String tokenSession) {
        this.tokenSession = tokenSession;
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "userID='" + userID + '\'' +
                ", tokenSession='" + tokenSession + '\'' +
                '}';
    }
}
