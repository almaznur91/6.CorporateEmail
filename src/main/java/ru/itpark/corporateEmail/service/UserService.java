package ru.itpark.corporateEmail.service;

import ru.itpark.corporateEmail.model.User;

public interface UserService {

    User getById(Integer userId);

    User save(User user);

    void deleteById(Integer userId);

}
