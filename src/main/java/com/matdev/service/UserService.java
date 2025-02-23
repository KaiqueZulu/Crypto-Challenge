package com.matdev.service;

import com.matdev.model.User;
import com.matdev.repository.DataSource;
import com.matdev.security.RSAEncrypt;

import java.security.PublicKey;
import java.util.List;
import java.util.NoSuchElementException;

public class UserService {
    private final DataSource dataSource = DataSource.getInstance();

    public User create(String rawUserInput, PublicKey publicKey) throws Exception {
        List<String> data = formatLine(rawUserInput);

        long id = Long.parseLong(data.get(0));
        String userDocument = RSAEncrypt.encrypt(data.get(1), publicKey);
        String creditCardToken = RSAEncrypt.encrypt(data.get(2), publicKey);
        long value = Long.parseLong(data.get(3));

        User user = new User(id, userDocument, creditCardToken, value);
        dataSource.save(user);
        return user;
    }

    public List<User> userList() {
        return dataSource.findAll();
    }

    public User update(Long id, String rawUserInput, PublicKey publicKey) throws Exception {
        if (!dataSource.exists(id)) {
            throw new NoSuchElementException("User not found with ID: " + id);
        }

        List<String> data = formatLine(rawUserInput);
        String userDocument = RSAEncrypt.encrypt(data.get(1), publicKey);
        String creditCardToken = RSAEncrypt.encrypt(data.get(2), publicKey);
        long value = Long.parseLong(data.get(2));

        User updatedUser = new User(id, userDocument, creditCardToken, value);
        dataSource.save(updatedUser);
        return updatedUser;
    }

    public boolean delete(Long id) {
        return dataSource.delete(id);
    }

    private List<String> formatLine(String rawUserInput) {
        return List.of(rawUserInput.split(","));
    }
}
