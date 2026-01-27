package guru.springframework.myspring7restmvc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonDeserialize(builder = BeerDTO.BeerDTOBuilder.class)
@Builder
@Data
public class BeerDTO {

    @JsonProperty
    private UUID id;

    @JsonProperty
    private Integer version;

    @NotBlank
    @NotNull
    @JsonProperty
    private String beerName;

    @NotNull
    @JsonProperty
    private BeerStyle beerStyle;

    @NotBlank
    @NotNull
    @JsonProperty
    private String upc;

    @JsonProperty
    private Integer quantityOnHand;

    @NotNull
    @JsonProperty
    private BigDecimal price;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
