package com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings;

import com.jk.solutions.data_structures.health_care.plans_mgmt.entity.AccountPlanOrder;
import com.jk.solutions.data_structures.health_care.plans_mgmt.repository.AccountPlanOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

@Component
public class AccountPlanOrderDataSeederImpl implements AccountPlanOrderDataSeeder {

    @Autowired
    private AccountPlanOrderRepository accountPlanOrderRepository;

    @Override
    public String populateSyntheticAccountPlanOrders(String accountNumber, int count) {
        Random random = new Random();

        IntStream.range(0, count).forEach(i -> {
            AccountPlanOrder order = new AccountPlanOrder();
            order.setOrderId(UUID.randomUUID().toString());
            order.setId(order.getOrderId());
            order.setAccountNumber(accountNumber);
            order.setPlanId(order.getOrderId());
            order.setOrderLinesCount(random.nextInt(5) + 1);

            BigDecimal base = BigDecimal.valueOf(50 + random.nextInt(950));
            BigDecimal discount = base.multiply(BigDecimal.valueOf(random.nextDouble() * 0.2)).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal total = base.subtract(discount);

            order.setOrderCost(base);
            order.setOrderDiscount(discount);
            order.setOrderTotalCost(total);
            order.setOrderStatus("FULFILLED");
            order.setOrderFulfilledDate(LocalDate.now().minusDays(count - i));
            order.setOrderCanceledDate(null);

            accountPlanOrderRepository.save(order);
        });

        return "Seeded " + count + " orders for account " + accountNumber;
    }
}
