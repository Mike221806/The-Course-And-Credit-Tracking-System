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
