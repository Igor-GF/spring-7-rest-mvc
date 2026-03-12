package guru.springframework.myspring7restmvc.repositories;

import guru.springframework.myspring7restmvc.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
