package com.zyt.HttpUtil.Bean;

/**
 * Created by chenbifeng on 16/7/25.
 */
public class RegisterBean {
    public String tokenSession;
    public String userID;

    public String getTokenSession() {
        return tokenSession;
    }

    public void setTokenSession(String tokenSession) {
        this.tokenSession = tokenSession;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "RegisterBean{" +
                "tokenSession='" + tokenSession + '\'' +
                ", userID='" + userID + '\'' +
                '}';
    }
}
