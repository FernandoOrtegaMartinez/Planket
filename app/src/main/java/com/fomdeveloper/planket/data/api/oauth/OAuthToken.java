package com.fomdeveloper.planket.data.api.oauth;


/**
 * Created by Fernando on 13/08/16.
 */
public class OAuthToken {

    private String token;
    private String tokenSecret;
    private String fullName;
    private String userName;
    private String userId;


    public static class Builder {

        private final String token;
        private final String tokenSecret;

        private String fullName = "";
        private String userName = "";
        private String userId = "";

        public Builder(String token, String tokenSecret) {
            this.token = token;
            this.tokenSecret = tokenSecret;
        }

        public Builder fullName(String value) {
            fullName = value;
            return this;
        }

        public Builder userName(String value) {
            userName = value;
            return this;
        }

        public Builder userId(String value) {
            userId = value;
            return this;
        }

        public OAuthToken build() {
            return new OAuthToken(this);
        }
    }

    private OAuthToken(Builder builder) {
        token = builder.token;
        tokenSecret = builder.tokenSecret;
        fullName = builder.fullName;
        userName = builder.userName;
        userId = builder.userId;
    }

    public String getToken() {
        return token;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }
}
