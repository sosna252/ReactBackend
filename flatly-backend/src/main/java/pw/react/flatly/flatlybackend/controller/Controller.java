package pw.react.flatly.flatlybackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.react.flatly.flatlybackend.exception.UserNotFoundException;
import pw.react.flatly.flatlybackend.model.Booking;
import pw.react.flatly.flatlybackend.model.Item;
import pw.react.flatly.flatlybackend.model.ItemPhoto;
import pw.react.flatly.flatlybackend.model.User;
import pw.react.flatly.flatlybackend.repository.UserRepository;
import pw.react.flatly.flatlybackend.service.BookingService;
import pw.react.flatly.flatlybackend.service.ItemPhotoService;
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
    private ItemPhotoService itemPhotoService;

    private UserRepository userRepository;

    @Autowired
    public Controller(UserService userService, ItemService itemService, BookingService bookingService, ItemPhotoService itemPhotoService, UserRepository userRepository) {
        this.userService = userService;
        this.itemService = itemService;
        this.bookingService = bookingService;

        this.itemPhotoService = itemPhotoService;

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

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @GetMapping(path = "/login")
    public List<User> getUsers() {

        return userRepository.findAll();
    }

    @PostMapping(path = "/login")
    public ResponseEntity login(@RequestBody LoginData loginData) throws UserNotFoundException {

        return ResponseEntity.ok(userService.login(loginData.getLogin(), loginData.getPassword()));
    }

    // 3 - Adding new item

    @PostMapping(path = "/items")
    public ResponseEntity addItem(@RequestHeader String securityTokenValue, @RequestBody Item item) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        Item savedItem = itemService.save(securityToken, item);

        if(savedItem!=null) return ResponseEntity.ok(savedItem);
        else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("something went wrong");
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

    @GetMapping(path = "/items/{id}/vacant")
    public ResponseEntity getVacantById(@RequestHeader String securityTokenValue, @PathVariable("id") Long id) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        return ResponseEntity.ok(itemService.findVacantById(securityToken, id));
    }

    // 5 - Get specific item

    @GetMapping(path = "/items/{id}")
    public ResponseEntity getItem(@RequestHeader String securityTokenValue, @PathVariable("id") Long id) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        Item item = itemService.findById(securityToken, id);

        if(item!=null) return ResponseEntity.ok(item);
        else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("something went wrong");
    }

    // Get specific booking

    @GetMapping(path="book/{id}")
    public ResponseEntity getBooking(@RequestHeader String securityTokenValue, @PathVariable("id") Long id) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        return ResponseEntity.ok(bookingService.getBooking(securityToken, id));
    }

    @GetMapping(path = "bookingdetails/{id}")
    public ResponseEntity getBookingDetails(@RequestHeader String securityTokenValue, @PathVariable("id") Long id) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        return ResponseEntity.ok(bookingService.getBookingDetails(securityToken, id));
    }


    // 6 - Delete specific item

    @DeleteMapping(path = "/item/{id}")
    public ResponseEntity deleteItem(@RequestHeader String securityTokenValue, @PathVariable("id") Long id) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        itemService.deleteById(securityToken, id);
        return ResponseEntity.ok("Item deleted");
    }

    // 7 - Update specific item

    @PutMapping(path = "/item/{id}")
    public ResponseEntity updateItem(@RequestHeader String securityTokenValue, @PathVariable("id") Long id, @RequestBody Item itemDetails) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        return ResponseEntity.ok(itemService.updateById(securityToken, id, itemDetails));
    }

    // 8 - Book specific item

    @PostMapping(path = "/book/{item_id}")
    public ResponseEntity postBooking(@RequestHeader String securityTokenValue, @PathVariable("id") Long item_id, @RequestBody Booking booking) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        return ResponseEntity.ok(bookingService.addBooking(securityToken, item_id, booking));
    }

    // 9 - Release specific item

    @DeleteMapping(path = "/cancel/{id}")
    public ResponseEntity deleteBooking(  UUID securityToken, @PathVariable("id") Long id) {
        Booking booking = bookingService.deleteBooking(securityToken, id);

        return ResponseEntity.ok(booking);
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

    @GetMapping(path = "user/{id}/book")
    public ResponseEntity getAllBooksFromToken(@RequestHeader String securityTokenValue) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        return ResponseEntity.ok(bookingService.findAllByToken(securityToken));
    }

    @GetMapping(path="user/{id}/bookinglist")
    public ResponseEntity getBookingListFromToken(@RequestHeader String securityTokenValue) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        return ResponseEntity.ok(bookingService.findAllBookingListByToken(securityToken));
    }



    // Przesyłanie obrazka

    @GetMapping(path = "/itemphoto/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getItemPhoto(@PathVariable("id") Long id) throws IOException {
        ItemPhoto itemPhoto = itemPhotoService.findItemPhotoByItemId(id);

        return itemPhoto.getPhoto();
    }

    @PostMapping(path = "/{id}/itemphoto")
    public ItemPhoto saveItemPhoto(@RequestHeader String securityTokenValue, @RequestParam Long id, @RequestBody byte[] photo) {
        UUID securityToken = UUID.fromString(securityTokenValue);
        return itemPhotoService.saveItemPhoto(securityToken, id, photo);
    }
}
