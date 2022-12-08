package com.ironhack.bank.repositories;

import com.ironhack.bank.models.Account;
import com.ironhack.bank.models.AccountStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountStatementRepository extends JpaRepository<AccountStatement, Long> {

    List<AccountStatement> findByAccount(Account account);

}
