package com.example.airline.service;

import com.example.airline.repository.AirlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirlineService {

    @Autowired
    private AirlineRepository airlineRepository;

    public List<com.example.airline.model.Airline> getAllAirlines() {
        return airlineRepository.findAll();
    }

    public Optional<com.example.airline.model.Airline> getAirlineById(Long id) {
        return airlineRepository.findById(id);
    }

    public com.example.airline.model.Airline saveAirline(com.example.airline.model.Airline airline) {
        return airlineRepository.save(airline);
    }

    public void deleteAirline(Long id) {
        airlineRepository.deleteById(id);
    }
}
