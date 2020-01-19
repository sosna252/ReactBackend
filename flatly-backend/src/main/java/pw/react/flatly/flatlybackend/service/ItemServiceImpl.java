package pw.react.flatly.flatlybackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.flatly.flatlybackend.model.Booking;
import pw.react.flatly.flatlybackend.model.Item;
import pw.react.flatly.flatlybackend.repository.ItemRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public List<Item> findByParams(LocalDate dateFrom, LocalDate dateTo, String city, Integer people, Long authorId) {

        List<Item> items = itemRepository.findAll().stream()
                .filter(item -> {

                    if(dateFrom==null && dateTo==null) return true;

                    List<Booking> bookings = item.getBookings();
                    LocalDate from = dateFrom!=null ? dateFrom : item.getStart_date_time();
                    LocalDate to = dateTo!=null ? dateTo : item.getEnd_date_time();


                    for (Booking booking: bookings) {
                        if(booking.getStart_date().compareTo(from)>=0 && booking.getStart_date().compareTo(to)<=0) return false;
                    }

                    return true;
                })
                .filter(item -> city == null || city.equals(item.getCity()))
                .filter(item -> people == null || people <= item.getBeds())
                .filter(item -> authorId == null || authorId == item.getUser().getId())
                .collect(Collectors.toList());

        return items;
    }

    @Override
    public List<List<LocalDate>> findVacantById(Long id) {
        Item item = itemRepository.findById(id).orElse(null);
        if(item==null) return null;

        List<Booking> bookings = item.getBookings();

        List<List<LocalDate>> lists = new ArrayList<List<LocalDate>>();

        List<LocalDate> dates = new ArrayList<LocalDate>();
        dates.add(item.getStart_date_time());
        for (Booking booking:bookings) {
            dates.add(booking.getStart_date());
            lists.add(dates);

            dates = new ArrayList<LocalDate>();

            dates.add(booking.getEnd_date());
        }
        dates.add(item.getEnd_date_time());
        lists.add(dates);

        return lists;
    }

    @Override
    public Item findById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteById(Long id) {
        Item item = itemRepository.findById(id).orElse(null);
        if(item == null) return false;

        itemRepository.delete(item);
        return true;
    }

    @Override
    public Item updateById(Long id, Item itemDetails) {
        Item item = itemRepository.findById(id).orElse(null);
        if(item==null) return null;

        itemDetails.setId(item.getId());
        itemRepository.save(itemDetails);

        return itemDetails;
    }

}
