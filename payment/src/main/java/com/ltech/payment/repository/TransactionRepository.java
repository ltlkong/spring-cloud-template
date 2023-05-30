package com.ltech.payment.repository;

import com.ltech.payment.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    @Modifying
    @Query("update Transaction t set t.status = :status where t.thirdPartyId = :thirdPartyId and t.thirdPartyType = :thirdPartyType")
    void updateStatusByThirdPartyIdAndThirdPartyProvider(String thirdPartyId ,String thirdPartyType, String status);

}
