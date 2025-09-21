package com.myproject.selfService.domain;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class WeightEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double weight;          // peso em kg
    private double value;           // valor em R$
    private LocalDateTime date;

    public WeightEntry(double weight, double value, LocalDateTime date) {
        this.weight = weight;
        this.value = value;
        this.date = date;
    }

    public Long getId() {
        return id;
    }


    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
