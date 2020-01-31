package pw.react.flatly.flatlybackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.flatly.flatlybackend.exception.BookingNotFoundException;
import pw.react.flatly.flatlybackend.exception.ItemNotFoundException;
import pw.react.flatly.flatlybackend.exception.ParamsMismatchException;
import pw.react.flatly.flatlybackend.exception.UserNotFoundException;
import pw.react.flatly.flatlybackend.model.*;
import pw.react.flatly.flatlybackend.repository.BookingRepository;
import pw.react.flatly.flatlybackend.repository.ItemRepository;
import pw.react.flatly.flatlybackend.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;
    private ItemRepository itemRepository;
    private UserRepository userRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Booking getBooking(UUID security_token, Long id) {
        User user = userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UserNotFoundException("Nie masz uprawnień"));
        return bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException("Nie ma takiej rezerwacji"));
    }

    @Override
    public BookingDetails getBookingDetails(UUID security_token, Long id) {
        User user = userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UserNotFoundException("Nie masz uprawnień"));
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException("Nie ma takiej rezerwacji"));

        return new BookingDetails(booking, booking.getItem());
    }




    @Override
    public Booking addBooking(UUID security_token, Long item_id, Booking booking) {
        User user = userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UserNotFoundException("Nie masz uprawnień"));
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
    public Booking deleteBooking(UUID security_token, Long id) {
        User user = userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UserNotFoundException("Nie masz uprawnień"));
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Nie ma takiego mieszkania"));

        bookingRepository.delete(booking);
        return booking;
    }

    /*@Override
    public List<Booking> findAllByUserId(UUID security_token, Long id) {
        userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UserNotFoundException("Nie masz uprawnień"));
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Nie znaleziono takiego użytkownika"));
        List<Item> items = user.getItems();
        List<Booking> bookings = new ArrayList<Booking>();

        for(Item item: items) {
            bookings.addAll(item.getBookings());
        }

        return bookings;
    }

    @Override
    public List<BookingList> findAllBookingListByUserId(UUID security_token, Long id) {
        userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UserNotFoundException("Nie masz uprawnień"));
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Nie znaleziono takiego użytkownika"));
        List<Item> items = user.getItems();
        List<BookingList> bookingsList = new ArrayList<BookingList>();

        for(Item item: items) {
            bookingsList.addAll(item.getBookings().stream().map(booking -> new BookingList(booking, item)).collect(Collectors.toList()));
        }

        return bookingsList;
    }*/

    @Override
    public List<Booking> findAllByToken(UUID security_token) {
        userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UserNotFoundException("Nie masz uprawnień"));
        User user = userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UserNotFoundException("Nie znaleziono takiego użytkownika"));
        List<Item> items = user.getItems();
        List<Booking> bookings = new ArrayList<Booking>();

        for(Item item: items) {
            bookings.addAll(item.getBookings());
        }

        return bookings;
    }

    @Override
    public List<BookingList> findAllBookingListByToken(UUID security_token) {
        userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UserNotFoundException("Nie masz uprawnień"));
        User user = userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UserNotFoundException("Nie znaleziono takiego użytkownika"));
        List<Item> items = user.getItems();
        List<BookingList> bookingsList = new ArrayList<BookingList>();

        for(Item item: items) {
            bookingsList.addAll(item.getBookings().stream().map(booking -> new BookingList(booking, item)).collect(Collectors.toList()));
        }

        return bookingsList;
    }

}
