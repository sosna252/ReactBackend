package pw.react.flatly.flatlybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.flatly.flatlybackend.model.Item;

import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
