package pw.react.flatly.flatlybackend.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import pw.react.flatly.flatlybackend.exception.BookingNotFoundException;
import pw.react.flatly.flatlybackend.exception.ItemNotFoundException;
import pw.react.flatly.flatlybackend.exception.ParamsMismatchException;
import pw.react.flatly.flatlybackend.exception.UnauthorizedException;
import pw.react.flatly.flatlybackend.model.*;
import pw.react.flatly.flatlybackend.repository.BookingRepository;
import pw.react.flatly.flatlybackend.repository.ItemRepository;
import pw.react.flatly.flatlybackend.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    public void givenUnassignedSecurityToken_whenGetBooking_thenThrowUnauthorizedException() {

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.empty());

        try {
            Booking returnedBooking = bookingService.getBooking(UUID.randomUUID(), 1L);
            fail("Should throw UnauthorizedException");
        } catch (UnauthorizedException ex) {
            assertEquals(ex.getMessage(), "Nie masz uprawnień");
        }
    }

    @Test
    public void givenNotExistingBooking_whenGetBooking_thenThrowBookingNotFoundException() {

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        try {
            Booking returnedBooking = bookingService.getBooking(UUID.randomUUID(), 1L);
            fail("Should throw BookingNotFoundException");
        } catch (BookingNotFoundException ex) {
            assertEquals(ex.getMessage(), "Nie ma takiej rezerwacji");
        }
    }

    @Test
    public void givenExistingBooking_whenGetBooking_thenReturnBooking() {

        Booking booking = new Booking();

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));

        Booking returnedBooking = bookingService.getBooking(UUID.randomUUID(), 1L);
        assertEquals(returnedBooking, booking);
    }

    @Test
    public void givenUnassignedSecurityToken_whenGetBookingDetails_thenThrowUnauthorizedException() {

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.empty());

        try {
            BookingDetails returnedBooking = bookingService.getBookingDetails(UUID.randomUUID(), 1L);
            fail("Should throw UnauthorizedException");
        } catch (UnauthorizedException ex) {
            assertEquals(ex.getMessage(), "Nie masz uprawnień");
        }
    }

    @Test
    public void givenNotExistingBooking_whenGetBookingDetails_thenThrowBookingNotFoundException() {

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        try {
            BookingDetails returnedBooking = bookingService.getBookingDetails(UUID.randomUUID(), 1L);
            fail("Should throw BookingNotFoundException");
        } catch (BookingNotFoundException ex) {
            assertEquals(ex.getMessage(), "Nie ma takiej rezerwacji");
        }
    }

    @Test
    public void givenExistingBooking_whenGetBookingDetails_thenReturnBookingDetails() {

        Item item = new Item();
        Booking booking = new Booking();
        booking.setItem(item);
        BookingDetails bookingDetails = new BookingDetails(booking, booking.getItem());

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));

        BookingDetails returnedBooking = bookingService.getBookingDetails(UUID.randomUUID(), 1L);

        assertEquals(returnedBooking.getId(), booking.getId());
    }

    @Test
    public void givenBookingWithDatesInIncorrectOrder_whenAddBooking_thenThrowParamsMismatchException() {

        Booking booking = new Booking();
        booking.setStart_date(LocalDate.parse("2012-12-12"));
        booking.setEnd_date(LocalDate.parse("2012-12-11"));

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));

        try {
            Booking returnedBooking = bookingService.addBooking(UUID.randomUUID(), 1L, booking);
            fail("Should throw ParamsMismatchException");
        } catch (ParamsMismatchException ex) {
            assertEquals(ex.getMessage(), "Data rozpoczęcia nie może być późniejsza niż zakończenia");
        }
    }

    @Test
    public void givenNotExistingItem_whenAddBooking_thenThrowItemNotFoundException() {

        Booking booking = new Booking();
        booking.setStart_date(LocalDate.parse("2012-12-12"));
        booking.setEnd_date(LocalDate.parse("2012-12-13"));

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        try {
            Booking returnedBooking = bookingService.addBooking(UUID.randomUUID(), 1L, booking);
            fail("Should throw ItemNotFoundException");
        } catch (ItemNotFoundException ex) {
            assertEquals(ex.getMessage(), "Nie ma takiego mieszkania");
        }
    }

    @Test
    public void givenExistingItemAndBookingStartsToEarly_whenAddBooking_thenThrowParamsMismatchException() {

        Item item = new Item();
        item.setStart_date_time(LocalDate.parse("2012-12-13"));
        item.setEnd_date_time(LocalDate.parse("2012-12-25"));

        Booking booking = new Booking();
        booking.setStart_date(LocalDate.parse("2012-12-12"));
        booking.setEnd_date(LocalDate.parse("2012-12-20"));

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        try {
            Booking returnedBooking = bookingService.addBooking(UUID.randomUUID(), 1L, booking);
            fail("Should throw ParamsMismatchException");
        } catch (ParamsMismatchException ex) {
            assertEquals(ex.getMessage(), "Mieszkanie nie jest wtedy dostępne");
        }
    }

    @Test
    public void givenExistingItemAndBookingEndsToLate_whenAddBooking_thenThrowParamsMismatchException() {

        Item item = new Item();
        item.setStart_date_time(LocalDate.parse("2012-12-10"));
        item.setEnd_date_time(LocalDate.parse("2012-12-15"));

        Booking booking = new Booking();
        booking.setStart_date(LocalDate.parse("2012-12-12"));
        booking.setEnd_date(LocalDate.parse("2012-12-20"));

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        try {
            Booking returnedBooking = bookingService.addBooking(UUID.randomUUID(), 1L, booking);
            fail("Should throw ParamsMismatchException");
        } catch (ParamsMismatchException ex) {
            assertEquals(ex.getMessage(), "Mieszkanie nie jest wtedy dostępne");
        }
    }

    @Test
    public void givenExistingItemAndBookingStartToEarlyAndEndsToLate_whenAddBooking_thenThrowParamsMismatchException() {

        Item item = new Item();
        item.setStart_date_time(LocalDate.parse("2012-12-15"));
        item.setEnd_date_time(LocalDate.parse("2012-12-20"));

        Booking booking = new Booking();
        booking.setStart_date(LocalDate.parse("2012-12-10"));
        booking.setEnd_date(LocalDate.parse("2012-12-25"));

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        try {
            Booking returnedBooking = bookingService.addBooking(UUID.randomUUID(), 1L, booking);
            fail("Should throw ParamsMismatchException");
        } catch (ParamsMismatchException ex) {
            assertEquals(ex.getMessage(), "Mieszkanie nie jest wtedy dostępne");
        }
    }

    @Test
    public void givenExistingItemButIsOccupied1_whenAddBooking_thenThrowParamsMismatchException() {
        // two bookings intersect, new booking is later

        Item item = new Item();
        item.setStart_date_time(LocalDate.parse("2012-12-01"));
        item.setEnd_date_time(LocalDate.parse("2012-12-31"));

        List<Booking> bookings = new ArrayList<>();
        Booking itemBooking1 = new Booking();
        itemBooking1.setStart_date(LocalDate.parse("2012-12-05"));
        itemBooking1.setEnd_date(LocalDate.parse("2012-12-15"));
        bookings.add(itemBooking1);
        item.setBookings(bookings);

        Booking booking = new Booking();
        booking.setStart_date(LocalDate.parse("2012-12-10"));
        booking.setEnd_date(LocalDate.parse("2012-12-20"));

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        try {
            Booking returnedBooking = bookingService.addBooking(UUID.randomUUID(), 1L, booking);
            fail("Should throw ParamsMismatchException");
        } catch (ParamsMismatchException ex) {
            assertEquals(ex.getMessage(), "Mieszkanie nie jest wtedy dostępne");
        }
    }

    @Test
    public void givenExistingItemButIsOccupied2_whenAddBooking_thenThrowParamsMismatchException() {
        // two bookings intersect, new booking is earlier

        Item item = new Item();
        item.setStart_date_time(LocalDate.parse("2012-12-01"));
        item.setEnd_date_time(LocalDate.parse("2012-12-31"));

        List<Booking> bookings = new ArrayList<>();
        Booking itemBooking1 = new Booking();
        itemBooking1.setStart_date(LocalDate.parse("2012-12-10"));
        itemBooking1.setEnd_date(LocalDate.parse("2012-12-20"));
        bookings.add(itemBooking1);
        item.setBookings(bookings);

        Booking booking = new Booking();
        booking.setStart_date(LocalDate.parse("2012-12-05"));
        booking.setEnd_date(LocalDate.parse("2012-12-15"));

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        try {
            Booking returnedBooking = bookingService.addBooking(UUID.randomUUID(), 1L, booking);
            fail("Should throw ParamsMismatchException");
        } catch (ParamsMismatchException ex) {
            assertEquals(ex.getMessage(), "Mieszkanie nie jest wtedy dostępne");
        }
    }

    @Test
    public void givenExistingItemButIsOccupied3_whenAddBooking_thenThrowParamsMismatchException() {
        // two bookings intersect, new booking is within another

        Item item = new Item();
        item.setStart_date_time(LocalDate.parse("2012-12-01"));
        item.setEnd_date_time(LocalDate.parse("2012-12-31"));

        List<Booking> bookings = new ArrayList<>();
        Booking itemBooking1 = new Booking();
        itemBooking1.setStart_date(LocalDate.parse("2012-12-05"));
        itemBooking1.setEnd_date(LocalDate.parse("2012-12-20"));
        bookings.add(itemBooking1);
        item.setBookings(bookings);

        Booking booking = new Booking();
        booking.setStart_date(LocalDate.parse("2012-12-10"));
        booking.setEnd_date(LocalDate.parse("2012-12-15"));

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        try {
            Booking returnedBooking = bookingService.addBooking(UUID.randomUUID(), 1L, booking);
            fail("Should throw ParamsMismatchException");
        } catch (ParamsMismatchException ex) {
            assertEquals(ex.getMessage(), "Mieszkanie nie jest wtedy dostępne");
        }
    }

    @Test
    public void givenExistingItemButIsOccupied4_whenAddBooking_thenThrowParamsMismatchException() {
        // two bookings intersect, old booking is within new

        Item item = new Item();
        item.setStart_date_time(LocalDate.parse("2012-12-01"));
        item.setEnd_date_time(LocalDate.parse("2012-12-31"));

        List<Booking> bookings = new ArrayList<>();
        Booking itemBooking1 = new Booking();
        itemBooking1.setStart_date(LocalDate.parse("2012-12-10"));
        itemBooking1.setEnd_date(LocalDate.parse("2012-12-15"));
        bookings.add(itemBooking1);
        item.setBookings(bookings);

        Booking booking = new Booking();
        booking.setStart_date(LocalDate.parse("2012-12-05"));
        booking.setEnd_date(LocalDate.parse("2012-12-20"));

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        try {
            Booking returnedBooking = bookingService.addBooking(UUID.randomUUID(), 1L, booking);
            fail("Should throw ParamsMismatchException");
        } catch (ParamsMismatchException ex) {
            assertEquals(ex.getMessage(), "Mieszkanie nie jest wtedy dostępne");
        }
    }

    @Test
    public void givenExistingItem_whenAddBooking_thenReturnBooking() {

        Item item = new Item();
        item.setStart_date_time(LocalDate.parse("2012-12-01"));
        item.setEnd_date_time(LocalDate.parse("2012-12-31"));

        List<Booking> bookings = new ArrayList<>();
        Booking itemBooking1 = new Booking();
        itemBooking1.setStart_date(LocalDate.parse("2012-12-10"));
        itemBooking1.setEnd_date(LocalDate.parse("2012-12-15"));
        bookings.add(itemBooking1);
        item.setBookings(bookings);

        Booking booking = new Booking();
        booking.setStart_date(LocalDate.parse("2012-12-20"));
        booking.setEnd_date(LocalDate.parse("2012-12-25"));

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);


        Booking returnedBooking = bookingService.addBooking(UUID.randomUUID(), 1L, booking);
        assertEquals(returnedBooking, booking);
    }

    @Test
    public void givenUnassignedSecurityToken_whenDeleteBooking_thenThrowUnauthorizedException() {

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.empty());

        try {
            Booking returnedBooking = bookingService.deleteBooking(UUID.randomUUID(), 1L);
            fail("Should throw UnauthorizedException");
        } catch (UnauthorizedException ex) {
            assertEquals(ex.getMessage(), "Nie masz uprawnień");
        }
    }

    @Test
    public void givenNotExistingBooking_whenDeleteBooking_thenThrowBookingNotFoundException() {

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        try {
            Booking returnedBooking = bookingService.deleteBooking(UUID.randomUUID(), 1L);
            fail("Should throw BookingNotFoundException");
        } catch (BookingNotFoundException ex) {
            assertEquals(ex.getMessage(), "Nie ma takiej rezerwacji");
        }
    }

    @Test
    public void givenBooking_whenDeleteBooking_thenReturnBooking() {

        Booking booking = new Booking();

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));

        Booking returnedBooking = bookingService.deleteBooking(UUID.randomUUID(), 1L);
        assertEquals(returnedBooking, booking);
    }

    @Test
    public void givenUnassignedSecurityToken_whenFindAllByToken_thenThrowUnauthorizedException() {

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.empty());

        try {
            List<Booking> returnedBookings = bookingService.findAllByToken(UUID.randomUUID());
            fail("Should throw UnauthorizedException");
        } catch (UnauthorizedException ex) {
            assertEquals(ex.getMessage(), "Nie masz uprawnień");
        }
    }

    @Test
    public void givenExistingUserWithTwoItems_whenFindAllByToken_thenReturnListOfBookings() {

        Item item1 = new Item();
        List<Booking> bookings1 = new ArrayList<>();
        bookings1.add(new Booking());
        item1.setBookings(bookings1);

        Item item2 = new Item();
        List<Booking> bookings2 = new ArrayList<>();
        bookings2.add(new Booking());
        item2.setBookings(bookings2);

        User user = new User();
        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        user.setItems(items);


        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(user));

        List<Booking> returnedBookings = bookingService.findAllByToken(UUID.randomUUID());
        assertEquals(returnedBookings.size(), 2);
    }

    @Test
    public void givenUnassignedSecurityToken_whenFindAllBookingListByToken_thenThrowUnauthorizedException() {

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.empty());

        try {
            List<BookingList> returnedBookings = bookingService.findAllBookingListByToken(UUID.randomUUID());
            fail("Should throw UnauthorizedException");
        } catch (UnauthorizedException ex) {
            assertEquals(ex.getMessage(), "Nie masz uprawnień");
        }
    }

    @Test
    public void givenExistingUserWithTwoItems_whenFindAllBookingListByToken_thenReturnListOfBookingList() {

        Item item1 = new Item();
        List<Booking> bookings1 = new ArrayList<>();
        bookings1.add(new Booking());
        item1.setBookings(bookings1);

        Item item2 = new Item();
        List<Booking> bookings2 = new ArrayList<>();
        bookings2.add(new Booking());
        item2.setBookings(bookings2);

        User user = new User();
        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        user.setItems(items);


        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(user));

        List<BookingList> returnedBookings = bookingService.findAllBookingListByToken(UUID.randomUUID());
        assertEquals(returnedBookings.size(), 2);
    }



}