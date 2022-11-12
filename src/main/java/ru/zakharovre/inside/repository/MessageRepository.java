package ru.zakharovre.inside.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zakharovre.inside.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}

