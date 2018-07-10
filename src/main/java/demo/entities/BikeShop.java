package demo.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class BikeShop {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String address;

    @OneToMany(mappedBy = "bikeShop", cascade = CascadeType.ALL)
    private Set<Bike> bikes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Bike> getBikes() {
        return bikes;
    }

    public void setBikes(Set<Bike> bikes) {
        this.bikes = bikes;
    }

    public void addBike(Bike bike) {
        bikes.add(bike);
        bike.setBikeShop(this);
    }

    public void removeBike(Bike bike) {
        bikes.remove(bike);
        bike.setBikeShop(null);
    }
}
