package org.instituteatri.deep.repository;


import org.instituteatri.deep.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, String> {
}
