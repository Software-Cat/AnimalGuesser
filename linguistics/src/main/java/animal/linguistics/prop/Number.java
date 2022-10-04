package animal.linguistics.prop;

public enum Number {
    SINGULAR(1),
    PLURAL(2);

    private final int intValue;

    Number(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }
}
