package pisibg.ittalents.utils;


import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pisibg.ittalents.exception.BadRequestException;
import pisibg.ittalents.model.dto.LoginUserDTO;
import pisibg.ittalents.model.pojo.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Authenticator {


    public static final Pattern VALID_ADDRESS = Pattern.compile("^[a-zA-Z]*$");
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_PASSWORD = Pattern.compile("^(?!.* )(?=.*\\d)" +
            "(?=.*[A-Z]).{8,15}$");

    //only alphabetical, no whitespace
    public static final Pattern VALID_NAME = Pattern.compile("^[a-zA-Z]*$");


    public static boolean isEmailValid(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    public static boolean isValidPassword(String password) {
        Matcher matcher = VALID_PASSWORD.matcher(password);
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

    public static boolean isFirstNameValid(String firstName) {
        if (firstName == null || firstName.isEmpty()) {
           return false;
        }
        Matcher matcher = VALID_NAME.matcher(firstName);
        return matcher.find();
    }

    public static boolean isLastNameValid(String lastName) {
        if (lastName == null) {
            return false;
        }
        Matcher matcher = VALID_NAME.matcher(lastName);
        return matcher.find();
    }

    public static boolean isCityValid(String city) {
        Matcher matcher = VALID_ADDRESS.matcher(city);
        return matcher.find();
    }

    public static boolean dateValid(LocalDateTime date1, LocalDateTime date2) {
        if(date1==null ||date2==null || date2.toString().trim().isEmpty() || date1.toString().trim().isEmpty()){
            throw new BadRequestException("Date should be valid in format yyyy-mm-dd hh:mm:ss");
        }
        return date2.isAfter(date1);
    }


}
