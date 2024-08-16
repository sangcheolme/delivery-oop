package study.oopdeliveryservice.v1.domain.order;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.oopdeliveryservice.v1.domain.generic.money.Money;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "ORDER_OPTION_GROUP")
public class OrderOptionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_option_group_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_line_item_id")
    private OrderLineItem orderLineItem;

    private String name;

    @OneToMany(mappedBy = "orderOptionGroup", cascade = CascadeType.ALL)
    private List<OrderOption> orderOptions = new ArrayList<>();

    public OrderOptionGroup(String name, List<OrderOption> orderOptions) {
        this(null, name, orderOptions);
    }

    @Builder
    public OrderOptionGroup(Long id, String name, List<OrderOption> orderOptions) {
        this.id = id;
        this.name = name;
        for (OrderOption orderOption : orderOptions) {
            addOrderOption(orderOption);
        }
    }

    // [연관관계 편의 메서드]
    public void addOrderOption(OrderOption orderOption) {
        orderOptions.add(orderOption);
        orderOption.addOrderOptionGroup(this);
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItem = orderLineItem;
    }

    public Money calculatePrice() {
        return Money.sum(orderOptions, OrderOption::getPrice);
    }
}
