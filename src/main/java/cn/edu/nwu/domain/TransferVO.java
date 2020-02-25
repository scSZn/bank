package cn.edu.nwu.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferVO {
     Integer type;
    BigDecimal amount;
   String oppositecard;
   String card;
}
