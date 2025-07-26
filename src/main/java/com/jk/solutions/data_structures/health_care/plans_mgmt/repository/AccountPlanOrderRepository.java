package com.jk.solutions.data_structures.health_care.plans_mgmt.repository;

import com.jk.solutions.data_structures.health_care.plans_mgmt.entity.AccountPlanOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

@Repository
public interface AccountPlanOrderRepository extends JpaRepository<AccountPlanOrder, String> {

    // Custom finder method to retrieve orders by account number
    List<AccountPlanOrder> findAllByAccountNumber(String accountNumber);

    @Query("SELECT o.orderTotalCost FROM AccountPlanOrder o WHERE o.accountNumber = :accountNumber")
    List<BigDecimal> findOrderTotalCostsByAccountNumberOOrderByCreatedAt(@Param("accountNumber") String accountNumber);

    @Query("SELECT o.orderTotalCost FROM AccountPlanOrder o WHERE o.accountNumber = :accountNumber ORDER BY o.createdAt")
    Stream<BigDecimal> streamOrderTotalCostsByAccountNumberOrderByCreatedAt(@Param("accountNumber") String accountNumber);


}
