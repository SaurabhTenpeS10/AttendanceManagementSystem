package com.saurabh.ams.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saurabh.ams.model.Student;
import com.saurabh.ams.model.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long>{

	Optional<Student> findByEmail(String email);
}
