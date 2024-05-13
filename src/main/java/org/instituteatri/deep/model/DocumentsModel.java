package org.instituteatri.deep.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class DocumentsModel {
    @Id
        private String id;
        private String name;
        private String description;
        private String createdAt;
        private String Author;
    }

