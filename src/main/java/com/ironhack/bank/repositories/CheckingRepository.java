package com.ironhack.bank.repositories;

import com.ironhack.bank.models.Checking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CheckingRepository extends JpaRepository<Checking, Long> {


    List<Checking> findByMaintenanceFeeNextPaymentBefore(LocalDate date);

    List<Checking> findByMinimumBalanceCheckFalse();

    List<Checking> findByMinimumBalanceCheckTrue();

}
