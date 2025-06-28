package com.scaler.userservicejune25.controllers;

import com.scaler.userservicejune25.dtos.LoginRequestDto;
import com.scaler.userservicejune25.dtos.SignupRequestDto;
import com.scaler.userservicejune25.dtos.TokenDto;
import com.scaler.userservicejune25.dtos.UserDto;
import com.scaler.userservicejune25.exceptions.InvalidTokenException;
import com.scaler.userservicejune25.models.Token;
import com.scaler.userservicejune25.models.User;
import com.scaler.userservicejune25.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequestDto requestDto) {
        Token token = userService.login(
                requestDto.getEmail(),
                requestDto.getPassword()
        );

        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        TokenDto tokenDto = TokenDto.from(token);

        return new ResponseEntity<>(tokenDto, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public UserDto signup(@RequestBody SignupRequestDto requestDto) {
        User user = userService.signUp(
                requestDto.getName(),
                requestDto.getEmail(),
                requestDto.getPassword()
        );

        return UserDto.from(user);
    }

    @GetMapping("/validate/{tokenValue}")
    public ResponseEntity<UserDto> validateToken(@PathVariable("tokenValue") String tokenValue) throws InvalidTokenException {
        User user = userService.validateToken(tokenValue);

        if (user == null) {
            //Token is invalid or expired
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        //Token is valid.
        return new ResponseEntity<>(
                UserDto.from(user),
                HttpStatus.OK
        );
    }

//    private UserDto from(User user) {
//        if (user == null) {
//            return null;
//        }
//
//        UserDto userDto = new UserDto();
//        userDto.setName(user.getName());
//        userDto.setEmail(user.getEmail());
//        userDto.setUserId(user.getId());
//        userDto.setRoles(user.getRoles());
//
//        return userDto;
//    }

    //TODO - logout, forgot password....
}
