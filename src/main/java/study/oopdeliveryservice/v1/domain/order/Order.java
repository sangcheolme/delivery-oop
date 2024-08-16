package study.oopdeliveryservice.v1.domain.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import study.oopdeliveryservice.v1.domain.shop.Shop;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "ORDERS")
public class Order {

    public enum OrderStatus {ORDERED, PAYED, DELIVERED}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    private LocalDateTime orderedTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order(Long userId, Shop shop, List<OrderLineItem> orderLineItems) {
        this(null, userId, shop, LocalDateTime.now(), null, orderLineItems);
    }

    @Builder
    public Order(Long id, Long userId, Shop shop, LocalDateTime orderedTime, OrderStatus orderStatus,
        List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.userId = userId;
        this.shop = shop;
        this.orderedTime = orderedTime;
        this.orderStatus = orderStatus;
        for (OrderLineItem orderLineItem : orderLineItems) {
            addOrderLineItem(orderLineItem);
        }
    }

    // [연관관계 편의 메서드]
    public void addOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
        orderLineItem.addOrder(this);
    }

    public void place() {
        ordered();
    }

    public void ordered() {
        this.orderStatus = OrderStatus.ORDERED;
    }

    public void payed() {
        this.orderStatus = OrderStatus.PAYED;
    }

    public void delivered() {
        this.orderStatus = OrderStatus.DELIVERED;
        this.shop.billCommissionFee(calculatePrice());
    }

    public Money calculatePrice() {
        return Money.sum(orderLineItems, OrderLineItem::calculatePrice);
    }
}
