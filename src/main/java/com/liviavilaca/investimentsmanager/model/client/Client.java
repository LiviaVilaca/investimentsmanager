package com.liviavilaca.investimentsmanager.model.client;

import com.liviavilaca.investimentsmanager.model.acquisition.Acquisition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private Integer age;

    @OneToMany(mappedBy = "client", targetEntity = Acquisition.class, fetch = FetchType.LAZY,  orphanRemoval = true)
    private List<Acquisition> acquisitions;
}
