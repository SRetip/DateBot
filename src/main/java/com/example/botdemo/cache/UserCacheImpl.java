package com.example.botdemo.cache;

import com.example.botdemo.domain.Usser;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserCacheImpl implements Cache<Usser> {

    private final Map<Long, Usser> users;

    public UserCacheImpl() {
        this.users = new HashMap<>();
    }

    @Override
    public void add(Usser usser) {
        users.put(usser.getId(), usser);
    }

    @Override
    public Usser getById(Long id) {
        return users.get(id);
    }

    @Override
    public void removeById(Long id) {
        users.remove(id);
    }

    @Override
    public List<Usser> getAll() {
        return new ArrayList<>(users.values());
    }
}
