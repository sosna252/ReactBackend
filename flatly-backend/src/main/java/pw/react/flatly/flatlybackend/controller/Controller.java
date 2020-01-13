package pw.react.flatly.flatlybackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.react.flatly.flatlybackend.model.Item;
import pw.react.flatly.flatlybackend.service.BookingService;
import pw.react.flatly.flatlybackend.service.ItemService;
import pw.react.flatly.flatlybackend.service.UserService;

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

    public Controller(UserService userService, ItemService itemService, BookingService bookingService) {
        this.userService = userService;
        this.itemService = itemService;
        this.bookingService = bookingService;
    }

    // 2 - Login to system

    @PostMapping(path = "/login")
    public ResponseEntity<UUID> login(@RequestBody String login, @RequestBody String password) {
        UUID uuid = userService.login(login, password);

        if(uuid!=null) return new ResponseEntity(uuid, HttpStatus.OK);
        else return new ResponseEntity("wrong login or password", HttpStatus.UNAUTHORIZED);
    }

    // 3 - Adding new item

    @PostMapping(path = "/items")
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        Item savedItem = itemService.save(item);

        if(savedItem!=null) return new ResponseEntity(savedItem, HttpStatus.OK);
        else return new ResponseEntity("something went wrong", HttpStatus.FORBIDDEN);
    }

    // 4 - Get all items

    @GetMapping(path = "/items")
    public ResponseEntity<List<Item>> getItems(
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Integer people,
            @RequestParam(required = false) UUID authorId) {
        List<Item> items =  itemService.findByParams(dateFrom, dateTo, city, people, authorId);

        if(items!=null) return new ResponseEntity(items, HttpStatus.OK);
        else return new ResponseEntity("something went wrong", HttpStatus.FORBIDDEN);
    }

    @GetMapping(path = "/items/{id}/vacant")
    public ResponseEntity<List<List<LocalDate>>> getVacantById(@PathVariable("id") UUID id) {
        List<List<LocalDate>> dates =  itemService.findVacantById(id);

        return new ResponseEntity(dates, HttpStatus.OK);
    }

    // 5 - Get specific item

    @GetMapping(path = "/items/{id}")
    public ResponseEntity<Item> getItem(@PathVariable("id") UUID id) {
        Item item = itemService.findById(id);

        if(item!=null) return new ResponseEntity(item, HttpStatus.OK);
        else return new ResponseEntity("", HttpStatus.FORBIDDEN);
    }

    // 6 - Delete specific item

    @DeleteMapping(path = "/item/{id}")
    public ResponseEntity deleteItem(@PathVariable("id") UUID id) {
        if(itemService.deleteById(id)) return new ResponseEntity("Item deleted", HttpStatus.OK);
        else return new ResponseEntity("something went wrong", HttpStatus.FORBIDDEN);
    }
}
