package demo.entities;

import demo.enums.Style;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity(name = "Bike")
public class Bike extends BaseEntity {

    @Embedded
    private BikeName bikeName;

    private Integer gears;

    @Enumerated
    private Style style;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bikeShop_id")
    private BikeShop bikeShop;
    
    private Double price;
    
    @Formula("price * 1.31")
    private Double priceInDollars;

    public Bike(BikeName bikeName, Integer gears, Style style) {
        this.bikeName = bikeName;
        this.gears = gears;
        this.style = style;
    }

    public Bike() {
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPriceInDollars() {
        return priceInDollars;
    }

    public void setPriceInDollars(Double priceInDollars) {
        this.priceInDollars = priceInDollars;
    }
}
