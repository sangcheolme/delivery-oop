package study.oopdeliveryservice.v1.domain.order;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.oopdeliveryservice.v1.domain.generic.money.Money;
import study.oopdeliveryservice.v1.domain.shop.OptionSpecification;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "ORDER_OPTION")
public class OrderOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_option_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_option_group_id")
    private OrderOptionGroup orderOptionGroup;

    private String name;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price"))
    private Money price;

    public OrderOption(String name, Money price) {
        this(null, name, price);
    }

    @Builder
    public OrderOption(Long id, String name, Money price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public void addOrderOptionGroup(OrderOptionGroup orderOptionGroup) {
        this.orderOptionGroup = orderOptionGroup;
    }

    public boolean isSatisfiedBy(OptionSpecification optionSpecification) {
        return name.equals(optionSpecification.getName()) && price.equals(optionSpecification.getPrice());
    }
}
