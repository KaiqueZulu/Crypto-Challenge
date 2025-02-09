package com.matdev.repository;

import com.matdev.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSource {

    private static DataSource instance;
    private final Map<Long, User> userDatabase = new HashMap<>();

    public static DataSource getInstance() {
        if (instance == null) {
            synchronized (DataSource.class) {
                if (instance == null) {
                    instance = new DataSource();
                }
            }
        }
        return instance;
    }

    public void save(User user) {
        userDatabase.put(user.getId(), user);
    }

    public User findById(Long id) {
        return userDatabase.get(id);
    }

    public List<User> findAll() {
        return List.copyOf(userDatabase.values());
    }

    public boolean exists(Long id) {
        return userDatabase.containsKey(id);
    }

    public boolean delete(Long id) {
        return userDatabase.remove(id) != null;
    }
}
