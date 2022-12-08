package com.ironhack.bank.services;

import com.ironhack.bank.models.users.AccountHolder;
import com.ironhack.bank.models.users.User;
import com.ironhack.bank.repositories.AccountHolderRepository;
import com.ironhack.bank.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    AccountHolderRepository accountHolderRepository;

    // add a new Account Holder, user kind

    public AccountHolder addUser(AccountHolder accountHolder) {
        return accountHolderRepository.save(accountHolder);
    }

}
