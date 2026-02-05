package guru.springframework.myspring7restmvc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonDeserialize(builder = CustomerDTO.CustomerDTOBuilder.class)
@Builder
@Data
public class CustomerDTO {

    @JsonProperty
    private UUID id;
    @JsonProperty
    private String customerName;
    @JsonProperty
    private Integer version;

    private String email;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
