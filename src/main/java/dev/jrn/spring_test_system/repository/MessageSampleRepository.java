package dev.jrn.spring_test_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.jrn.spring_test_system.entity.MessageSample;

@Repository
public interface MessageSampleRepository extends JpaRepository<MessageSample, String> { }
