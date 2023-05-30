package ru.meowo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.meowo.model.User;

@Repository
public interface UserRepository extends MongoRepository<String, User> {
}
