package com.example.airline.repository;

import com.example.airline.model.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {

    List<Aircraft> findAllByAirlineNameContaining(String keyword); //try these things out and print coresponding sql
}
