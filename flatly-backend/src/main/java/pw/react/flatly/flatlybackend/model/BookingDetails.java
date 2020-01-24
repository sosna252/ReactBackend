package pw.react.flatly.flatlybackend.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sun.deploy.security.BadCertificateDialog;
import pw.react.flatly.flatlybackend.utils.JsonLocalDateDeserializer;
import pw.react.flatly.flatlybackend.utils.JsonLocalDateSerializer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BookingDetails implements Serializable {

    private Long id;

    private String description;

    private Integer people;

    private String name;

    private String last_name;

    private String email;

    @JsonDeserialize(using = JsonLocalDateDeserializer.class)
    @JsonSerialize(using = JsonLocalDateSerializer.class)
    private LocalDate start_date;

    @JsonDeserialize(using = JsonLocalDateDeserializer.class)
    @JsonSerialize(using = JsonLocalDateSerializer.class)
    private LocalDate end_date;

    private String title;

    private Integer beds;

    private BigDecimal price;

    private String city;

    private String address;

    private String country;

    public BookingDetails() {
    }

    public BookingDetails(Booking booking, Item item) {
        this.id = booking.getId();
        this.description = item.getDescription();
        this.people = booking.getPeople();
        this.name = booking.getName();
        this.last_name = booking.getLast_name();
        this.email = booking.getEmail();
        this.start_date = booking.getStart_date();
        this.end_date = booking.getEnd_date();
        this.title = item.getTitle();
        this.beds = item.getBeds();
        this.price = item.getPrice();
        this.city = item.getCity();
        this.address = item.getAddress();
        this.country = item.getCountry();
    }
}