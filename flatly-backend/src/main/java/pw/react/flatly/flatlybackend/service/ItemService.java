package pw.react.flatly.flatlybackend.service;

import pw.react.flatly.flatlybackend.model.Item;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ItemService {
    Item save(UUID security_token, Item item);

    List<Item> findAll(UUID security_token, String dateFrom, String dateTo, String city, Integer people, Long authorId, String sort, String dir);
    List<List<LocalDate>> findVacantById(UUID security_token, Long id);

    Item findById(UUID security_token, Long id);

    void deleteById(UUID security_token, Long id);

    Item updateById(UUID security_token, Long id, Item itemDetails);
}
