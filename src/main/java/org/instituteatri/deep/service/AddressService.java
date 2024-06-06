package org.instituteatri.deep.service;

import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.dto.response.AddressDTO;
import org.instituteatri.deep.exception.BadRequestException;
import org.instituteatri.deep.model.Address;
import org.instituteatri.deep.repository.AddressRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressService.class);
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    public List<AddressDTO> getAll(){
        List<Address> addresses = addressRepository.findAll();
        List<AddressDTO> addressDTOList = new ArrayList<>();
        addresses.forEach(x -> addressDTOList.add(modelMapper.map(x,  AddressDTO.class)));
        return addressDTOList;
    }
    public AddressDTO getById(String id){
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Address not found!"));
        return modelMapper.map(address, AddressDTO.class);
    }
    @Transactional
    public AddressDTO save(AddressDTO addressDTO){
        Address address = modelMapper.map(addressDTO, Address.class);
        Address newAddress = addressRepository.save(address);
        LOGGER.info("Inserting {} to the database", address);
        return modelMapper.map(newAddress, AddressDTO.class);
    }
    public void delete(String id){
        LOGGER.info("Deleting ID: {} from de database", id);
        addressRepository.deleteById(id);
        LOGGER.info("Address deleted successfully");
    }
}
