package com.ironhack.bank.models.users;

import jakarta.persistence.Entity;

@Entity
public class Admin extends User {

    public Admin(String username, String password) {
        super(username, password);
    }

    public Admin() {
    }

}
