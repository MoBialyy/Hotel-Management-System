package main.java.code;

public enum BoardingOption {
    ROOM_ONLY(0),
    BED_AND_BREAKFAST(20),   // per night
    HALF_BOARD(40),          // per night
    FULL_BOARD(60);          // per night

    private final double pricePerNight;

    BoardingOption(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }
}
