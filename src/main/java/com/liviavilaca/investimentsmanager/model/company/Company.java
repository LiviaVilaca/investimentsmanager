package com.liviavilaca.investimentsmanager.model.company;

import com.liviavilaca.investimentsmanager.model.action.Action;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ticker;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @OneToMany(mappedBy = "company", targetEntity = Action.class, fetch = FetchType.LAZY,  orphanRemoval = true)
    private List<Action> actions;

    @Column(columnDefinition = "boolean default true")
    private Boolean status;
}
