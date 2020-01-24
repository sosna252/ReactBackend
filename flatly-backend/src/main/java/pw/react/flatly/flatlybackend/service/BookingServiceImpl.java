package pw.react.flatly.flatlybackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.flatly.flatlybackend.exception.ItemNotFoundException;
import pw.react.flatly.flatlybackend.exception.ParamsMismatchException;
import pw.react.flatly.flatlybackend.exception.UserNotFoundException;
import pw.react.flatly.flatlybackend.model.Booking;
import pw.react.flatly.flatlybackend.model.Item;
import pw.react.flatly.flatlybackend.model.User;
import pw.react.flatly.flatlybackend.repository.BookingRepository;
import pw.react.flatly.flatlybackend.repository.ItemRepository;
import pw.react.flatly.flatlybackend.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    BookingRepository bookingRepository;
    ItemRepository itemRepository;
    UserRepository userRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Booking addBooking(Long item_id, Booking booking) {
        if(booking.getStart_date().compareTo(booking.getEnd_date())>0) {
            throw new ParamsMismatchException("Data rozpoczęcia nie może być późniejsza niż zakończenia");
        }
        Item item = itemRepository.findById(item_id).orElseThrow(() -> new ItemNotFoundException("Nie ma takiego mieszkania"));

        if(item.getStart_date_time().compareTo(booking.getStart_date())>0 || item.getEnd_date_time().compareTo(booking.getEnd_date())<0) {
            throw new ParamsMismatchException("Mieszkanie nie jest wtedy dostępne");
        }

        List<Booking> itemBookings = item.getBookings();
        for(Booking itemBooking : itemBookings) {
            if(!(itemBooking.getStart_date().compareTo(booking.getEnd_date())<0 && itemBooking.getEnd_date().compareTo(booking.getStart_date())<0)) {
                throw new ParamsMismatchException("Mieszkanie nie jest wtedy dostępne");
            }
        }

        bookingRepository.save(booking);
        return booking;
    }

    @Override
    public Booking deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Nie ma takiego mieszkania"));

        bookingRepository.delete(booking);
        return booking;
    }

    @Override
    public List<Booking> findAllByUserId(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Nie znaleziono takiego użytkownika"));
        List<Item> items = user.getItems();
        List<Booking> bookings = new ArrayList<Booking>();

        for(Item item: items) {
            bookings.addAll(item.getBookings());
        }

        return bookings;
    }

}
