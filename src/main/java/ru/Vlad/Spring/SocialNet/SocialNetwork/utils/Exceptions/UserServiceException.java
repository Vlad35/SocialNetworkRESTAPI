package ru.Vlad.Spring.SocialNet.SocialNetwork.utils.Exceptions;

public class UserServiceException extends RuntimeException {
    public UserServiceException(String message) {
        super(message);
    }
}
