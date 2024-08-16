package study.oopdeliveryservice.v1.domain.generic.money;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RatioTest {

    @Test
    @DisplayName("퍼센트 계산")
    void percent() {
        // given
        Ratio tenPercent = new Ratio(0.1);
        Money thousandWon = Money.wons(1000);

        // then
        assertThat(tenPercent.of(thousandWon)).isEqualTo(Money.wons(100));
    }


}