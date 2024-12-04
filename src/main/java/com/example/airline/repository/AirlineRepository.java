package com.example.airline.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AirlineRepository extends JpaRepository<com.example.airline.model.Airline, Long> {
}
