package pw.react.flatly.flatlybackend.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import pw.react.flatly.flatlybackend.exception.ItemNotFoundException;
import pw.react.flatly.flatlybackend.exception.ParamsMismatchException;
import pw.react.flatly.flatlybackend.exception.UnauthorizedException;
import pw.react.flatly.flatlybackend.model.Booking;
import pw.react.flatly.flatlybackend.model.Item;
import pw.react.flatly.flatlybackend.model.User;
import pw.react.flatly.flatlybackend.repository.ItemRepository;
import pw.react.flatly.flatlybackend.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    public void givenUnassignedSecurityToken_whenSave_thenThrowUnauthorizedException() {

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.empty());

        try {
            Item returnedItem = itemService.save(UUID.randomUUID(), new Item());
            fail("Should throw UnauthorizedException");
        } catch (UnauthorizedException ex) {
            assertEquals("You do not have permission", ex.getMessage());
        }
    }

    @Test
    public void givenItemWithStartDateAfterEndDate_whenSave_thenThrowParamsMismatchException() {

        Item item = new Item();
        item.setStart_date_time(LocalDate.parse("2012-12-21"));
        item.setEnd_date_time(LocalDate.parse("2012-12-14"));

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));

        try {
            Item returnedItem = itemService.save(UUID.randomUUID(), item);
            fail("Should throw ParamsMismatchException");
        } catch (ParamsMismatchException ex) {
            assertEquals("Start date cannot be after end date", ex.getMessage());
        }
    }

    @Test
    public void givenExistingItem_whenSave_thenThrowParamsMismatchException() {

        Item item = new Item();
        item.setStart_date_time(LocalDate.parse("2012-12-15"));
        item.setEnd_date_time(LocalDate.parse("2012-12-20"));

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.save(Mockito.any())).thenReturn(item);

        Item returnedItem = itemService.save(UUID.randomUUID(), item);
        assertEquals(item, returnedItem);
    }

    @Test
    public void givenUnassignedSecurityToken_whenFindAll_thenThrowUnauthorizedException() {

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.empty());

        try {
            List<Item> returnedItems = itemService.findAll(UUID.randomUUID(), null, null, null, null, null, null, null);
            fail("Should throw UnauthorizedException");
        } catch (UnauthorizedException ex) {
            assertEquals("You do not have permission", ex.getMessage());
        }
    }

    @Test
    public void givenStartDateAfterEndDateInParams_whenFindAll_thenThrowParamsMismatchException() {

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));

        try {
            List<Item> returnedItems = itemService.findAll(UUID.randomUUID(), "2012-12-15", "2012-12-10", null, null, null, null, null);
            fail("Should throw ParamsMismatchException");
        } catch (ParamsMismatchException ex) {
            assertEquals("Start date cannot be after end date", ex.getMessage());
        }
    }

    private List<Item> findAll() {
        Item item4 = new Item(LocalDate.parse("2012-12-14"), LocalDate.parse("2012-12-16"), 10, BigDecimal.valueOf(10), "Białystok");
        Item item5 = new Item(LocalDate.parse("2012-12-06"), LocalDate.parse("2012-12-20"), 4, BigDecimal.valueOf(4),  "Warszawa");
        Item item3 = new Item(LocalDate.parse("2012-12-13"), LocalDate.parse("2012-12-20"), 2, BigDecimal.valueOf(12), "Poznań");
        Item item1 = new Item(LocalDate.parse("2012-12-02"), LocalDate.parse("2012-12-17"), 3, BigDecimal.valueOf(7), "Warszawa");
        Item item2 = new Item(LocalDate.parse("2012-12-06"), LocalDate.parse("2012-12-09"), 5, BigDecimal.valueOf(2), "Gdańsk");

        return Arrays.asList(item1, item2, item3, item4, item5);
    }

    @Test
    public void givenNullToEveryParams_whenFindAll_thenReturnListInConcreteOrder() {

        List<Item> items = findAll();

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findAll()).thenReturn(items);

        List<Item> returnedItems = itemService.findAll(UUID.randomUUID(), null, null, null, null, null, null, null);

        items.sort(Comparator.comparing(Item::getStart_date_time));

        assertEquals(items.size(), returnedItems.size());
        assertIterableEquals(items, returnedItems);
        assertTrue(returnedItems.get(0).getStart_date_time().isBefore(returnedItems.get(1).getStart_date_time()));
    }

    @Test
    public void givenSortingByEndTime_whenFindAll_thenReturnListInConcreteOrder() {

        List<Item> items = findAll();

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findAll()).thenReturn(items);

        List<Item> returnedItems = itemService.findAll(UUID.randomUUID(), null, null, null, null, null, "end-time", null);

        items.sort(Comparator.comparing(Item::getEnd_date_time));

        assertEquals(items.size(), returnedItems.size());
        assertIterableEquals(items, returnedItems);
        assertTrue(returnedItems.get(0).getEnd_date_time().isBefore(returnedItems.get(1).getEnd_date_time()));
    }

    @Test
    public void givenSortingByPriceDescending_whenFindAll_thenReturnListInConcreteOrder() {

        List<Item> items = findAll();

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findAll()).thenReturn(items);

        List<Item> returnedItems = itemService.findAll(UUID.randomUUID(), null, null, null, null, null, "price", "desc");

        items.sort(Comparator.comparing(Item::getPrice).reversed());

        assertEquals(items.size(), returnedItems.size());
        assertIterableEquals(items, returnedItems);
        assertTrue(returnedItems.get(0).getPrice().compareTo(returnedItems.get(1).getPrice())>=0);
    }

    @Test
    public void givenFilteringByCity_whenFindAll_thenReturnListInConcreteOrder() {

        List<Item> items = findAll();

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findAll()).thenReturn(items);

        List<Item> returnedItems = itemService.findAll(UUID.randomUUID(), null, null, "Warszawa", null, null, null, null);

        assertEquals(2, returnedItems.size());
        assertTrue(returnedItems.get(0).getStart_date_time().isBefore(returnedItems.get(1).getStart_date_time()));
    }

    @Test
    public void givenFilteringByDatesAndSortingByPrices_whenFindAll_thenReturnListInConcreteOrder() {

        List<Item> items = findAll();

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findAll()).thenReturn(items);

        List<Item> returnedItems = itemService.findAll(UUID.randomUUID(), "2012-12-10", "2012-12-15", null, null, null, "price", null);

        assertEquals(2, returnedItems.size());
        assertTrue(returnedItems.get(0).getPrice().compareTo(returnedItems.get(1).getPrice())<=0);
    }



    @Test
    public void givenUnassignedSecurityToken_whenFindVacantById_thenThrowUnauthorizedException() {

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.empty());

        try {
            List<List<LocalDate>> returnedLocalDates = itemService.findVacantById(UUID.randomUUID(), 1L);
            fail("Should throw UnauthorizedException");
        } catch (UnauthorizedException ex) {
            assertEquals("You do not have permission", ex.getMessage());
        }
    }

    @Test
    public void givenNotExistingItem_whenFindVacantById_thenThrowItemNotFoundException() {

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        try {
            List<List<LocalDate>> returnedLocalDates = itemService.findVacantById(UUID.randomUUID(), 1L);
            fail("Should throw ItemNotFoundException");
        } catch (ItemNotFoundException ex) {
            assertEquals("There is no such an item", ex.getMessage());
        }
    }

    @Test
    public void givenOccupiedItemByOneBooking_whenFindVacantById_thenThrowItemNotFoundException() {

        Item item = new Item();
        item.setStart_date_time(LocalDate.parse("2012-12-05"));
        item.setEnd_date_time(LocalDate.parse("2012-12-20"));
        Booking booking = new Booking();
        booking.setStart_date(LocalDate.parse("2012-12-05"));
        booking.setEnd_date(LocalDate.parse("2012-12-20"));

        item.setBookings(Collections.singletonList(booking));

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        List<List<LocalDate>> returnedLocalDates = itemService.findVacantById(UUID.randomUUID(), 1L);

        assertEquals(0, returnedLocalDates.size());
    }

    @Test
    public void givenOccupiedItemByTwoBooking_whenFindVacantById_thenThrowItemNotFoundException() {

        Item item = new Item();
        item.setStart_date_time(LocalDate.parse("2012-12-05"));
        item.setEnd_date_time(LocalDate.parse("2012-12-20"));
        Booking booking1 = new Booking();
        booking1.setStart_date(LocalDate.parse("2012-12-05"));
        booking1.setEnd_date(LocalDate.parse("2012-12-12"));
        Booking booking2 = new Booking();
        booking2.setStart_date(LocalDate.parse("2012-12-13"));
        booking2.setEnd_date(LocalDate.parse("2012-12-20"));

        item.setBookings(Arrays.asList(booking1, booking2));

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        List<List<LocalDate>> returnedLocalDates = itemService.findVacantById(UUID.randomUUID(), 1L);

        assertEquals(0, returnedLocalDates.size());
    }

    @Test
    public void givenItemWithOneDayVacant_whenFindVacantById_thenThrowItemNotFoundException() {

        Item item = new Item();
        item.setStart_date_time(LocalDate.parse("2012-12-05"));
        item.setEnd_date_time(LocalDate.parse("2012-12-20"));
        Booking booking = new Booking();
        booking.setStart_date(LocalDate.parse("2012-12-06"));
        booking.setEnd_date(LocalDate.parse("2012-12-20"));

        item.setBookings(Collections.singletonList(booking));

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        List<List<LocalDate>> returnedLocalDates = itemService.findVacantById(UUID.randomUUID(), 1L);

        assertEquals(returnedLocalDates.get(0).get(0), returnedLocalDates.get(0).get(1));
    }

    @Test
    public void givenItemsWithSomeDaysOccupied_whenFindVacantById_thenThrowItemNotFoundException() {

        Item item = new Item();
        item.setStart_date_time(LocalDate.parse("2012-12-05"));
        item.setEnd_date_time(LocalDate.parse("2012-12-30"));
        Booking booking1 = new Booking();
        booking1.setStart_date(LocalDate.parse("2012-12-10"));
        booking1.setEnd_date(LocalDate.parse("2012-12-15"));
        Booking booking2 = new Booking();
        booking2.setStart_date(LocalDate.parse("2012-12-20"));
        booking2.setEnd_date(LocalDate.parse("2012-12-25"));

        item.setBookings(Arrays.asList(booking1, booking2));

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        List<List<LocalDate>> returnedLocalDates = itemService.findVacantById(UUID.randomUUID(), 1L);

        assertEquals(3, returnedLocalDates.size());
    }

    @Test
    public void givenUnassignedSecurityToken_whenFindById_thenThrowUnauthorizedException() {

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.empty());

        try {
            Item returnedItem = itemService.findById(UUID.randomUUID(), 1L);
            fail("Should throw UnauthorizedException");
        } catch (UnauthorizedException ex) {
            assertEquals("You do not have permission", ex.getMessage());
        }
    }

    @Test
    public void givenNotExistingItem_whenFindById_thenThrowItemNotFoundException() {

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        try {
            Item returnedItem = itemService.findById(UUID.randomUUID(), 1L);
            fail("Should throw ItemNotFoundException");
        } catch (ItemNotFoundException ex) {
            assertEquals("There is no such an item", ex.getMessage());
        }
    }

    @Test
    public void givenExistingItem_whenFindById_thenReturnItem() {

        Item item = new Item();

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        Item returnedItem = itemService.findById(UUID.randomUUID(), 1L);

        assertEquals(item, returnedItem);
    }

    @Test
    public void givenUnassignedSecurityToken_whenDeleteById_thenThrowUnauthorizedException() {

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.empty());

        try {
            itemService.deleteById(UUID.randomUUID(), 1L);
            fail("Should throw UnauthorizedException");
        } catch (UnauthorizedException ex) {
            assertEquals("You do not have permission", ex.getMessage());
        }
    }

    @Test
    public void givenNotExistingItem_whenDeleteById_thenThrowItemNotFoundException() {

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        try {
            itemService.deleteById(UUID.randomUUID(), 1L);
            fail("Should throw ItemNotFoundException");
        } catch (ItemNotFoundException ex) {
            assertEquals("There is no such an item", ex.getMessage());
        }
    }

    @Test
    public void givenExistingItemOfAnotherUser_whenDeleteById_thenThrowUnauthorizedException() {

        Item item = new Item();
        item.setUser(new User());

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        try {
            itemService.deleteById(UUID.randomUUID(), 1L);
            fail("Should throw UnauthorizedException");
        } catch (UnauthorizedException ex) {
            assertEquals("This item is not yours", ex.getMessage());
        }
    }

    @Test
    public void givenExistingItem_whenDeleteById_thenAccept() {

        Item item = new Item();

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        itemService.findById(UUID.randomUUID(), 1L);
    }

    @Test
    public void givenUnassignedSecurityToken_whenUpdateById_thenThrowUnauthorizedException() {

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.empty());

        try {
            Item returnedItem = itemService.updateById(UUID.randomUUID(), 1L, new Item());
            fail("Should throw UnauthorizedException");
        } catch (UnauthorizedException ex) {
            assertEquals("You do not have permission", ex.getMessage());
        }
    }

    @Test
    public void givenNotExistingItem_whenUpdateById_thenThrowItemNotFoundException() {

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        try {
            Item returnedItem = itemService.updateById(UUID.randomUUID(), 1L, new Item());
            fail("Should throw ItemNotFoundException");
        } catch (ItemNotFoundException ex) {
            assertEquals("There is no such an item", ex.getMessage());
        }
    }

    @Test
    public void givenExistingItemOfAnotherUser_whenUpdateById_thenThrowUnauthorizedException() {

        Item item = new Item();
        item.setUser(new User());

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        try {
            Item returnedItem = itemService.updateById(UUID.randomUUID(), 1L, new Item());
            fail("Should throw UnauthorizedException");
        } catch (UnauthorizedException ex) {
            assertEquals("This item is not yours", ex.getMessage());
        }
    }

    @Test
    public void givenExistingItem_whenUpdateById_thenChangeTitle() {

        User user = new User();
        Item item1 = new Item();
        item1.setUser(user);
        Item item2 = new Item();
        item2.setTitle("Title");

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item1));
        Mockito.when(itemRepository.save(Mockito.any())).thenReturn(item1);

        Item returnedItem = itemService.updateById(UUID.randomUUID(), 1L, item2);
        assertEquals("Title", returnedItem.getTitle());
    }

    @Test
    public void givenNotExistingItem_whenFindItemPhotoByItemId_thenThrowItemNotFoundException() {

        Mockito.when(itemRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        try {
            byte[] returnedPhoto = itemService.findItemPhotoByItemId(1L);
            fail("Should throw ItemNotFoundException");
        } catch (ItemNotFoundException ex) {
            assertEquals("There is no such an item", ex.getMessage());
        }
    }

    @Test
    public void givenUnassignedSecurityToken_whenSaveItemPhoto_thenThrowUnauthorizedException() {

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.empty());

        try {
            Item returnedItem = itemService.saveItemPhoto(UUID.randomUUID(), 1L, new byte[0]);
            fail("Should throw UnauthorizedException");
        } catch (UnauthorizedException ex) {
            assertEquals("You do not have permission", ex.getMessage());
        }
    }

    @Test
    public void givenNotExistingItem_whenSaveItemPhoto_thenThrowItemNotFoundException() {

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        try {
            Item returnedItem = itemService.saveItemPhoto(UUID.randomUUID(), 1L, new byte[0]);
            fail("Should throw ItemNotFoundException");
        } catch (ItemNotFoundException ex) {
            assertEquals("There is no such an item", ex.getMessage());
        }
    }

    @Test
    public void givenExistingItemOfAnotherUser_whenSaveItemPhoto_thenThrowUnauthorizedException() {

        Item item = new Item();
        item.setUser(new User());

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        try {
            Item returnedItem = itemService.saveItemPhoto(UUID.randomUUID(), 1L, new byte[0]);
            fail("Should throw UnauthorizedException");
        } catch (UnauthorizedException ex) {
            assertEquals("This item is not yours", ex.getMessage());
        }
    }

    @Test
    public void givenExistingItem_whenSaveItemPhoto_thenReturnItem() {

        User user = new User();
        Item item = new Item();
        item.setUser(user);

        byte[] photo = new byte[0];

        Mockito.when(userRepository.findBySecurityToken(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        Mockito.when(itemRepository.save(Mockito.any())).thenReturn(item);

        Item returnedItem = itemService.saveItemPhoto(UUID.randomUUID(), 1L, photo);

        assertEquals(photo, returnedItem.getPhoto());
    }


}