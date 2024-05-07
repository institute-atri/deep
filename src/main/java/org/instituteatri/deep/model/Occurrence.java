package org.instituteatri.deep.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_occurrence")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Occurrence {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private String date;

    @Enumerated(EnumType.STRING)
    private TypeOfCrime typeOfCrime;

    @OneToOne
    private Address address;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "defendants_occurrences", joinColumns = @JoinColumn(name = "case_id"), inverseJoinColumns = @JoinColumn(name = "defendant_id"))
    @JsonIgnore
    private List<Defendant> defendant = new ArrayList<>();

    @OneToMany(mappedBy = "occurrence")
    private List<Message> messages = new ArrayList<>();
}