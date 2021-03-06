package pw.react.flatly.flatlybackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import pw.react.flatly.flatlybackend.exception.ItemNotFoundException;
import pw.react.flatly.flatlybackend.exception.ParamsMismatchException;
import pw.react.flatly.flatlybackend.exception.UnauthorizedException;
import pw.react.flatly.flatlybackend.model.Booking;
import pw.react.flatly.flatlybackend.model.Item;
import pw.react.flatly.flatlybackend.model.User;
import pw.react.flatly.flatlybackend.repository.ItemRepository;
import pw.react.flatly.flatlybackend.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;
    private UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Item save(UUID security_token, Item item) {
        User user = userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UnauthorizedException("You do not have permission"));

        if(item.getStart_date_time().isAfter(item.getEnd_date_time())) {
            throw new ParamsMismatchException("Start date cannot be after end date");
        }

        item.setUser(user);
        return itemRepository.save(item);
    }

    @Override
    public List<Item> findAll(UUID security_token, String dateFrom, String dateTo, String city, Integer people, Long authorId, String sort, String dir) {

        userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UnauthorizedException("You do not have permission"));

        if(dateFrom!=null && dateTo!=null) {
            if(LocalDate.parse(dateFrom).isAfter(LocalDate.parse(dateTo))) {
                throw new ParamsMismatchException("Start date cannot be after end date");
            }
        }

        return itemRepository.findAll().stream()
                .filter(item -> dateFrom == null || (!item.getStart_date_time().isAfter(LocalDate.parse(dateFrom)) && !item.getEnd_date_time().isBefore(LocalDate.parse(dateFrom))))
                .filter(item -> dateTo == null || (!item.getEnd_date_time().isBefore(LocalDate.parse(dateTo)) && !item.getStart_date_time().isAfter(LocalDate.parse(dateTo))))
                .filter(item -> city == null || city.equals(item.getCity()))
                .filter(item -> people == null || people <= item.getBeds())
                .filter(item -> authorId == null || authorId.equals(item.getUser().getId()))
                .sorted((i1, i2) -> {
                    if(dir!=null && dir.equals("desc")) {
                        if(sort==null) return i2.getStart_date_time().compareTo(i1.getStart_date_time());
                        switch (sort) {
                            case "end-time":
                                return i2.getEnd_date_time().compareTo((i1.getEnd_date_time()));
                            case "price":
                                return i2.getPrice().compareTo(i1.getPrice());
                            case "rating":
                                return i2.getRating().compareTo(i1.getRating());
                            default:
                                return i2.getStart_date_time().compareTo(i1.getStart_date_time());
                        }
                    } else {
                        if(sort==null) return i1.getStart_date_time().compareTo(i2.getStart_date_time());
                        switch (sort) {
                            case "end-time":
                                return i1.getEnd_date_time().compareTo(i2.getEnd_date_time());
                            case "price":
                                return i1.getPrice().compareTo(i2.getPrice());
                            case "rating":
                                return i1.getRating().compareTo(i2.getRating());
                            default:
                                return i1.getStart_date_time().compareTo(i2.getStart_date_time());
                        }
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<List<LocalDate>> findVacantById(UUID security_token, Long item_id) {
        userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UnauthorizedException("You do not have permission"));
        Item item = itemRepository.findById(item_id).orElseThrow(() -> new ItemNotFoundException("There is no such an item"));

        List<Booking> bookings = item.getBookings();

        List<List<LocalDate>> lists = new ArrayList<List<LocalDate>>();

        List<LocalDate> dates = new ArrayList<LocalDate>();
        dates.add(item.getStart_date_time());
        for (Booking booking:bookings) {
            dates.add(booking.getStart_date().minusDays(1));

            if(!dates.get(0).isAfter(dates.get(1))) lists.add(dates);

            dates = new ArrayList<LocalDate>();

            dates.add(booking.getEnd_date().plusDays(1));
        }
        dates.add(item.getEnd_date_time());

        if(!dates.get(0).isAfter(dates.get(1))) lists.add(dates);

        return lists;
    }

    @Override
    public Item findById(UUID security_token, Long item_id) {
        userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UnauthorizedException("You do not have permission"));
        return itemRepository.findById(item_id).orElseThrow(() -> new ItemNotFoundException("There is no such an item"));
    }

    @Override
    public void deleteById(UUID security_token, Long item_id) {
        User user = userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UnauthorizedException("You do not have permission"));
        Item item = itemRepository.findById(item_id).orElseThrow(() -> new ItemNotFoundException("There is no such an item"));

        if(item.getUser()!=user) throw new UnauthorizedException("This item is not yours");

        itemRepository.delete(item);
    }

    @Override
    public Item updateById(UUID security_token, Long item_id, Item itemDetails) {
        User user = userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UnauthorizedException("You do not have permission"));
        Item item = itemRepository.findById(item_id).orElseThrow(() -> new ItemNotFoundException("There is no such an item"));

        if(item.getUser()!=user) throw new UnauthorizedException("This item is not yours");

        item.setValues(itemDetails);
        return itemRepository.save(item);
    }

    @Override
    public byte[] findItemPhotoByItemId(Long item_id) {
        Item item = itemRepository.findById(item_id).orElseThrow(() -> new ItemNotFoundException("There is no such an item"));

        if(item.getPhoto()!=null) return item.getPhoto();

        try {
            ClassPathResource imgFile = new ClassPathResource("image/flat.jpg");
            return StreamUtils.copyToByteArray(imgFile.getInputStream());
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public Item saveItemPhoto(UUID security_token, Long item_id, byte[] photo) {
        User user = userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UnauthorizedException("You do not have permission"));
        Item item = itemRepository.findById(item_id).orElseThrow(() -> new ItemNotFoundException("There is no such an item"));

        if(item.getUser()!=user) throw new UnauthorizedException("This item is not yours");

        item.setPhoto(photo);

        return itemRepository.save(item);
    }

}
