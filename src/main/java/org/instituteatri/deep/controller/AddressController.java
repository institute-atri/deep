package org.instituteatri.deep.controller;

import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.dto.response.AddressDTO;
import org.instituteatri.deep.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressController.class);
    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAll(){
        List<AddressDTO> addressDTO = addressService.getAll();
        LOGGER.info("Request to get all addresses received");
        return ResponseEntity.ok(addressDTO);
    }
    @GetMapping("search/{id}")
    public ResponseEntity<AddressDTO> getById(@PathVariable String id){
        AddressDTO addressDTO = addressService.getById(id);
        LOGGER.info("Request to get address by id received");
        return ResponseEntity.ok(addressDTO);
    }
    @PostMapping("/create")
    public ResponseEntity<AddressDTO> create (@RequestBody AddressDTO request){
        AddressDTO addressDTO = addressService.saveAddress(request);
        LOGGER.info("Request to create a new address received");
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(addressDTO);
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<AddressDTO> delete(@PathVariable String id){
        addressService.deleteAddress(id);
        LOGGER.info("Request to delete an address");
        return ResponseEntity.noContent().build();
    }


}
