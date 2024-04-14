package pl.bodzioch.damian.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Encoder {

    private static final PasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

    public static String encodePassword(String password) {
        return bcryptEncoder.encode(password);
    }

    public static boolean matches(String rawData, String hashedData) {
        return bcryptEncoder.matches(rawData, hashedData);
    }
}
