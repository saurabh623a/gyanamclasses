package com.gyanam.gyanamclasses.repository;

import com.gyanam.gyanamclasses.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseRepository extends MongoRepository<Course, String> {
}
