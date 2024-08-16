package study.oopdeliveryservice.v1.domain.shop;

import java.util.ArrayList;
import java.util.Arrays;
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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "OPTION_GROUP_SPEC")
public class OptionGroupSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_group_spec_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private String name;
    private boolean exclusive;
    private boolean basic;

    @OneToMany(mappedBy = "optionGroupSpec", cascade = CascadeType.ALL)
    private List<OptionSpecification> optionSpecs = new ArrayList<>();

    public static OptionGroupSpecification basic(String name, boolean exclusive, OptionSpecification... options) {
        return new OptionGroupSpecification(name, exclusive, true, options);
    }

    public static OptionGroupSpecification additive(String name, boolean exclusive, OptionSpecification... options) {
        return new OptionGroupSpecification(name, exclusive, false, options);
    }

    public OptionGroupSpecification(String name, boolean exclusive, boolean basic, OptionSpecification... options) {
        this(null, name, exclusive, basic, Arrays.asList(options));
    }

    @Builder
    public OptionGroupSpecification(Long id, String name, boolean exclusive, boolean basic,
        List<OptionSpecification> options) {
        this.id = id;
        this.name = name;
        this.exclusive = exclusive;
        this.basic = basic;
        for (OptionSpecification optionSpec : options) {
            addOptionSpec(optionSpec);
        }
    }

    // [연관관계 편의 메서드]
    public void addOptionSpec(OptionSpecification optionSpec) {
        this.optionSpecs.add(optionSpec);
        optionSpec.addOptionGroupSpec(this);
    }

    public void addMenu(Menu menu) {
        this.menu = menu;
    }
}
