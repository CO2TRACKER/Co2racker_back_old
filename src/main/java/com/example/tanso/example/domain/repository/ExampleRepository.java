package com.example.tanso.example.domain.repository;

import com.example.tanso.example.domain.model.Example;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExampleRepository extends JpaRepository<Example, Long> {

}
