package guru.springframework.myspring7restmvc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonDeserialize(builder = Beer.BeerBuilder.class)
@Builder
@Data
public class Beer {

    @JsonProperty
    private UUID id;
    @JsonProperty
    private Integer version;
    @JsonProperty
    private String beerName;
    @JsonProperty
    private BeerStyle beerStyle;
    @JsonProperty
    private String upc;
    @JsonProperty
    private Integer quantityOnHand;
    @JsonProperty
    private BigDecimal price;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
