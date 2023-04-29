package ru.yandex.practicum.filmorate.model.feed;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Event {
    int eventId;
    long timestamp;
    int userId;
    EventType eventType;
    EventOperation operation;
    int entityId;
}
