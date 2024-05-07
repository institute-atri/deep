package org.instituteatri.deep.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.instituteatri.deep.model.Address;
import org.instituteatri.deep.model.Defendant;
import org.instituteatri.deep.model.TypeOfCrime;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OccurrenceRequestDTO {

    private String id;

    private String name;
    private String description;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private String date;

    @Enumerated(EnumType.STRING)
    private TypeOfCrime typeOfCrime;

    private Address address;

    private List<Defendant> defendant;
}