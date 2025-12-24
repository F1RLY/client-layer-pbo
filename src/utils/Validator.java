package utils;

import java.util.regex.Pattern;

public class Validator {
    
    // NIK validation (16 digits)
    public static boolean isValidNIK(String nik) {
        if (nik == null || nik.length() != 16) return false;
        return nik.matches("\\d{16}");
    }
    
    // Phone number validation
    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        return phone.matches("08\\d{9,12}");
    }
    
    // Email validation
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.matches(emailRegex, email);
    }
    
    // Name validation (letters and spaces only)
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        return name.matches("[a-zA-Z\\s]+");
    }
    
    // Required field validation
    public static boolean isRequired(String field) {
        return field != null && !field.trim().isEmpty();
    }
    
    // Number validation
    public static boolean isPositiveNumber(String number) {
        if (number == null) return false;
        try {
            int value = Integer.parseInt(number);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}