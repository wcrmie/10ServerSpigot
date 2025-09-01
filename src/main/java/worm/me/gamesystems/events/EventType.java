package worm.me.gamesystems.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EventType {
    KOTH("King of the Hill", 600, 25);

    private final String name;
    private final int duration; // in seconds
    private final int weight; // weight for event selection
}
