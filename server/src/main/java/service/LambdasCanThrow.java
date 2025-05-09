package service;

import dataaccess.DataAccessException;

@FunctionalInterface
public interface LambdasCanThrow<T, R> {
    R apply(T t) throws DataAccessException;
}
