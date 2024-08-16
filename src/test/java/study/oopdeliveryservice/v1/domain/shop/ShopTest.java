package study.oopdeliveryservice.v1.domain.shop;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import study.oopdeliveryservice.v1.domain.generic.money.Money;
import study.oopdeliveryservice.v1.domain.generic.money.Ratio;

class ShopTest {

    @Test
    @DisplayName("최소주문금액 체크")
    void validOrderAmount() {
        // given
        Shop shop = new Shop("shop1", true, Money.wons(1000));

        // when
        boolean validOrderAmount1 = shop.isValidOrderAmount(Money.wons(900));
        boolean validOrderAmount2 = shop.isValidOrderAmount(Money.wons(1000));
        boolean validOrderAmount3 = shop.isValidOrderAmount(Money.wons(1100));

        // then
        assertThat(validOrderAmount1).isFalse();
        assertThat(validOrderAmount2).isTrue();
        assertThat(validOrderAmount3).isTrue();
    }

    @Test
    @DisplayName("수수료 부과 체크")
    void bill() {
        // given
        Shop shop = new Shop("shop1", true, Money.wons(1000), Ratio.valueOf(0.1), Money.ZERO);

        // when
        shop.billCommissionFee(Money.wons(1000));

        // then
        assertThat(shop.getCommission()).isEqualTo(Money.wons(100));
    }



}