package com.ironhack.bank.repositories;

import com.ironhack.bank.models.CreditCard;
import com.ironhack.bank.models.Saving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SavingRepository extends JpaRepository<Saving, Long> {

    List<Saving> findByInterestRateNextPaymentBefore(LocalDate date);

    List<Saving> findByMinimumBalanceCheckFalse();

    List<Saving> findByMinimumBalanceCheckTrue();


}
