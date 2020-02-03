package pw.react.flatly.flatlybackend.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.sun.deploy.security.BadCertificateDialog;
import pw.react.flatly.flatlybackend.utils.JsonLocalDateDeserializer;
import pw.react.flatly.flatlybackend.utils.JsonLocalDateSerializer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BookingDetails implements Serializable {

    @JsonSerialize
    private Long id;

    @JsonSerialize
    private String description;

    @JsonSerialize
    private Integer people;

    @JsonSerialize
    private String name;

    @JsonSerialize
    private String last_name;

    @JsonSerialize
    private String email;

    @JsonDeserialize(using = JsonLocalDateDeserializer.class)
    @JsonSerialize(using = JsonLocalDateSerializer.class)
    private LocalDate start_date;

    @JsonDeserialize(using = JsonLocalDateDeserializer.class)
    @JsonSerialize(using = JsonLocalDateSerializer.class)
    private LocalDate end_date;

    @JsonSerialize
    private String title;

    @JsonSerialize
    private Integer beds;

    @JsonSerialize
    private BigDecimal price;

    @JsonSerialize
    private String city;

    @JsonSerialize
    private String address;

    @JsonSerialize
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}