package study.oopdeliveryservice.v1.domain.shop;

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

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    private String name;
    private String description;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<OptionGroupSpecification> optionGroupSpecs = new ArrayList<>();

    @Builder
    public Menu(Long id, Shop shop, String name, String description, OptionGroupSpecification basic,
        List<OptionGroupSpecification> additives) {
        this.id = id;
        this.name = name;
        this.description = description;

        addShop(shop);

        addOptionGroupSpec(basic); // 기본 옵션
        for (OptionGroupSpecification additive : additives) {
            addOptionGroupSpec(additive); // 추가 옵션
        }
    }

    // [연관관계 편의 메서드]
    public void addOptionGroupSpec(OptionGroupSpecification optionGroupSpec) {
        optionGroupSpecs.add(optionGroupSpec);
        optionGroupSpec.addMenu(this);
    }

    // [연관관계 편의 메서드]
    public void addShop(Shop shop) {
        this.shop = shop;
        shop.addMenu(this);
    }
}
