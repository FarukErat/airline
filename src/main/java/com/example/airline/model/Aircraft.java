package com.example.airline.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("manufacturer_serial_number")
    private String manufacturerSerialNumber;

    private String type;
    private String model;

    @JsonProperty("number_of_engines")
    private int numberOfEngines;

    @ManyToOne
    @JoinColumn(name = "airline_id")
    @JsonIgnore // To prevent recursion when returning the Aircraft
    private Airline airline; // Relationship with the Airline entity

    @Transient // Temporary field for incoming string ID
    @JsonProperty("operator_airline")
    private String operatorAirlineId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManufacturerSerialNumber() {
        return manufacturerSerialNumber;
    }

    public void setManufacturerSerialNumber(String manufacturerSerialNumber) {
        this.manufacturerSerialNumber = manufacturerSerialNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getNumberOfEngines() {
        return numberOfEngines;
    }

    public void setNumberOfEngines(int numberOfEngines) {
        this.numberOfEngines = numberOfEngines;
    }

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    public String getOperatorAirlineId() {
        return operatorAirlineId;
    }

    public void setOperatorAirlineId(String operatorAirlineId) {
        this.operatorAirlineId = operatorAirlineId;
    }
}
