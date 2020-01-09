package pw.react.flatly.flatlybackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pw.react.flatly.flatlybackend.service.BookingService;
import pw.react.flatly.flatlybackend.service.ItemService;
import pw.react.flatly.flatlybackend.service.UserService;

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
}
