package com.ironhack.bank.controller;

import com.ironhack.bank.models.users.AccountHolder;
import com.ironhack.bank.models.users.Role;
import com.ironhack.bank.repositories.AccountHolderRepository;
import com.ironhack.bank.repositories.RoleRepository;
import com.ironhack.bank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @PostMapping("/create-user")
    public void createAccountHolder(@RequestBody AccountHolder accountHolder) {
        String encodedPassword = passwordEncoder.encode(accountHolder.getPassword());
        accountHolder.setPassword(encodedPassword);
        accountHolder = accountHolderRepository.save(accountHolder);
        Role role = roleRepository.save(new Role("USER", accountHolder));
    }

}
