package com.liviavilaca.investimentsmanager.model.action;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liviavilaca.investimentsmanager.model.acquisition.Acquisition;
import com.liviavilaca.investimentsmanager.model.company.Company;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private BigDecimal totalSpent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acquisition_id", referencedColumnName = "id")
    @JsonBackReference
    private Acquisition acquisition;

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) { return this.toString();
        }
    }

    @Override
    public boolean equals(Object o) {
        return ((Action)o).toString().equals(this.toString());
    }

}
