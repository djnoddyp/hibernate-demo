package demo.entities;

import javax.persistence.Embeddable;

@Embeddable
public class BikeName {

    private String make;

    private String model;

    public BikeName(String make, String model) {
        this.make = make;
        this.model = model;
    }

    public BikeName() {
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
