package study.oopdeliveryservice.v1.domain.shop;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.oopdeliveryservice.v1.domain.generic.money.Money;
import study.oopdeliveryservice.v1.domain.generic.money.Ratio;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_id")
    private Long id;

    private String name;
    private boolean open;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "min_order_amount"))
    private Money minOrderAmount; // 최소주문금액

    @Embedded
    @AttributeOverride(name = "rate", column = @Column(name = "commission_rate"))
    private Ratio commissionRate; // 수수료율

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "commission"))
    private Money commission = Money.ZERO; // 수수료

    // [조회 전용 양방향 연관관계]
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<Menu> menus = new ArrayList<>();

    public Shop(String name, boolean open, Money minOrderAmount) {
        this(name, open, minOrderAmount, Ratio.valueOf(0), Money.ZERO);
    }

    public Shop(String name, boolean open, Money minOrderAmount, Ratio commissionRate, Money commission) {
        this(null, name, open, minOrderAmount, commissionRate, commission);
    }

    @Builder
    public Shop(Long id, String name, boolean open, Money minOrderAmount, Ratio commissionRate, Money commission) {
        this.id = id;
        this.name = name;
        this.open = open;
        this.minOrderAmount = minOrderAmount;
        this.commissionRate = commissionRate;
        this.commission = commission;
    }

    public void addMenu(Menu menu) {
        menus.add(menu);
    }

    public boolean isValidOrderAmount(Money amount) {
        return amount.isGreaterThanOrEqual(minOrderAmount);
    }

    public void open() {
        this.open = true;
    }

    public void close() {
        this.open = false;
    }

    public void modifyCommissionRate(Ratio commissionRate) {
        this.commissionRate = commissionRate;
    }

    public void billCommissionFee(Money price) {
        commission = commission.plus(commissionRate.of(price));
    }
}
