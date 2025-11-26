package main.java.code;

public class RoomFactory {

    
    // To keep track of the last assigned room number
    private static int lastRoomNumber = 0;

    public static void createRoom(String type) {
        // Generate Room Number
        int roomNumber = ++lastRoomNumber;

        // Create Room Based on Type
        Room room = switch (type) {
            case "Single" -> new SingleRoom(roomNumber);
            case "Double" -> new DoubleRoom(roomNumber);
            case "Triple" -> new TripleRoom(roomNumber);
            default -> throw new IllegalArgumentException("Invalid room type: " + type);
        };

        // Add to DB
        HotelDB.getInstance().addRoom(room);
    }

    public static void createRooms(int numSingles, int numDoubles, int numTriples) {

        for (int i = 0; i < numSingles; i++)
            createRoom("Single");

        for (int i = 0; i < numDoubles; i++)
            createRoom("Double");

        for (int i = 0; i < numTriples; i++)
            createRoom("Triple");
    }
}
