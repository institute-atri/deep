package org.instituteatri.deep.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_defendant")
@Entity
public class Defendant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String birthday;
    @CPF
    private String idNumber;

    @OneToOne
    private Address address;

    @ManyToMany
    private List<Occurrence> occurrences = new ArrayList<>();
}
