package pw.react.flatly.flatlybackend.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pw.react.flatly.flatlybackend.utils.JsonLocalDateDeserializer;
import pw.react.flatly.flatlybackend.utils.JsonLocalDateSerializer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BookingList implements Serializable {

    @JsonSerialize
    private Long id;

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
    private Long item_id;

    public BookingList() {
    }

    public BookingList(Booking booking, Item item) {
        this.id = booking.getId();
        this.start_date = booking.getStart_date();
        this.end_date = booking.getEnd_date();
        this.title = item.getTitle();
        this.beds = item.getBeds();
        this.price = item.getPrice();
        this.item_id = item.getId();
    }
}
