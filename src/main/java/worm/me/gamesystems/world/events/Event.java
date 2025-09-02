package worm.me.gamesystems.world.events;

public class Event {

    private final EventType type;


    public Event(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }
}
