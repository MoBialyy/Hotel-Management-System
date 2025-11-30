package main.java.code;

import java.time.LocalDate;

public class Client {
        public static void main(String[] args) {

                // Management controls everything
                HotelManagement hm = new HotelManagement();

                // --------------------------------------
                // 1) Create rooms through RoomFactory
                // --------------------------------------
                RoomFactory.createRooms(2, 1, 1);

                // --------------------------------------
                // 2) Add residents ONLY through management
                // --------------------------------------
                Resident r2 = hm.addResident(
                        "Sarah", "Kamal", 31, "Egyptian",
                        "sarah@gmail.com", "0100001111", "Giza", "EG54321");

                // --------------------------------------
                // 3) Book rooms ONLY through management
                // --------------------------------------
                //hm.bookRoomByType(r1, "Single", LocalDate.of(2025, 12, 1), 3, BoardingOption.BED_AND_BREAKFAST);
                hm.bookRoomByType(r2, "Single", LocalDate.of(2025, 12, 5), 2, BoardingOption.FULL_BOARD);
                // --------------------------------------
                // 4) Checking availability
                // --------------------------------------
                System.out.println("\nAvailable rooms 2 - 4 Dec:");
                hm.printAvailableRooms(LocalDate.of(2025, 12, 2),
                                LocalDate.of(2025, 12, 4));

                // --------------------------------------
                // 5) Show resident details
                // --------------------------------------
                r2.displayInfo();
                System.out.println("\n");
                r2.printBookings();
        }
}
