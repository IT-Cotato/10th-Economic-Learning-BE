package com.ripple.BE.notification.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class EmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    public Map<String, SseEmitter> findAllEmitterByUserId(final long userId) {
        String userIdStr = String.valueOf(userId);

        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userIdStr))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<String, Object> findAllEventCacheByUserId(final long userId) {
        String userIdStr = String.valueOf(userId);

        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userIdStr))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void deleteEmitterById(String id) {
        emitters.remove(id);
    }

    public void deleteAllEmitterById(String id) {
        emitters.keySet().removeIf(key -> key.startsWith(id));
    }

    public void deleteAllEventCacheById(String id) {
        eventCache.keySet().removeIf(key -> key.startsWith(id));
    }
}
