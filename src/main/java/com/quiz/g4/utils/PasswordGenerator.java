package com.quiz.g4.utils;

public class PasswordGenerator {

    public static String generatePassword() {
        int length = 8;
        String password = "";
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        while (password.length() < length) {
            int randomIndex = (int) (Math.random() * characterSet.length());
            password += characterSet.charAt(randomIndex);
        }

        // Ensure the password contains at least one uppercase letter, one lowercase letter, and one number
        if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
            return generatePassword();
        }

        return password;
    }
}
