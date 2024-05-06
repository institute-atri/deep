package org.instituteatri.deep.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    @OneToMany(mappedBy = "occurrence")
    private List<Message> messages = new ArrayList<>();
}
