package ru.itpark.corporateEmail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itpark.corporateEmail.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
}
