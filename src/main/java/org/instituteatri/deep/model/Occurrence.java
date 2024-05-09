package org.instituteatri.deep.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "occurrence", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "occurrence_defendants",
            joinColumns = @JoinColumn(name = "occurrence_id"),
            inverseJoinColumns = @JoinColumn(name = "defendant_id"))
    private List<Defendant> defendants = new ArrayList<>();
}
