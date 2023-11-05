package ru.Vlad.Spring.SocialNet.SocialNetwork.utils.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.Vlad.Spring.SocialNet.SocialNetwork.DTO.UserLoginDTO;
import ru.Vlad.Spring.SocialNet.SocialNetwork.DTO.UserRegistrationDTO;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Services.UserService;

@Component
public class UserRegistrationDtoValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserRegistrationDtoValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserLoginDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRegistrationDTO userRegistrationDTO = (UserRegistrationDTO) target;

        if(userService.getUserByName(userRegistrationDTO.getUsername()) == null) {
            return;
        }else {
            errors.rejectValue("username","","Nickname is used by Someone else");
            throw new IllegalArgumentException("Username is used!");
        }
    }
}