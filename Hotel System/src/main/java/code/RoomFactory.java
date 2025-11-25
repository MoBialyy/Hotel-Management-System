package main.java.code;
import java.util.ArrayList;
import java.util.List;

public class RoomFactory {

    // Factory method to create a single room
    private static Room createRoom(int roomNumber, String type) {
        return switch (type) {
            case "Single" -> new Room(roomNumber, "Single", 100); // price example
            case "Double" -> new Room(roomNumber, "Double", 150);
            case "Triple" -> new Room(roomNumber, "Triple", 200);
            default -> throw new IllegalArgumentException("Invalid room type: " + type);
        };
    }

    // Factory method to create multiple rooms
    public static List<Room> createRooms(int numSingles, int numDoubles, int numTriples) {
        List<Room> rooms = new ArrayList<>();
        int roomNumber = 1;

        for (int i = 0; i < numSingles; i++) {
            rooms.add(createRoom(roomNumber++, "Single"));
        }

        for (int i = 0; i < numDoubles; i++) {
            rooms.add(createRoom(roomNumber++, "Double"));
        }

        for (int i = 0; i < numTriples; i++) {
            rooms.add(createRoom(roomNumber++, "Triple"));
        }

        return rooms;
    }
}

