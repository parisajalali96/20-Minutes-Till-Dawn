package models.Enums;

public enum GameTime {
    TWO(120),
    FIVE(300),
    TEN(600),
    TWENTY(1200);
    private final float totalSeconds;
    GameTime(float totalSeconds) {
        this.totalSeconds = totalSeconds;
    }
    public float getTotalSeconds() {
        return totalSeconds;
    }

}
