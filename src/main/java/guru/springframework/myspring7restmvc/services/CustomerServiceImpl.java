package guru.springframework.myspring7restmvc.services;

import guru.springframework.myspring7restmvc.model.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        customerMap = new HashMap<>();
    }

    @Override
    public List<CustomerDTO> findAll() {
        log.debug("in the service - find all customers method");
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<CustomerDTO> findById(UUID id) {
        return Optional.of(customerMap.get(id));
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        CustomerDTO savedCustomerDTO = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .version(2)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .customerName(customerDTO.getCustomerName())
                .build();

        customerMap.put(savedCustomerDTO.getId(), savedCustomerDTO);
        return savedCustomerDTO;
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customerDTO) {
        CustomerDTO existing = customerMap.get(customerId);
        existing.setCustomerName(customerDTO.getCustomerName());
        return Optional.of(existing);
    }

    @Override
    public Boolean deleteById(UUID customerId) {
        customerMap.remove(customerId);
        return true;
    }

    @Override
    public Optional<CustomerDTO> patchById(UUID customerId, CustomerDTO customerDTO) {
        CustomerDTO existing = customerMap.get(customerId);

        if (StringUtils.hasText(customerDTO.getCustomerName())) {
            existing.setCustomerName(customerDTO.getCustomerName());
        }

        return Optional.of(existing);
    }
}
