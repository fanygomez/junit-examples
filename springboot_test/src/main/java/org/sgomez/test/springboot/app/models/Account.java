package org.sgomez.test.springboot.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sgomez.test.springboot.app.exceptions.MoneyIsNotEnoughException;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account {
    private Long id;
    private String person;
    private BigDecimal balance;

    public void debit(BigDecimal amount){
        BigDecimal newBalance = this.balance.subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0){
            throw new MoneyIsNotEnoughException("Money is not enough");
        }
        this.balance = newBalance;
    }
    public void credit(BigDecimal amount){
        this.balance = this.balance.add(amount);
    }
}
