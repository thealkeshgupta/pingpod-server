package com.thealkeshgupta.PingPod.controller;

import com.thealkeshgupta.PingPod.model.User;
import com.thealkeshgupta.PingPod.repository.UserRepository;
import com.thealkeshgupta.PingPod.security.jwt.JwtUtils;
import com.thealkeshgupta.PingPod.security.request.LoginRequest;
import com.thealkeshgupta.PingPod.security.request.SignUpRequest;
import com.thealkeshgupta.PingPod.security.response.MessageResponse;
import com.thealkeshgupta.PingPod.security.response.UserInfoResponse;
import com.thealkeshgupta.PingPod.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new MessageResponse("Error: Username is already taken"), HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new MessageResponse("Error: Username is already taken"), HttpStatus.BAD_REQUEST);
        }

        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getName(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword())
        );

        user.setJoinedOn(LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));

        userRepository.save(user);

        return new ResponseEntity<>(new MessageResponse("User registered successfully"), HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        UserInfoResponse response = new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), jwtCookie.toString());

        return ResponseEntity.ok().header(
                HttpHeaders.SET_COOKIE, jwtCookie.toString()
        ).body(response);
    }

}
