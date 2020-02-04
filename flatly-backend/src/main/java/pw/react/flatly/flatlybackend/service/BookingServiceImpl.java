package pw.react.flatly.flatlybackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.flatly.flatlybackend.exception.BookingNotFoundException;
import pw.react.flatly.flatlybackend.exception.ItemNotFoundException;
import pw.react.flatly.flatlybackend.exception.ParamsMismatchException;
import pw.react.flatly.flatlybackend.exception.UnauthorizedException;
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

    public BookingServiceImpl(BookingRepository bookingRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Booking getBooking(UUID security_token, Long book_id) {
        userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UnauthorizedException("You do not have permission"));
        return bookingRepository.findById(book_id).orElseThrow(() -> new BookingNotFoundException("There is no such a booking"));
    }

    @Override
    public BookingDetails getBookingDetails(UUID security_token, Long book_id) {
        userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UnauthorizedException("You do not have permission"));
        Booking booking = bookingRepository.findById(book_id).orElseThrow(() -> new BookingNotFoundException("There is no such a booking"));

        return new BookingDetails(booking, booking.getItem());
    }

    @Override
    public Booking addBooking(UUID security_token, Long item_id, Booking booking) {
        User user = userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UnauthorizedException("You do not have permission"));

        if(booking.getStart_date().isAfter(booking.getEnd_date())) {
            throw new ParamsMismatchException("Start date cannot be after end date");
        }

        Item item = itemRepository.findById(item_id).orElseThrow(() -> new ItemNotFoundException("There is no such an item"));

        if(item.getStart_date_time().isAfter(booking.getStart_date()) || item.getEnd_date_time().isBefore(booking.getEnd_date())) {
            throw new ParamsMismatchException("Item is not available these days");
        }

        for(Booking itemBooking : item.getBookings()) {
            if(!(booking.getEnd_date().isBefore(itemBooking.getStart_date()) || booking.getStart_date().isAfter(itemBooking.getEnd_date()))) {
                throw new ParamsMismatchException("Item is not available these days");
            }
        }

        booking.setItem(item);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking deleteBooking(UUID security_token, Long id) {
        User user = userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UnauthorizedException("You do not have permission"));
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException("There is no such a booking"));

        bookingRepository.delete(booking);
        return booking;
    }

    @Override
    public List<Booking> findAllByToken(UUID security_token) {
        User user = userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UnauthorizedException("You do not have permission"));
        List<Item> items = user.getItems();
        List<Booking> bookings = new ArrayList<Booking>();

        for(Item item: items) {
            bookings.addAll(item.getBookings());
        }

        return bookings;
    }

    @Override
    public List<BookingList> findAllBookingListByToken(UUID security_token) {
        User user = userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UnauthorizedException("You do not have permission"));

        List<Item> items = user.getItems();
        List<BookingList> bookingsList = new ArrayList<BookingList>();

        for(Item item: items) {
            bookingsList.addAll(item.getBookings().stream().map(booking -> new BookingList(booking, item)).collect(Collectors.toList()));
        }

        return bookingsList;
    }

}
