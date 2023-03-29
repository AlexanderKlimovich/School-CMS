package com.klymovych.school.consoleViev.service.implementsAllinterfases;

import com.klymovych.school.consoleViev.dao.GroupDAO;
import com.klymovych.school.consoleViev.dao.implementsAll.DAOException;
import com.klymovych.school.consoleViev.model.Group;
import com.klymovych.school.consoleViev.service.GroupService;
import com.klymovych.school.consoleViev.service.ServiceException;
import com.klymovych.school.consoleViev.validators.GroupValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {
    private final GroupDAO groupDAO;

    private final GroupValidator groupValidator;

    @Autowired
    public GroupServiceImpl(GroupDAO GroupDAO, GroupValidator groupValidator) {
        this.groupDAO = GroupDAO;
        this.groupValidator = groupValidator;
    }


    @Override
    public Optional<Group> getById(int id) throws ServiceException {
        try {
            return groupDAO.getById(id);
        } catch (DAOException e) {
            throw new ServiceException("Cannot get group by this id",e);
        }
    }

    @Override
    public void save(Group group) throws ServiceException {
        groupValidator.validate(group);
        try {
            groupDAO.save(group);
        } catch (DAOException e) {
            throw new ServiceException("Cannot save data about this group",e);
        }
    }

    @Override
    public void saveAll(List<Group> groups) throws ServiceException {
        groupValidator.validate(groups);
        try {
            groupDAO.saveAll(groups);
        } catch (DAOException e) {
            throw new ServiceException("Cannot save data about this groups",e);
        }
    }

    @Override
    public void update(Group group) throws ServiceException {
        groupValidator.validate(group);
        try {
            groupDAO.update(group);
        } catch (DAOException e) {
            throw new ServiceException("Cannot update data to this group",e);
        }
    }

    @Override
    public void deleteById(int id) throws ServiceException {
        try {
            groupDAO.deleteById(id);
        } catch (DAOException e) {
            throw new ServiceException("Cannot delete data about this group",e);
        }
    }

    @Override
    public List<Group> getGroupsWithSameOrFewerAmountOfStudents(int amountOfStudents) throws ServiceException {
        try {
            return groupDAO.getGroupsWithSameOrFewerAmountOfStudents(amountOfStudents);
        } catch (DAOException e) {
            throw new ServiceException("Cannot take data about this groups wit same or fewer amount of students",e);
        }
    }

    @Override
    public List<Group> getAll() throws ServiceException {
        try {
            return groupDAO.getAll();
        } catch (DAOException e) {
            throw new ServiceException("Cannot take data about this groups",e);
        }
    }
}
