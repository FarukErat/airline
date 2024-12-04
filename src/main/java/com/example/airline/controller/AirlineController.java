package com.example.airline.controller;

import com.example.airline.service.AirlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/airlines")
public class AirlineController {

    @Autowired
    private AirlineService airlineService;

    @GetMapping
    public List<com.example.airline.model.Airline> getAllAirlines() {
        return airlineService.getAllAirlines();
    }

    @GetMapping("/{id}")
    public ResponseEntity<com.example.airline.model.Airline> getAirlineById(@PathVariable Long id) {
        Optional<com.example.airline.model.Airline> airline = airlineService.getAirlineById(id);
        return airline.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<com.example.airline.model.Airline> createAirline(@RequestBody com.example.airline.model.Airline airline) {
        com.example.airline.model.Airline savedAirline = airlineService.saveAirline(airline);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAirline);
    }

    @PutMapping("/{id}")
    public ResponseEntity<com.example.airline.model.Airline> updateAirline(@PathVariable Long id, @RequestBody com.example.airline.model.Airline airline) {
        if (!airlineService.getAirlineById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        airline.setId(id);
        com.example.airline.model.Airline updatedAirline = airlineService.saveAirline(airline);
        return ResponseEntity.ok(updatedAirline);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirline(@PathVariable Long id) {
        if (!airlineService.getAirlineById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        airlineService.deleteAirline(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

