package ru.itpark.corporateEmail.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itpark.corporateEmail.entity.UserEntity;
import ru.itpark.corporateEmail.model.User;
import ru.itpark.corporateEmail.repository.DepartmentRepository;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final DepartmentRepository departmentRepository;

    public UserEntity userToUserEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .department(departmentRepository.findById(user.getDepartmentId())
                        .orElseThrow(() -> new IllegalArgumentException("No Department found by id")))
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .build();
    }

    public User userEntityToUser(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .departmentId(userEntity.getDepartment().getId())
                .email(userEntity.getEmail())
                .login(userEntity.getLogin())
                .name(userEntity.getName())
                .build();

    }

}
