package ru.itpark.corporateEmail.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itpark.corporateEmail.entity.UserEntity;
import ru.itpark.corporateEmail.mapper.UserMapper;
import ru.itpark.corporateEmail.model.User;
import ru.itpark.corporateEmail.repository.DepartmentRepository;
import ru.itpark.corporateEmail.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final UserMapper userMapper;

    @Override
    public User getById(Integer userId) {
        UserEntity userEntity = userRepository.getOne(userId);
        return userMapper.userEntityToUser(userEntity);
    }

    @Override
    public User save(User user) {
        UserEntity userEntity;
        if (user.getId() != null) {
            userEntity = userMapper.userToUserEntity(user);
            userRepository.save(userEntity);
        } else {
            userEntity = userRepository.getOne(user.getId());
            userEntity.setDepartment(departmentRepository.findById(user.getDepartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("No Department found by id")));
            userEntity.setEmail(user.getEmail());
            userEntity.setLogin(user.getLogin());
            userEntity.setName(user.getName());
            userEntity = userRepository.save(userEntity);
        }
        return userMapper.userEntityToUser(userEntity);
    }

    @Override
    public void deleteById(Integer userId) {
        userRepository.deleteById(userId);
    }
}
