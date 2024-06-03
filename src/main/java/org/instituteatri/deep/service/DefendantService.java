package org.instituteatri.deep.service;

import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.dto.response.DefendantDTO;
import org.instituteatri.deep.exception.BadRequestException;
import org.instituteatri.deep.model.Defendant;
import org.instituteatri.deep.repository.DefendantRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefendantService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OccurrenceService.class);
    private final DefendantRepository defendantRepository;
    private final ModelMapper modelMapper;


    public List<DefendantDTO> getAll(){
        List<Defendant> defendantDTOList = defendantRepository.findAll();
        List<DefendantDTO> defendantResponse = new ArrayList<>();
        defendantResponse.forEach(x -> defendantResponse.add(modelMapper.map(x, DefendantDTO.class)));
        return defendantResponse;
    }
    public DefendantDTO getById(String id){
        Defendant defendant = defendantRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Defendant not found!"));
        return modelMapper.map(defendant, DefendantDTO.class);
    }
    @Transactional
    public DefendantDTO save(DefendantDTO defendantDTO){
        Defendant defendant = modelMapper.map(defendantDTO, Defendant.class);
        LOGGER.info("Inserting {} to the database", defendant);
        Defendant newDefendant = defendantRepository.save(defendant);
        return modelMapper.map(newDefendant, DefendantDTO.class);
    }
    public void delete(String id){
        LOGGER.info("Deleting ID: {} from the database", id);
        defendantRepository.deleteById(id);
        LOGGER.info("successfully deleted");
    }


}
