package guru.springframework.myspring7restmvc.mappers;

import guru.springframework.myspring7restmvc.entities.Beer;
import guru.springframework.myspring7restmvc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO dto);
    BeerDTO beerToBeerDTO(Beer beer);
}
