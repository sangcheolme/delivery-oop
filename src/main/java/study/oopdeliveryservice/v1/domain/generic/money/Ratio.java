package study.oopdeliveryservice.v1.domain.generic.money;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Ratio {

    private double rate;

    public static Ratio valueOf(double rate) {
        return new Ratio(rate);
    }

    public Money of(Money price) {
        return price.times(rate);
    }
}
