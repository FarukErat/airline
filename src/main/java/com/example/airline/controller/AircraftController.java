package com.example.airline.controller;

import com.example.airline.model.Aircraft;
import com.example.airline.service.AircraftService;
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

    @PostMapping("/")
    public ResponseEntity<Aircraft> createAircraft(@RequestBody Aircraft aircraft) {
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

    @PatchMapping("/{id}")
    public ResponseEntity<Aircraft> updateAircraft(@PathVariable Long id, @RequestBody Aircraft aircraft) {
        if (!aircraftService.getAircraftById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        aircraft.setId(id);
        Aircraft updatedAircraft = aircraftService.saveAircraft(aircraft);
        return ResponseEntity.ok(updatedAircraft);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAircraft(@PathVariable Long id) {
        if (!aircraftService.getAircraftById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        aircraftService.deleteAircraft(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
