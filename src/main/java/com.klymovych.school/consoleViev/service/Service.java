package com.klymovych.school.consoleViev.service;

import java.util.List;
import java.util.Optional;

public interface Service<T> {
    Optional<T> getById(int id) throws  ServiceException;
    void save(T t) throws ServiceException;
    void update( T t) throws ServiceException;
    void deleteById(int id) throws ServiceException;
    List<T> getAll() throws ServiceException;
    void saveAll(List<T> t) throws ServiceException;
}
