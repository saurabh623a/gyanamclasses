package com.gyanam.gyanamclasses.repository;

import com.gyanam.gyanamclasses.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    List<Student> findByTotalOutstandingFeesGreaterThan(double amount);
}
