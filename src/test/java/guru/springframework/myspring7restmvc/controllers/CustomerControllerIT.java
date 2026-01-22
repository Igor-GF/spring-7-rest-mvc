package guru.springframework.myspring7restmvc.controllers;

import guru.springframework.myspring7restmvc.entities.Customer;
import guru.springframework.myspring7restmvc.mappers.CustomerMapper;
import guru.springframework.myspring7restmvc.model.CustomerDTO;
import guru.springframework.myspring7restmvc.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Test
    void test_update_customer() {
        Customer customer = customerRepository.findAll().getFirst();
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        final String customerName = "Customer UPDATE";
        customerDTO.setCustomerName(customerName);

        ResponseEntity responseEntity = customerController.handleUpdateCustomer(customer.getId(), customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getCustomerName()).isEqualTo(customerName);
    }

    @Rollback
    @Transactional
    @Test
    void test_save_customer() {
        CustomerDTO dto = CustomerDTO.builder()
                .customerName("New customer")
                .build();

        ResponseEntity responseEntity = customerController.handleCreateCustomer(dto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] split = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUuid = UUID.fromString(split[split.length - 1]);

        Customer customer = customerRepository.findById(savedUuid).get();
        assertThat(customer).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    void test_delete_customer() {
        Customer customer = customerRepository.findAll().getFirst();

        ResponseEntity responseEntity = customerController.handleDeleteCustomer(customer.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(customerRepository.findById(customer.getId())).isEmpty();
    }

    @Test
    void test_delete_customer_returns_not_found() {
        assertThrows(NotFoundException.class, () -> {
            customerController.handleDeleteCustomer(UUID.randomUUID());
        });
    }

    @Test
    void test_get_all_customers() {
        List<CustomerDTO> customers = customerController.getCustomers();

        assertThat(customers.size()).isEqualTo(3);
    }

    @Test
    void test_a_get_all_customers_returns_empty_list() {
        customerRepository.deleteAll();
        List<CustomerDTO> dtos = customerController.getCustomers();

        assertThat(dtos.size()).isEqualTo(0);
    }

    @Test
    void test_get_customer_by_id() {
        Customer customer = customerRepository.findAll().getFirst();
        CustomerDTO dto = customerController.getCustomerById(customer.getId());

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(customer.getId());
    }

    @Test
    void test_get_customer_by_id_returns_not_found() {
        assertThrows(NotFoundException.class, () -> {
            customerController.getCustomerById(UUID.randomUUID());
        });
    }
}