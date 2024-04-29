package pl.bodzioch.damian.client.bur.configuration;

class Token {

    private static String token = "";

    static String getToken() {
        return token;
    }

    static void setToken(String token) {
        Token.token = token;
    }
}
