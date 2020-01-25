package pw.react.flatly.flatlybackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity addItem(@RequestHeader UUID security_token, @RequestBody Item item) {
        Item savedItem = itemService.save(security_token, item);

        if(savedItem!=null) return ResponseEntity.ok(savedItem);
        else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("something went wrong");
    }

    // 4 - Get all items

    @GetMapping(path = "/items")
    public ResponseEntity getItems(
            @RequestHeader UUID security_token,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Integer people,
            @RequestParam(required = false) Long authorId,

            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String dir) {

        return ResponseEntity.ok(itemService.findAll(security_token, dateFrom, dateTo, city, people, authorId, sort, dir));
    }

    @GetMapping(path = "/items/{id}/vacant")
    public ResponseEntity getVacantById(@RequestHeader UUID security_token, @PathVariable("id") Long id) {

        return ResponseEntity.ok(itemService.findVacantById(security_token, id));
    }

    // 5 - Get specific item

    @GetMapping(path = "/items/{id}")
    public ResponseEntity getItem(@RequestHeader UUID security_token, @PathVariable("id") Long id) {
        Item item = itemService.findById(security_token, id);

        if(item!=null) return ResponseEntity.ok(item);
        else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("something went wrong");
    }

    // Get specific booking

    @GetMapping(path="book/{id}")
    public ResponseEntity getBooking(@RequestHeader UUID security_token, @PathVariable("id") Long id) {
        return ResponseEntity.ok(bookingService.getBooking(security_token, id));
    }

    @GetMapping(path = "bookingdetails/{id}")
    public ResponseEntity getBookingDetails(@RequestHeader UUID security_token, @PathVariable("id") Long id) {
        return ResponseEntity.ok(bookingService.getBookingDetails(security_token, id));
    }


    // 6 - Delete specific item

    @DeleteMapping(path = "/item/{id}")
    public ResponseEntity deleteItem(@RequestHeader UUID security_token, @PathVariable("id") Long id) {
        itemService.deleteById(security_token, id);
        return ResponseEntity.ok("Item deleted");
    }

    // 7 - Update specific item

    @PutMapping(path = "/item/{id}")
    public ResponseEntity updateItem(@RequestHeader UUID security_token, @PathVariable("id") Long id, @RequestBody Item itemDetails) {

        return ResponseEntity.ok(itemService.updateById(security_token, id, itemDetails));
    }

    // 8 - Book specific item

    @PostMapping(path = "/book/{item_id}")
    public ResponseEntity postBooking(@RequestHeader UUID security_token, @PathVariable("id") Long item_id, @RequestBody Booking booking) {

        return ResponseEntity.ok(bookingService.addBooking(security_token, item_id, booking));
    }

    // 9 - Release specific item

    @DeleteMapping(path = "/cancel/{id}")
    public ResponseEntity deleteBooking(@RequestHeader UUID security_token, @PathVariable("id") Long id) {
        Booking booking = bookingService.deleteBooking(security_token, id);

        return ResponseEntity.ok(booking);
    }

    // wszystkie rezerwacje danego autora

    @GetMapping(path = "user/{id}/book")
    public ResponseEntity getAllBooksByUserId(@RequestHeader UUID security_token, @PathVariable("id") Long id) {
        return ResponseEntity.ok(bookingService.findAllByUserId(security_token, id));
    }

    @GetMapping(path="user/{id}/bookinglist")
    public ResponseEntity getBookingList(@RequestHeader UUID security_token, @PathVariable("id") Long id) {
        return ResponseEntity.ok(bookingService.findAllBookingListByUserId(security_token, id));
    }





    // Przesyłanie obrazka

    @GetMapping(path = "/itemphoto/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getItemPhoto(@PathVariable("id") Long id) throws IOException {
        ItemPhoto itemPhoto = itemPhotoService.findItemPhotoByItemId(id);

        return itemPhoto.getPhoto();
    }

    @PostMapping(path = "/{id}/itemphoto")
    public ItemPhoto saveItemPhoto(@RequestHeader UUID security_token, @PathVariable Long id, @RequestBody byte[] photo) {
        return itemPhotoService.saveItemPhoto(security_token, id, photo);
    }
}
