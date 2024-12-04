package com.example.airline.controller;

import com.example.airline.model.Airline;
import com.example.airline.service.AirlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/airline")
public class AirlineController {

    @Autowired
    private AirlineService airlineService;

    @PostMapping("/")
    public ResponseEntity<Airline> createAirline(@RequestBody Airline airline) {
        Airline savedAirline = airlineService.saveAirline(airline);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAirline);
    }

    @GetMapping("/")
    public List<Airline> getAllAirlines() {
        return airlineService.getAllAirlines();
    }

    @GetMapping("/{id}/")
    public ResponseEntity<Airline> getAirlineById(@PathVariable Long id) {
        Optional<Airline> airline = airlineService.getAirlineById(id);
        return airline.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}/")
    public ResponseEntity<Airline> updateAirline(@PathVariable Long id, @RequestBody Map<String, Integer> fields) {
        Optional<Airline> existingAirlineOpt = airlineService.getAirlineById(id);
        if (!existingAirlineOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Airline existingAirline = existingAirlineOpt.get();

        if (fields.containsKey("founded_year")) {
            Integer foundedYear = fields.get("founded_year");
            if (foundedYear != null && foundedYear > 0) {
                existingAirline.setFoundedYear(foundedYear);
            }
        }

        Airline updatedAirline = airlineService.saveAirline(existingAirline);
        return ResponseEntity.ok(updatedAirline);
    }


    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> deleteAirline(@PathVariable Long id) {
        if (!airlineService.getAirlineById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        airlineService.deleteAirline(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

