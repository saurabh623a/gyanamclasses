package com.gyanam.gyanamclasses.repository;

import com.gyanam.gyanamclasses.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
}
