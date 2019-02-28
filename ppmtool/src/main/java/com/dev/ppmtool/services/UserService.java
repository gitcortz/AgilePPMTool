package com.dev.ppmtool.services;

import com.dev.ppmtool.domain.User;
import com.dev.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser){
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));


        //Username has to be unique(exception)
        //Make sure password and confirm password match
        //We don't persist or show confirm password
        return userRepository.save(newUser);
    }


}
