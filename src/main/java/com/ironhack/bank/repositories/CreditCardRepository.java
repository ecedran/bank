package com.ironhack.bank.repositories;

import com.ironhack.bank.models.Checking;
import com.ironhack.bank.models.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    List<CreditCard> findByInterestRateNextPaymentBefore(LocalDate date);

}
