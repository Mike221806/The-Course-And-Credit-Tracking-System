package com.university.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for password hashing and validation.
 * Demonstrates Single Responsibility Principle (SRP) by focusing only on password operations.
 * Provides secure password handling using SHA-256 with salt.
 */
public class PasswordUtil {

    private static final SecureRandom random = new SecureRandom();
    private static final int SALT_LENGTH = 16;

    /**
     * Hashes a password with a randomly generated salt.
     * @param password the plain text password
     * @return the hashed password with salt (Base64 encoded)
     */
    public static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        try {
            // Generate random salt
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            // Hash password with salt
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());

            // Combine salt and hashed password
            byte[] combined = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);

            // Return Base64 encoded result
            return Base64.getEncoder().encodeToString(combined);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Verifies a password against a hashed password.
     * @param password the plain text password to verify
     * @param hashedPassword the hashed password with salt (Base64 encoded)
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        if (password == null || hashedPassword == null) {
            return false;
        }

        try {
            // Decode the combined salt and hash
            byte[] combined = Base64.getDecoder().decode(hashedPassword);

            // Extract salt (first 16 bytes)
            byte[] salt = new byte[SALT_LENGTH];
            System.arraycopy(combined, 0, salt, 0, SALT_LENGTH);

            // Extract hash (remaining bytes)
            byte[] storedHash = new byte[combined.length - SALT_LENGTH];
            System.arraycopy(combined, SALT_LENGTH, storedHash, 0, storedHash.length);

            // Hash the provided password with the extracted salt
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] computedHash = md.digest(password.getBytes());

            // Compare the hashes
            return MessageDigest.isEqual(storedHash, computedHash);
        } catch (Exception e) {
            System.err.println("Error verifying password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Generates a random password.
     * @param length the desired password length
     * @return a random password string
     */
    public static String generateRandomPassword(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("Password length must be at least 8 characters");
        }

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }

    /**
     * Validates password strength.
     * @param password the password to validate
     * @return true if password meets strength requirements, false otherwise
     */
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    /**
     * Gets password strength description.
     * @param password the password to evaluate
     * @return strength description string
     */
    public static String getPasswordStrength(String password) {
        if (password == null) return "Invalid";

        if (password.length() < 6) return "Very Weak";
        if (password.length() < 8) return "Weak";
        if (isPasswordStrong(password)) return "Strong";
        if (password.length() >= 8) return "Medium";

        return "Weak";
    }
}