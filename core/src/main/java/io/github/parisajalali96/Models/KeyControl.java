package io.github.parisajalali96.Models;

import com.badlogic.gdx.Input;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyControl {
    public static int up = Input.Keys.UP;
    public static int down = Input.Keys.DOWN;
    public static int left = Input.Keys.LEFT;
    public static int right = Input.Keys.RIGHT;
    public static int shootWeapon = Input.Buttons.LEFT;
    public static int reloadWeapon = Input.Keys.R;
    public static int cheatCodeMenu = Input.Keys.C;
    public static int pauseGame = Input.Keys.P;
    public static int autoAim = Input.Keys.SPACE;

    public static Map<String, Integer> getKeyControl(){
        Map<String, Integer> keyControl = new HashMap<>();
        keyControl.put("Up", up);
        keyControl.put("Down", down);
        keyControl.put("Left", left);
        keyControl.put("Right", right);
        keyControl.put("Shoot Weapon", shootWeapon);
        keyControl.put("Reload Weapon", reloadWeapon);
        keyControl.put("Cheat Code Activator", cheatCodeMenu);
        keyControl.put("Pause Game", pauseGame);
        keyControl.put("Auto Aim", autoAim);
        return keyControl;
    }

    public static void changeKey(String selectedKey, String key) {
        int newKey = parseKeyOrButton(key);
        if (newKey == -1) {
            System.out.println("Failed to parse key/button: " + key);
            return;
        }
        switch (selectedKey) {
            case "Up": up = newKey; break;
            case "Down": down = newKey; break;
            case "Left": left = newKey; break;
            case "Right": right = newKey; break;
            case "Shoot Weapon": shootWeapon = newKey; break;
            case "Reload Weapon": reloadWeapon = newKey; break;
            case "Cheat Code Activator": cheatCodeMenu = newKey; break;
            case "Pause Game": pauseGame = newKey; break;
            case "Auto Aim": autoAim = newKey; break;
        }
    }

    public static int parseKeyOrButton(String key) {
        key = key.toUpperCase().trim();
        switch (key) {
            case "LEFT_BUTTON": return Input.Buttons.LEFT;
            case "RIGHT_BUTTON": return Input.Buttons.RIGHT;
            case "MIDDLE_BUTTON": return Input.Buttons.MIDDLE;
            default:
                try {
                    return Input.Keys.valueOf(key);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid key or button: " + key);
                    return -1;
                }
        }
    }



}
