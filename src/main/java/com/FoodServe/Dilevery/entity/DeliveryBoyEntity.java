package com.FoodServe.Dilevery.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "dilevery")
public class DeliveryBoyEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Phone")
    private String phone;

    // ✅ Renamed from "isAvailable" to "available"
    @Column(name = "Available")
    private boolean available;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    // ✅ Getter still named isAvailable() — Java convention for boolean
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}