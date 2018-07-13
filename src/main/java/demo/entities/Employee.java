package demo.entities;

import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    
    private String name;
    
    @NaturalId
    private Integer employeeNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bikeShop_id")
    private BikeShop bikeShop;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
