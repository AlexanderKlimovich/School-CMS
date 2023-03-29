package com.klymovych.school.consoleViev.dao.implementsAll;

import com.klymovych.school.consoleViev.dao.DaoJpa.GroupDaoJpa;
import com.klymovych.school.consoleViev.dao.GroupDAO;
import com.klymovych.school.consoleViev.model.Group;
import com.klymovych.school.consoleViev.model.Student;
import com.klymovych.school.consoleViev.validators.GroupValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class JPAGroupDAO implements GroupDAO {

    private final GroupDaoJpa dao;

    private final GroupValidator groupValidator;

    @Autowired
    public JPAGroupDAO(GroupValidator groupValidator, GroupDaoJpa dao) {
        this.groupValidator = groupValidator;
        this.dao = dao;
    }

    @Override
    public Optional<Group> getById(int id) throws DAOException {
        return dao.findById(id);
    }

    @Override
    public void save(Group group) throws DAOException {
        groupValidator.validate(group);
        dao.save(group);
    }

    @Override
    public void saveAll(List<Group> groups) throws DAOException {
        groupValidator.validate(groups);
        dao.saveAll(groups);
    }

    @Override
    public void update(Group updatedGroup) throws DAOException {
        groupValidator.validate(updatedGroup);
        if (getById(updatedGroup.getId()).isPresent()) {
            dao.save(updatedGroup);
        } else {
            throw new DAOException("No group in database with this id " + updatedGroup.getId());
        }
    }

    @Override
    public void deleteById(int id) throws DAOException {
        if (getById(id).isPresent()) {
            dao.deleteById(id);
        } else {
            throw new DAOException("No group in database with this id " + id);
        }
    }

    @Override
    public List<Group> getAll() throws DAOException {
        return dao.findAll();
    }
    @Override
    public List<Group> getGroupsWithSameOrFewerAmountOfStudents(int amountOfStudents) throws DAOException {
        return dao.findByAmountOfStudents(amountOfStudents);
    }
    @Override
    public List<Student> getStudentsFromGroup(int id) throws DAOException {
        if (getById(id).isPresent()) {
            return getById(id).get().getStudents();
        } else {
            throw new DAOException("No group in database with this id " + id);
        }
    }
}
