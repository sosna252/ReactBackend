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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/")
@CrossOrigin
public class Controller {
    UserService userService;
    ItemService itemService;
    BookingService bookingService;
    ItemPhotoService itemPhotoService;

    UserRepository userRepository;

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
    public ResponseEntity addItem(@RequestBody Item item) {
        Item savedItem = itemService.save(item);

        if(savedItem!=null) return ResponseEntity.ok(savedItem);
        else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("something went wrong");
    }

    // 4 - Get all items

    @GetMapping(path = "/items")
    public ResponseEntity getItems(
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Integer people,
            @RequestParam(required = false) Long authorId) {

        return ResponseEntity.ok(itemService.findByParams(dateFrom, dateTo, city, people, authorId));
    }

    @GetMapping(path = "/items/{id}/vacant")
    public ResponseEntity getVacantById(@PathVariable("id") Long id) {

        return ResponseEntity.ok(itemService.findVacantById(id));
    }

    // 5 - Get specific item

    @GetMapping(path = "/items/{id}")
    public ResponseEntity getItem(@PathVariable("id") Long id) {
        Item item = itemService.findById(id);

        if(item!=null) return ResponseEntity.ok(item);
        else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("something went wrong");
    }

    // 6 - Delete specific item

    @DeleteMapping(path = "/item/{id}")
    public ResponseEntity deleteItem(@PathVariable("id") Long id) {
        itemService.deleteById(id);
        return ResponseEntity.ok("Item deleted");
    }

    // 7 - Update specific item

    @PutMapping(path = "/item/{id}")
    public ResponseEntity updateItem(@PathVariable("id") Long id, @RequestBody Item itemDetails) {

        return ResponseEntity.ok(itemService.updateById(id, itemDetails));
    }

    // 8 - Book specific item

    @PostMapping(path = "/book/{item_id}")
    public ResponseEntity postBooking(@PathVariable("id") Long item_id, @RequestBody Booking booking) {

        return ResponseEntity.ok(bookingService.addBooking(item_id, booking));
    }

    // 9 - Release specific item

    @DeleteMapping(path = "/cancel/{id}")
    public ResponseEntity deleteBooking(@PathVariable("id") Long id) {
        Booking booking = bookingService.deleteBooking(id);

        return ResponseEntity.ok(booking);
    }





    // Przesyłanie obrazka

    @GetMapping(path = "/itemphoto/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getItemPhoto(@PathVariable("id") Long id) throws IOException {
        ItemPhoto itemPhoto = itemPhotoService.findItemPhotoByItemId(id);

        return itemPhoto.getPhoto();
    }
}
