package com.gb02.syumsvc.utils;

/**
 * Constants for common error messages used across controllers.
 * Centralizes error message strings to improve maintainability and consistency.
 */
public class ErrorMessages {
    
    // Authentication and session errors
    public static final String INVALID_SESSION_TOKEN = "Invalid session token.";
    public static final String SESSION_EXPIRED = "Session has expired.";
    
    // User-related errors
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_NOT_AUTHORIZED_MODIFY = "You are not authorized to modify this user's data.";
    public static final String USER_NOT_AUTHORIZED_DELETE = "You are not authorized to delete this user's account.";
    
    // Registration and login errors
    public static final String EMAIL_ALREADY_REGISTERED = "This email is already registered.";
    public static final String USERNAME_ALREADY_REGISTERED = "This username is already registered.";
    public static final String INVALID_CREDENTIALS = "Invalid credentials. Please check your username/email and password.";
    
    // Generic errors
    public static final String UNEXPECTED_ERROR_REGISTRATION = "An unknown error occurred during registration.";
    public static final String UNEXPECTED_ERROR_USER_UPDATE = "Unexpected error occurred while updating user data.";
    public static final String UNEXPECTED_ERROR_USER_FETCH = "Unexpected error occurred while fetching user data.";
    public static final String UNEXPECTED_ERROR_USER_DELETE = "Unexpected error occurred while deleting user account.";
    public static final String UNEXPECTED_ERROR_AUTHENTICATION = "An unexpected error occurred during authentication.";
    public static final String UNEXPECTED_ERROR_LOGOUT = "An unexpected error occurred during logout.";
    public static final String UNEXPECTED_ERROR_LINK_ARTIST = "Unexpected error occurred while linking artist to user.";
    
    // Favorites errors
    public static final String UNEXPECTED_ERROR_GET_FAVORITES = "Unexpected error occurred while fetching favorites.";
    public static final String UNEXPECTED_ERROR_ADD_FAVORITE = "Unexpected error occurred while adding favorite.";
    public static final String UNEXPECTED_ERROR_DELETE_FAVORITE = "Unexpected error occurred while deleting favorite.";
    public static final String FAV_NOT_FOUND = "Favorite not found.";
    public static final String FAV_ALREADY_EXISTS = "Favorite already exists.";
    
    private ErrorMessages() {
        // Private constructor to prevent instantiation
    }
}
