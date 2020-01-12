package pw.react.flatly.flatlybackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private UUID Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="author_id")
    @JsonIgnore
    private User user;

    @Column(name = "start_date_time")
    private LocalDate StartDateTime;

    @Column(name = "end_date_time")
    private LocalDate EndDateTime;

    @ElementCollection
    @OneToMany(mappedBy="item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;

    @Column(name = "title")
    private String Title;

    @Column(name = "description")
    private String Description;

    @Column(name = "photo")
    private byte[] Photo;

    @Column(name = "room_number")
    private int RoomNumber;

    @Column(name = "beds")
    private int Beds;

    @Column(name = "price")
    private BigDecimal Price;

    @Column(name = "rating")
    private BigDecimal Rating;

    @Column(name = "city")
    private String City;

    @Column(name = "address")
    private String Address;

    @Column(name = "country")
    private String Country;

    public Item() {}
}
