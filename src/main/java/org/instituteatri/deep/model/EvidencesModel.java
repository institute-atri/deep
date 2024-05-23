package org.instituteatri.deep.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "evidences")
@Entity
public class EvidencesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
        private String id;
        private String caseId;
        private String name;
        private String description;
        private Instant createdAt;
        private String Author;


}
