package com.example.airline.controller;

import com.example.airline.model.Aircraft;
import com.example.airline.model.Airline;
import com.example.airline.security.JwtAuthenticated;
import com.example.airline.service.AircraftService;
import com.example.airline.service.AirlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@JwtAuthenticated
@RestController
@RequestMapping("/aircraft")
public class AircraftController {

    @Autowired
    private AircraftService aircraftService;

    @Autowired
    private AirlineService airlineService;

    @PostMapping("/")
    public ResponseEntity<?> createAircraft(@RequestBody Aircraft aircraft) {
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

        Aircraft savedAircraft = aircraftService.saveAircraft(aircraft);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAircraft);
    }

    @GetMapping("/")
    public List<Aircraft> getAllAircraft() {
        return aircraftService.getAllAircraft();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aircraft> getAircraftById(@PathVariable Long id) {
        Optional<Aircraft> aircraft = aircraftService.getAircraftById(id);
        return aircraft.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}/")
    public ResponseEntity<?> updateAircraft(
            @PathVariable Long id,
            @RequestBody Map<String, Object> fields) {

        Optional<Aircraft> existingAircraftOpt = aircraftService.getAircraftById(id);
        if (existingAircraftOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Aircraft existingAircraft = existingAircraftOpt.get();

        if (fields.containsKey("manufacturer_serial_number")) {
            Object serialNumberObj = fields.get("manufacturer_serial_number");
            if (serialNumberObj instanceof String) {
                existingAircraft.setManufacturerSerialNumber((String) serialNumberObj);
            } else {
                return ResponseEntity.badRequest().body("Invalid type for manufacturer_serial_number. It must be a string.");
            }
        } else {
            return ResponseEntity.badRequest().body("manufacturer_serial_number field is required for this operation.");
        }

        Aircraft updatedAircraft = aircraftService.saveAircraft(existingAircraft);

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
