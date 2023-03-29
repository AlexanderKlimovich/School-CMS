package com.klymovych.school.consoleViev.dao;
import com.klymovych.school.consoleViev.dao.implementsAll.DAOException;

import java.util.List;
import java.util.Optional;

public interface DAO <T>{
    Optional<T> getById(int id) throws DAOException;
    void save(T t) throws DAOException;
    void update(T t) throws DAOException;
    void deleteById(int id) throws DAOException;
    List<T> getAll() throws DAOException;
    void saveAll(List<T> t)throws DAOException;
}