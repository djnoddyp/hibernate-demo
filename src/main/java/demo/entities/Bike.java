package demo.entities;

import demo.enums.Style;

import javax.persistence.*;

@Entity(name = "Bike")
public class Bike {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Embedded
    private BikeName bikeName;

    private Integer gears;

    @Enumerated
    private Style style;

    @ManyToOne
    private BikeShop bikeShop;

    public Bike(BikeName bikeName, Integer gears, Style style) {
        this.bikeName = bikeName;
        this.gears = gears;
        this.style = style;
    }

    public Bike() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BikeName getBikeName() {
        return bikeName;
    }

    public void setBikeName(BikeName bikeName) {
        this.bikeName = bikeName;
    }

    public Integer getGears() {
        return gears;
    }

    public void setGears(Integer gears) {
        this.gears = gears;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public BikeShop getBikeShop() {
        return bikeShop;
    }

    public void setBikeShop(BikeShop bikeShop) {
        this.bikeShop = bikeShop;
    }
}
