package guru.springframework.myspring7restmvc.mappers;

import guru.springframework.myspring7restmvc.entities.Customer;
import guru.springframework.myspring7restmvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO dto);
    CustomerDTO customerToCustomerDTO(Customer customer);
}
