package com.jk.solutions.data_structures.health_care.plans_mgmt.repository;

import com.jk.solutions.data_structures.health_care.plans_mgmt.entity.AccountProductEligibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AccountProductEligibilityRepository extends JpaRepository<AccountProductEligibility, String> {

    List<AccountProductEligibility> findByAccountNumber(String accountNumber);

    @Modifying
    @Query("UPDATE AccountProductEligibility a SET a.isEligible = NOT a.isEligible, a.eligibilityReason = 'Toggled by DB', a.lastEvaluatedDate = :evalDate, a.eligibilityVersion = :version WHERE a.accountNumber = :accountNbr")
    int bulkToggleEligibilityAndUpdate(@Param("accountNbr") String accountNbr,
                                       @Param("evalDate") LocalDate evalDate,
                                       @Param("version") String version);

    @Modifying
    @Query("UPDATE AccountProductEligibility a SET a.isEligible = NOT a.isEligible, a.eligibilityVersion = :version WHERE a.accountNumber = :accountNbr")
    int dbSideEligibilityInversion(@Param("accountNbr") String accountNbr,
                                   @Param("version") String version);

    @Query("SELECT a.id FROM AccountProductEligibility a WHERE a.accountNumber = :accountNbr")
    List<Long> findIdsByAccountNumber(@Param("accountNbr") String accountNbr);

    @Modifying
    @Query("UPDATE AccountProductEligibility a SET a.isEligible = NOT a.isEligible WHERE a.id = :id")
    void toggleEligibilityFlagById(@Param("id") Long id);

}
