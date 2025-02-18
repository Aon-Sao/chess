package service;

public class UserClump {
    private final String username;
    private final String password;
    private final String email;
    private final String authToken;
    private final String message;
    private final int statusCode;

    private UserClump(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.email = builder.email;
        this.authToken = builder.authToken;
        this.message = builder.message;
        this.statusCode = builder.statusCode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    public String email() {
        return email;
    }

    public String authToken() {
        return authToken;
    }

    public String message() {
        return message;
    }

    public int statusCode() {
        return statusCode;
    }

    public static class Builder {
        private String username = "";
        private String password = "";
        private String email = "";
        private String authToken = "";
        private String message = "";
        private int statusCode = 0;

        public UserClump build() {
            return new UserClump(this);
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setAuthToken(String authToken) {
            this.authToken = authToken;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setStatusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }
    }
}