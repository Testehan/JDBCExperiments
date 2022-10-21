package com.testehan.database.postgresql.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> get(int id);

    List<T> getAll();

    int save(T t);

    void update(T t);

    void delete(T t);
}
