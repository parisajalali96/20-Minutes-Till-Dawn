package io.github.parisajalali96.Models.Enums;

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

    public static GameTime getGameTime(int time) {
        if(time == 2) return TWO;
        if(time == 5) return FIVE;
        if(time == 10) return TEN;
        if(time == 20) return TWENTY;
        return null;
    }

}
