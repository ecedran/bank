package com.ironhack.bank.controller;

import com.ironhack.bank.dtos.ThirdPartyOpDTO;
import com.ironhack.bank.dtos.TransferDTO;
import com.ironhack.bank.repositories.ThirdPartyRepository;
import com.ironhack.bank.services.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/third-party")
public class ThirdPartyController {

    @Autowired
    ThirdPartyService thirdPartyService;
    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @PostMapping("/send-money")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendMoney(@RequestBody ThirdPartyOpDTO thirdPartyOpDTO, @RequestHeader(value = "hashkey") String hashkey) {
        if (thirdPartyRepository.findByHashedKey(hashkey).isEmpty()) {
            throw new IllegalArgumentException("The hashkey is wrong");
        }
        thirdPartyService.sendMoney(thirdPartyOpDTO, hashkey);
    }

    @PostMapping("/withdraw-money")
    @ResponseStatus(HttpStatus.CREATED)
    public void withdrawMoney(@RequestBody ThirdPartyOpDTO thirdPartyOpDTO, @RequestHeader(value = "hashkey") String hashkey) {
        if (thirdPartyRepository.findByHashedKey(hashkey).isEmpty()) {
            throw new IllegalArgumentException("The hashkey is wrong");
        }
        thirdPartyService.withdrawMoney(thirdPartyOpDTO, hashkey);
    }
}
