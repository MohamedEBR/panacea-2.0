package com.example.panacea.enums;

public enum Belt {
    WHITE(0),
    YELLOW(1),
    ORANGE(2),
    GREEN(3),
    BLUE(4),
    PURPLE_II(5),
    PURPLE_I(6),
    BROWN_III(7),
    BROWN_II(8),
    BROWN_I(9),
    BLACK(10);

    private final int rank;

    Belt(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }
}


