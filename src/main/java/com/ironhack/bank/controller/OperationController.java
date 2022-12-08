package com.ironhack.bank.controller;

import com.ironhack.bank.dtos.TransferDTO;
import com.ironhack.bank.services.OperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class OperationController {

    @Autowired
    OperationsService operationsService;

    @PostMapping("/transfer-money")
    @ResponseStatus(HttpStatus.CREATED)
    public void transferMoney(@RequestBody TransferDTO transferDTO) {
        operationsService.transferMoney(transferDTO);
    }

}
