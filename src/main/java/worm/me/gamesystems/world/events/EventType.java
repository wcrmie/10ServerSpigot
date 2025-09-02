package worm.me.gamesystems.world.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public enum EventType {
    KOTH("King of the Hill", 600, 25);

    private final String name;
    private final int duration; // in seconds
    private final int weight; // weight for event selection

    EventType(String name, int duration, int weight) {
        this.name = name;
        this.duration = duration;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public int getWeight() {
        return weight;
    }
}
