package org.sgomez.test.springboot.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sgomez.test.springboot.app.exceptions.MoneyIsNotEnoughException;

import java.math.BigDecimal;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
