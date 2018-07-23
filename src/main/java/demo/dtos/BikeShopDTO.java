package demo.dtos;

import demo.entities.Bike;


public class BikeShopDTO {

    private final Long id;

    private final String address;

    public BikeShopDTO(Long id, String address) {
        this.id = id;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }
}
