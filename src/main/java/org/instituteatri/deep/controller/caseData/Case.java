package org.instituteatri.deep.controller.caseData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.instituteatri.deep.controller.dto.CaseDto;

import java.util.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "cases")
@Entity(name = "case")
@JsonIgnoreProperties({"cases"})
public class Case {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeOfCrime typeOfCrime;


    private String date;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "defendants_cases", joinColumns = @JoinColumn(name = "case_id"), inverseJoinColumns = @JoinColumn(name = "defendant_id"))
    @JsonIgnore
    private List<Defendant> defendant = new ArrayList<>();

    private Adress adress;

    private String processNumber;
    private String expertReports;

    public Case(CaseDto newCase) {
        this.typeOfCrime = newCase.typeOfCrime();
        this.date = newCase.date();
        this.defendant.add(newCase.defendant());
        this.expertReports = newCase.expertReports();
        this.processNumber = newCase.processNumber();
        this.adress = newCase.defendant().getAdress();
    }

    public void updateCase(CaseDto newCase) {
        if(newCase.typeOfCrime() != null){
            this.typeOfCrime = newCase.typeOfCrime();
        }
        if(newCase.date() != null){
            this.date = newCase.date();
        }
        if(newCase.defendant() != null){
            this.defendant.add(newCase.defendant());
        }
        if(newCase.defendant() != null){
            this.adress = newCase.defendant().getAdress();
        }
        if(newCase.processNumber() != null){
            this.processNumber = newCase.processNumber();
        }
        if(newCase.expertReports() != null){
            this.expertReports = newCase.expertReports();
        }
    }
}