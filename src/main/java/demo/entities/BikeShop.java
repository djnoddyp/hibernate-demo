package demo.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity(name = "BikeShop")
public class BikeShop extends BaseEntity {

    @Column(unique = true)
    private String address;

    // Bi-directional mapping, defined on child side
    @OneToMany(mappedBy = "bikeShop", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Bike> bikes = new HashSet<>();

    // Bi-directional mapping, defined on child side
    @OneToMany(mappedBy = "bikeShop", cascade = CascadeType.ALL)
    private Set<Employee> employees = new HashSet<>();

    public BikeShop(String address) {
        this.address = address;
    }

    public BikeShop() {
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

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        employee.setBikeShop(this);
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
        employee.setBikeShop(null);
    }
}
