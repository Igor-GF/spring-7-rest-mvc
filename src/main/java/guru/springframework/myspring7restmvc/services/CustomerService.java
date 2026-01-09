package guru.springframework.myspring7restmvc.services;

import guru.springframework.myspring7restmvc.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<Customer> findAll();
    Optional<Customer> findById(UUID id);
    Customer saveCustomer(Customer customer);
    void updateCustomerById(UUID customerId, Customer customer);
    void deleteById(UUID customerId);
    void patchById(UUID customerId, Customer customer);
}
