package pw.react.flatly.flatlybackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pw.react.flatly.flatlybackend.utils.JsonLocalDateDeserializer;
import pw.react.flatly.flatlybackend.utils.JsonLocalDateSerializer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="author_id")
    @JsonIgnore
    private User user;

    @Column
    @JsonDeserialize(using = JsonLocalDateDeserializer.class)
    @JsonSerialize(using = JsonLocalDateSerializer.class)
    private LocalDate start_date_time;

    @Column
    @JsonDeserialize(using = JsonLocalDateDeserializer.class)
    @JsonSerialize(using = JsonLocalDateSerializer.class)
    private LocalDate end_date_time;

    @ElementCollection
    @OneToMany(mappedBy="item", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookings;

    @Column
    private String title;

    @Column
    private String description;

    @JsonIgnore
    private byte[] photo;

    @Column
    private Integer room_number;

    @Column
    private Integer beds;

    @Column
    private BigDecimal price;

    @Column
    private BigDecimal rating;

    @Column
    private String city;

    @Column
    private String address;

    @Column
    private String country;

    public Item() {}

    public Item(LocalDate start_date_time, LocalDate end_date_time, Integer beds, BigDecimal price, String city) {
        this.start_date_time = start_date_time;
        this.end_date_time = end_date_time;
        this.beds = beds;
        this.price = price;
        this.city = city;
    }

    public void setValues(Item item) {
        this.start_date_time = item.getStart_date_time();
        this.end_date_time = item.end_date_time;
        this.title = item.getTitle();
        this.description = item.getDescription();
        this.room_number = item.getRoom_number();
        this.beds = item.getBeds();
        this.price = item.getPrice();
        this.rating = item.getRating();
        this.address = item.getAddress();
        this.city = item.getCity();
        this.country = item.getCountry();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getStart_date_time() {
        return start_date_time;
    }

    public void setStart_date_time(LocalDate start_date_time) {
        this.start_date_time = start_date_time;
    }

    public LocalDate getEnd_date_time() {
        return end_date_time;
    }

    public void setEnd_date_time(LocalDate end_date_time) {
        this.end_date_time = end_date_time;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Integer getRoom_number() {
        return room_number;
    }

    public void setRoom_number(Integer room_number) {
        this.room_number = room_number;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
