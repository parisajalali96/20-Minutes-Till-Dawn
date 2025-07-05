package io.github.parisajalali96.Views;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import io.github.parisajalali96.Models.GameAssetManager;
import io.github.parisajalali96.Models.Result;

public class Menu {
    private Stage stage;
    public void showResult(Result result) {
        Dialog dialog = new Dialog(result.isSuccess() ? "Success" : "Error",
            GameAssetManager.getGameAssetManager().getSkin());
        Label label = new Label(result.getMessage(), GameAssetManager.getGameAssetManager().getSkin());
        label.setWrap(true);
        label.setAlignment(Align.center);
        dialog.getContentTable().add(label).width(300).pad(20);
        dialog.button("OK");
        dialog.show(stage);
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
