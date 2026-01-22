package guru.springframework.myspring7restmvc.services;

import guru.springframework.myspring7restmvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<CustomerDTO> findAll();
    Optional<CustomerDTO> findById(UUID id);
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customerDTO);
    Boolean deleteById(UUID customerId);
    Optional<CustomerDTO> patchById(UUID customerId, CustomerDTO customerDTO);
}
