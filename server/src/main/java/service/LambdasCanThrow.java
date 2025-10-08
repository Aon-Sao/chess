package service;

import dataaccess.DataAccessException;

@FunctionalInterface
public interface LambdasCanThrow<T> {
    void apply(T t) throws DataAccessException;
}
