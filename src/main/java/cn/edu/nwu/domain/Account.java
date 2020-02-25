package cn.edu.nwu.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity
@Table(name="account")
public class Account {
    @Id
    String card;
//    Double amount;

    BigDecimal amount;
    String amountStr;

    public Account(String card, BigDecimal amount) {
        this.card = card;
        this.amount = amount;
    }

    public Account(String card, String amountStr) {
        this.card = card;
        this.amountStr = amountStr;
    }
}
