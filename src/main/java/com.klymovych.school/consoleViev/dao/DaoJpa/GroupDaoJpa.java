package com.klymovych.school.consoleViev.dao.DaoJpa;

import com.klymovych.school.consoleViev.dao.implementsAll.DAOException;
import com.klymovych.school.consoleViev.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GroupDaoJpa extends JpaRepository<Group, Integer> {
    @Query("from Group g left join g.students s group by g having count(s) <= :amount")
    List<Group> findByAmountOfStudents(@Param("amount") int maxStudents) throws DAOException;
}
