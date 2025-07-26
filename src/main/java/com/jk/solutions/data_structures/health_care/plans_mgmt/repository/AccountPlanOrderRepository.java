package com.jk.solutions.data_structures.health_care.plans_mgmt.repository;

import com.jk.solutions.data_structures.health_care.plans_mgmt.entity.AccountPlanOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountPlanOrderRepository extends JpaRepository<AccountPlanOrder, Long> {
}
