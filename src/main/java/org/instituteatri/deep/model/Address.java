package org.instituteatri.deep.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Table(name = "tb_address")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String streetAddress;
    private String houseNumber;
    private String neighborhood;
    private String city;
    private String zipCode;
    private String state;

    @OneToOne
    private Occurrence occurrence;
}
