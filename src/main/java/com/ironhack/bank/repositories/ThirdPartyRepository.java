package com.ironhack.bank.repositories;

import com.ironhack.bank.models.users.ThirdParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThirdPartyRepository extends JpaRepository<ThirdParty, Long> {


     List<ThirdParty> findByHashedKey(String hashedKey);

}
