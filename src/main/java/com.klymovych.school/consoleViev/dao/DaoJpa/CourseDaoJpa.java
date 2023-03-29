package com.klymovych.school.consoleViev.dao.DaoJpa;

import com.klymovych.school.consoleViev.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseDaoJpa extends JpaRepository<Course, Integer> {
}
