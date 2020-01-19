package pw.react.flatly.flatlybackend.service;

import pw.react.flatly.flatlybackend.model.Item;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ItemService {
    Item save(Item item);

    List<Item> findByParams(LocalDate dateFrom, LocalDate dateTo, String city, Integer people, Long authorId);
    List<List<LocalDate>> findVacantById(Long id);

    Item findById(Long id);

    boolean deleteById(Long id);

    Item updateById(Long id, Item itemDetails);
}
