package com.example.airline.controller;

import com.example.airline.model.Aircraft;
import com.example.airline.model.Airline;
import com.example.airline.service.AircraftService;
import com.example.airline.service.AirlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/aircraft")
public class AircraftController {

    @Autowired
    private AircraftService aircraftService;

    @Autowired
    private AirlineService airlineService;

    @PostMapping("/")
    public ResponseEntity<?> createAircraft(@RequestBody Aircraft aircraft) {
        // Handle operator_airline
        if (aircraft.getOperatorAirlineId() != null && !aircraft.getOperatorAirlineId().isEmpty()) {
            try {
                Long airlineId = Long.parseLong(aircraft.getOperatorAirlineId());
                Optional<Airline> airline = airlineService.getAirlineById(airlineId);
                if (airline.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Invalid operator_airline: Airline with ID " + airlineId + " does not exist.");
                }
                aircraft.setAirline(airline.get());
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid operator_airline: Must be a numeric ID.");
            }
        } else {
            aircraft.setAirline(null);
        }

        // Save the aircraft
        Aircraft savedAircraft = aircraftService.saveAircraft(aircraft);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAircraft);
    }

    @GetMapping("/")
    public List<Aircraft> getAllAircraft() {
        return aircraftService.getAllAircraft();
    }

    @GetMapping("/{id}/")
    public ResponseEntity<Aircraft> getAircraftById(@PathVariable Long id) {
        Optional<Aircraft> aircraft = aircraftService.getAircraftById(id);
        return aircraft.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}/")
    public ResponseEntity<Aircraft> updateAircraft(@PathVariable Long id, @RequestBody Aircraft aircraft) {
        if (!aircraftService.getAircraftById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        aircraft.setId(id);
        Aircraft updatedAircraft = aircraftService.saveAircraft(aircraft);
        return ResponseEntity.ok(updatedAircraft);
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> deleteAircraft(@PathVariable Long id) {
        if (!aircraftService.getAircraftById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        aircraftService.deleteAircraft(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
