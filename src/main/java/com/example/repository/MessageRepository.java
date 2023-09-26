package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query(value = "SELECT * FROM message WHERE posted_by = :posted_by", nativeQuery = true)
    public List<Message> findMessageByPostedBy(@Param("posted_by") int posted_by);
}
