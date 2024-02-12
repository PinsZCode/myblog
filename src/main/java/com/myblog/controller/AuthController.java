package com.myblog.controller;

import com.myblog.entity.Role;
import com.myblog.entity.User;
import com.myblog.payload.LoginDto;
import com.myblog.payload.SignUpDto;
import com.myblog.repository.RoleRepository;
import com.myblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signIn")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto){
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsernameOremail(), loginDto.getPassword());
      Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
      SecurityContextHolder.getContext().setAuthentication(authenticate);
        return new ResponseEntity<>("user signed in successfully!.", HttpStatus.OK);
    }
    
    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto ){
        if (userRepository.existsByUsername(signUpDto.getUsername())){
            return new ResponseEntity<>("userName not avaiable",HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signUpDto.getEmail())){
            return new ResponseEntity<>("already exists",HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder().encode(signUpDto.getPassword()));

        Role role = roleRepository.findByName(signUpDto.getRole()).get();
        Set<Role> covertIntoSet = new HashSet<>();
        covertIntoSet.add(role);
        user.setRoles(covertIntoSet);
        userRepository.save(user);

        return new ResponseEntity<>("User register successfully",HttpStatus.CREATED);
    }
}
