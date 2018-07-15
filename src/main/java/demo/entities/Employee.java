package demo.entities;

import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "Employee")
public class Employee extends BaseEntity {
    
    private String name;
    
    @NaturalId
    private Integer employeeNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bikeShop_id")
    private BikeShop bikeShop;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(Integer employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public BikeShop getBikeShop() {
        return bikeShop;
    }

    public void setBikeShop(BikeShop bikeShop) {
        this.bikeShop = bikeShop;
    }
}
