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
import study.oopdeliveryservice.v1.domain.shop.Menu;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "ORDER_LINE_ITEM")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_line_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private String name;
    private int count;

    @OneToMany(mappedBy = "orderLineItem", cascade = CascadeType.ALL)
    private List<OrderOptionGroup> orderOptionGroups = new ArrayList<>();

    public OrderLineItem(Menu menu, String name, int count, List<OrderOptionGroup> orderOptionGroups) {
        this(null, menu, name, count, orderOptionGroups);
    }

    @Builder
    public OrderLineItem(Long id, Menu menu, String name, int count, List<OrderOptionGroup> orderOptionGroups) {
        this.id = id;
        this.menu = menu;
        this.name = name;
        this.count = count;
        for (OrderOptionGroup orderOptionGroup : orderOptionGroups) {
            addOrderOptionGroup(orderOptionGroup);
        }
    }

    // [연관관계 편의 메서드]
    public void addOrderOptionGroup(OrderOptionGroup orderOptionGroup) {
        orderOptionGroups.add(orderOptionGroup);
        orderOptionGroup.addOrderLineItem(this);
    }

    public Money calculatePrice() {
        return Money.sum(orderOptionGroups, OrderOptionGroup::calculatePrice);
    }

    public void addOrder(Order order) {
        this.order = order;
    }
}
