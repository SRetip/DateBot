package com.example.botdemo.cache;

import java.util.List;

public interface Cache<T> {
    void add(T t);

    T getById(Long id);

    void removeById(Long id);

    List<T> getAll();
}
