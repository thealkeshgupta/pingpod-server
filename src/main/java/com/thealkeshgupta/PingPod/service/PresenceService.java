package com.thealkeshgupta.PingPod.service;


import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PresenceService {

    // Map<roomId, Set<usernames>>
    private final Map<Long, Set<Long>> roomActiveUsers = new HashMap<>();

    public synchronized void userJoined(Long roomId, Long username) {
        roomActiveUsers.computeIfAbsent(roomId, k -> new HashSet<>()).add(username);
    }

    public synchronized void userLeft(Long roomId, Long username) {
        if (roomActiveUsers.containsKey(roomId)) {
            roomActiveUsers.get(roomId).remove(username);
            if (roomActiveUsers.get(roomId).isEmpty()) {
                roomActiveUsers.remove(roomId);
            }
        }
    }

    public synchronized Set<Long> getActiveUsers(Long roomId) {
        return roomActiveUsers.getOrDefault(roomId, new HashSet<>());
    }
}
