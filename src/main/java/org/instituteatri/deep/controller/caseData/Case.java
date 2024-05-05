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

    private Address address;

    private String processNumber;
    private String expertReports;

    public Case(CaseDto inquiry) {
        this.typeOfCrime = inquiry.typeOfCrime();
        this.date = inquiry.date();
        this.defendant.add(inquiry.defendant());
        this.expertReports = inquiry.expertReports();
        this.processNumber = inquiry.processNumber();
        this.address = inquiry.defendant().getAddress();
    }

    public void updateCase(CaseDto inquiry) {
        if(inquiry.typeOfCrime() != null){
            this.typeOfCrime = inquiry.typeOfCrime();
        }
        if(inquiry.date() != null){
            this.date = inquiry.date();
        }
        if(inquiry.defendant() != null){
            this.defendant.add(inquiry.defendant());
        }
        if(inquiry.defendant() != null){
            this.address = inquiry.defendant().getAddress();
        }
        if(inquiry.processNumber() != null){
            this.processNumber = inquiry.processNumber();
        }
        if(inquiry.expertReports() != null){
            this.expertReports = inquiry.expertReports();
        }
    }
}