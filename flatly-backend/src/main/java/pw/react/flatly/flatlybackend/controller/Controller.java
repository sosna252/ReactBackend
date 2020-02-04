package pw.react.flatly.flatlybackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.react.flatly.flatlybackend.model.Booking;
import pw.react.flatly.flatlybackend.model.Item;
import pw.react.flatly.flatlybackend.model.User;
import pw.react.flatly.flatlybackend.repository.UserRepository;
import pw.react.flatly.flatlybackend.service.BookingService;
import pw.react.flatly.flatlybackend.service.ItemService;
import pw.react.flatly.flatlybackend.service.UserService;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/")
@CrossOrigin
public class Controller {
    private UserService userService;
    private ItemService itemService;
    private BookingService bookingService;

    private UserRepository userRepository;

    @Autowired
    public Controller(UserService userService, ItemService itemService, BookingService bookingService, UserRepository userRepository) {
        this.userService = userService;
        this.itemService = itemService;
        this.bookingService = bookingService;

        this.userRepository = userRepository;
    }

    // 2 - Login to system

    private static class LoginData implements Serializable {
        private String login;
        private String password;

        public LoginData(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public LoginData() {
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }

    @GetMapping(path = "/login")
    public List<User> getUsers() {

        return userRepository.findAll();
    }

    @PostMapping(path = "/login")
    public ResponseEntity login(@RequestBody LoginData loginData) {

        return ResponseEntity.ok(userService.login(loginData.getLogin(), loginData.getPassword()));
    }

    // 3 - Adding new item

    @PostMapping(path = "/items")
    public ResponseEntity addItem(@RequestHeader String securityTokenValue, @RequestBody Item item) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        return ResponseEntity.ok(itemService.save(securityToken, item));
    }

    // 4 - Get all items

    @GetMapping(path = "/items")
    public ResponseEntity getItems(
            @RequestHeader String securityTokenValue,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Integer people,
            @RequestParam(required = false) Long authorId,

            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String dir) {

        UUID securityToken = UUID.fromString(securityTokenValue);
        return ResponseEntity.ok(itemService.findAll(securityToken, dateFrom, dateTo, city, people, authorId, sort, dir));
    }

    @GetMapping(path = "/items/{item_id}/vacant")
    public ResponseEntity getVacantById(@RequestHeader String securityTokenValue, @PathVariable Long item_id) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        return ResponseEntity.ok(itemService.findVacantById(securityToken, item_id));
    }

    // 5 - Get specific item

    @GetMapping(path = "/items/{item_id}")
    public ResponseEntity getItem(@RequestHeader String securityTokenValue, @PathVariable Long item_id) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        return ResponseEntity.ok(itemService.findById(securityToken, item_id));
    }

    // Get specific booking

    @GetMapping(path="book/{book_id}")
    public ResponseEntity getBooking(@RequestHeader String securityTokenValue, @PathVariable Long book_id) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        return ResponseEntity.ok(bookingService.getBooking(securityToken, book_id));
    }

    @GetMapping(path = "bookingdetails/{book_id}")
    public ResponseEntity getBookingDetails(@RequestHeader String securityTokenValue, @PathVariable Long book_id) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        return ResponseEntity.ok(bookingService.getBookingDetails(securityToken, book_id));
    }


    // 6 - Delete specific item

    @DeleteMapping(path = "/item/{item_id}")
    public ResponseEntity deleteItem(@RequestHeader String securityTokenValue, @PathVariable Long item_id) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        itemService.deleteById(securityToken, item_id);
        return ResponseEntity.ok("Item deleted");
    }

    // 7 - Update specific item

    @PutMapping(path = "/item/{item_id}")
    public ResponseEntity updateItem(@RequestHeader String securityTokenValue, @PathVariable Long item_id, @RequestBody Item itemDetails) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        return ResponseEntity.ok(itemService.updateById(securityToken, item_id, itemDetails));
    }

    // 8 - Book specific item

    @PostMapping(path = "/book/{item_id}")
    public ResponseEntity postBooking(@RequestHeader String securityTokenValue, @PathVariable Long item_id, @RequestBody Booking booking) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        return ResponseEntity.ok(bookingService.addBooking(securityToken, item_id, booking));
    }

    // 9 - Release specific item

    @DeleteMapping(path = "/cancel/{book_id}")
    public ResponseEntity deleteBooking(@RequestHeader String securityTokenValue, @PathVariable Long book_id) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        return ResponseEntity.ok(bookingService.deleteBooking(securityToken, book_id));
    }

    // wszystkie rezerwacje danego autora

    /*@GetMapping(path = "user/{id}/book")
    public ResponseEntity getAllBooksByUserId(@RequestHeader String securityTokenValue, @PathVariable("id") Long id) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        return ResponseEntity.ok(bookingService.findAllByUserId(securityToken, id));
    }

    @GetMapping(path="user/{id}/bookinglist")
    public ResponseEntity getBookingList(@RequestHeader String securityTokenValue, @PathVariable("id") Long id) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        return ResponseEntity.ok(bookingService.findAllBookingListByUserId(securityToken, id));
    }*/

    @GetMapping(path = "user/book")
    public ResponseEntity getAllBooksFromToken(@RequestHeader String securityTokenValue) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        return ResponseEntity.ok(bookingService.findAllByToken(securityToken));
    }

    @GetMapping(path="user/bookinglist")
    public ResponseEntity getBookingListFromToken(@RequestHeader String securityTokenValue) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        return ResponseEntity.ok(bookingService.findAllBookingListByToken(securityToken));
    }



    // Przesyłanie obrazka

    @GetMapping(path = "/itemphoto/{item_id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity getItemPhoto(@PathVariable Long item_id) {
        return ResponseEntity.ok(itemService.findItemPhotoByItemId(item_id));
    }

    @PostMapping(path = "/{item_id}/itemphoto")
    public ResponseEntity saveItemPhoto(@RequestHeader String securityTokenValue, @PathVariable Long item_id, @RequestBody byte[] photo) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        return ResponseEntity.ok(itemService.saveItemPhoto(securityToken, item_id, photo));
    }
}
