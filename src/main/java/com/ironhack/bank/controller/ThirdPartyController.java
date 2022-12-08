package com.ironhack.bank.controller;

import com.ironhack.bank.dtos.ThirdPartyOpDTO;
import com.ironhack.bank.dtos.TransferDTO;
import com.ironhack.bank.services.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/third-party")
public class ThirdPartyController {

    @Autowired
    ThirdPartyService thirdPartyService;

    @PostMapping("/send-money")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendMoney(@RequestBody ThirdPartyOpDTO thirdPartyOpDTO) {
        thirdPartyService.sendMoney(thirdPartyOpDTO);
    }

    @PostMapping("/withdraw-money")
    @ResponseStatus(HttpStatus.CREATED)
    public void withdrawMoney(@RequestBody ThirdPartyOpDTO thirdPartyOpDTO) {
        thirdPartyService.withdrawMoney(thirdPartyOpDTO);
    }
}
