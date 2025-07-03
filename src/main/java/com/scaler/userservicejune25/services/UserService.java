package com.scaler.userservicejune25.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.userservicejune25.dtos.SendEmailEventDto;
import com.scaler.userservicejune25.exceptions.InvalidTokenException;
import com.scaler.userservicejune25.models.Token;
import com.scaler.userservicejune25.models.User;
import com.scaler.userservicejune25.repositories.TokenRepository;
import com.scaler.userservicejune25.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public UserService(UserRepository userRepository,
                       TokenRepository tokenRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       KafkaTemplate kafkaTemplate,
                       ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public Token login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            //redirect the user to the signup page.
            return null;
        }

        User user = optionalUser.get();

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        Token token = new Token();
        token.setUser(user);

        LocalDate localDate = LocalDate.now().plusDays(30);
        Date expiryDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_MONTH, 30);

        token.setExpiryAt(expiryDate);
        token.setValue(RandomStringUtils.randomAlphanumeric(128));

        return tokenRepository.save(token);
    }

    public User signUp(String name, String email, String password) throws JsonProcessingException {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            //redirect the user to the login page.
        }

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        user = userRepository.save(user);

        SendEmailEventDto eventDto = new SendEmailEventDto();
        eventDto.setEmail(email);
        eventDto.setSubject("Welcome to Scaler!");
        eventDto.setBody("Welcome to Scaler!");

        kafkaTemplate.send(
                "sendEmailEvent",
                objectMapper.writeValueAsString(eventDto)
        );

        return user;
    }

    public User validateToken(String tokenValue) throws InvalidTokenException {
        //Token should be present in th database, and expiry time > current time.

        Optional<Token> tokenOptional = tokenRepository.findByValueAndExpiryAtAfter(tokenValue, new Date());

        if (tokenOptional.isEmpty()) {
            //Token is invalid.
            throw new InvalidTokenException("Token is invalid or expired, Please try to login again.");
        }

        return tokenOptional.get().getUser();
    }
}
