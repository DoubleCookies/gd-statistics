package gd.enums;

/**
 * Represents different sorting variations
 */
public enum SortingCode {
    DEFAULT(0),
    DESCENDING_LIKES(1),
    ASCENDING_LIKES(2),
    DESCENDING_DOWNLOADS(3),
    ASCENDING_DOWNLOADS(4),
    LONGEST_DESCRIPTION(5);

    private final int number;

    SortingCode(int number) {
        this.number = number;
    }

    public int getValue() {
        return number;
    }
}