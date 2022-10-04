package animal.linguistics.prop;

public enum Person {
    FIRST(1),
    SECOND(2),
    THIRD(3);

    private final int intValue;

    Person(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }
}
