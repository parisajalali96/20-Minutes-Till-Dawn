package io.github.parisajalali96.Controllers;

import io.github.parisajalali96.Models.Enums.AbilityType;
import io.github.parisajalali96.Models.Game;
import io.github.parisajalali96.Views.GameView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameController {
    private GameView view;

    public void setView(GameView view) {
        this.view = view;
    }

    public static List<AbilityType> get3RandomAbilities() {

        List<AbilityType> available = new ArrayList<>(Arrays.asList(AbilityType.values()));

        Collections.shuffle(available);
        return available.subList(0, Math.min(3, available.size()));
    }

}
