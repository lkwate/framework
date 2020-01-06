package security;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class Validator {

    /**
     * key word validator
     */
    public static final String email = "email";
    public static final String password = "password";
    public static final String alphaNumeric = "alphaNumeric";

    private static LinkedHashMap<String, Pattern> patternLinkedHashMap = new LinkedHashMap();
    static {
        Validator.patternLinkedHashMap.put(Validator.alphaNumeric, Pattern.compile("^[a-zA-Z0-9]+$"));
        /**
         * password must contains : 1 special character, 1 small letter, 1 capital letter and length between 8 and 40
         */
        Validator.patternLinkedHashMap.put(Validator.password, Pattern.compile("((?=.*[a-z])(?=.*\\\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})"));
        Validator.patternLinkedHashMap.put(Validator.email, Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE));
    }

    public static Pattern getPattern(String namePattern) {
        if (namePattern == null) throw new IllegalArgumentException();
        return Validator.patternLinkedHashMap.get(namePattern);
    }
}
