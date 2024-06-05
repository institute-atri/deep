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
    private String dateOfBirth;
    private String idNumber;

    @OneToOne
    private Address address;

    @ManyToMany
    @JoinTable(name = "tb_occurrence_defendants",
            joinColumns = @JoinColumn(name = "defendant_id"),
            inverseJoinColumns = @JoinColumn(name = "occurrence_id"))
    private List<Occurrence> occurrences = new ArrayList<>();
}
