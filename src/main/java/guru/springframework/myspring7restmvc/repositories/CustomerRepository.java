package guru.springframework.myspring7restmvc.repositories;

import guru.springframework.myspring7restmvc.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
