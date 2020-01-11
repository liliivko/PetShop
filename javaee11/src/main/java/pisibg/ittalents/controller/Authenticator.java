package pisibg.ittalents.controller;


import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pisibg.ittalents.model.dto.LoginUserDTO;
import pisibg.ittalents.model.pojo.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Authenticator {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isEmailValid(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }


    public static boolean passwordIsAuthenticated(LoginUserDTO loginUser, User user) {
        return BCrypt.checkpw(loginUser.getPassword(), user.getPassword());
    }

    public static boolean validateConfirmPassword(String password, String confirmPassword) {
        return (password.equals(confirmPassword));
    }

    public static String encodePassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

}
