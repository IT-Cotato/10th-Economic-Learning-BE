package com.ripple.BE.chatbot.repository;

import com.ripple.BE.chatbot.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatbotRepository extends JpaRepository<ChatMessage, Long> {}
