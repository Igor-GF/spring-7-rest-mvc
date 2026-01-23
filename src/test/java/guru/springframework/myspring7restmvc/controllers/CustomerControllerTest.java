package guru.springframework.myspring7restmvc.controllers;

import guru.springframework.myspring7restmvc.model.CustomerDTO;
import guru.springframework.myspring7restmvc.services.CustomerService;
import guru.springframework.myspring7restmvc.services.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    // Required dependency for CustomerController
    @MockitoBean
    CustomerService customerService;

    CustomerServiceImpl customerServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidCaptor;
    @Captor
    ArgumentCaptor<CustomerDTO> customerCaptor;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void test_get_all_customers() throws Exception {
        given(customerService.findAll()).willReturn(customerServiceImpl.findAll());

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void test_get_customer_by_id() throws Exception {
        CustomerDTO testCustomerDTO = customerServiceImpl.findAll().getFirst();

        given(customerService.findById(testCustomerDTO.getId())).willReturn(Optional.of(testCustomerDTO));

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, testCustomerDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testCustomerDTO.getId().toString())))
                .andExpect(jsonPath("$.customerName", is(testCustomerDTO.getCustomerName())));
    }

    @Test
    void test_get_customer_by_id_not_found() throws Exception {
        given(customerService.findById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_create_new_customer() throws Exception {
        CustomerDTO testCustomerDTO = customerServiceImpl.findAll().getFirst();
        testCustomerDTO.setId(null);
        testCustomerDTO.setVersion(null);

        given(customerService.saveCustomer(any(CustomerDTO.class))).willReturn(customerServiceImpl.findAll().get(1));

        mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testCustomerDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void test_update_customer_by_id() throws Exception {
        CustomerDTO testCustomerDTO = customerServiceImpl.findAll().getFirst();

        given(customerService.updateCustomerById(any(), any())).willReturn(Optional.of(testCustomerDTO));

        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, testCustomerDTO.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testCustomerDTO)))
                .andExpect(status().isNoContent());

        verify(customerService).updateCustomerById(uuidCaptor.capture(), any(CustomerDTO.class));

        assertThat(testCustomerDTO.getId()).isEqualTo(uuidCaptor.getValue());
    }

    @Test
    void test_delete_customer_by_id() throws Exception {
        CustomerDTO testCustomerDTO = customerServiceImpl.findAll().getFirst();

        given(customerService.deleteById(any())).willReturn(true);

        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, testCustomerDTO.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).deleteById(uuidCaptor.capture());

        assertThat(testCustomerDTO.getId()).isEqualTo(uuidCaptor.getValue());
    }

    @Test
    void test_patch_customer() throws Exception {
        CustomerDTO testCustomerDTO = customerServiceImpl.findAll().getFirst();

        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("customerName", "Test-customer-name");

        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, testCustomerDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());

        verify(customerService).patchById(uuidCaptor.capture(), customerCaptor.capture());

        assertThat(testCustomerDTO.getId()).isEqualTo(uuidCaptor.getValue());
        assertThat(customerMap.get("customerName")).isEqualTo(customerCaptor.getValue().getCustomerName());

    }
}