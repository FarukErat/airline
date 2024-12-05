package com.example.airline.repository;

import com.example.airline.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepoistory extends JpaRepository<UserDetails, Long> {
}
