package io.github.parisajalali96.Controllers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import io.github.parisajalali96.Main;
import io.github.parisajalali96.Models.*;
import io.github.parisajalali96.Views.ForgotPassView;
import io.github.parisajalali96.Views.LoginMenu;

import java.io.IOException;

public class ForgotPassController {
    private ForgotPassView view;

    public void setView(ForgotPassView view) {
        this.view = view;
    }

    //forgot password
    public Result forgotPassword(String username, String SA) throws IOException {
        User user = UserStorage.findUserByUsername(username);
        if(user == null) return new Result(false, "User not found!");
        else if(!user.getSA().equals(SA)) return new Result(false, "Wrong answer!");
        else return new Result(true, "You can change your password!");
    }

    //get security question
    public String getQuestion(String username) throws IOException {
        User user = UserStorage.findUserByUsername(username);
        return user.getSQ();
    }

    //change pass controller
    public Result changePassword(String newPass, String username) throws IOException {
        User user = UserStorage.findUserByUsername(username);
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%&*)(_]).{8,}$";
        assert user != null;
        if(user.getPassword().equals(newPass))
            return new Result(true, "New password must be different!");
        else if(!newPass.matches(passwordRegex))
            return new Result(false, "New password is too weak!");
        user.setPassword(newPass);
        return new Result(true, "Password changed successfully!");
    }

    //add listeners
    public void addListeners() {
        view.getQuestion.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Dialog dialog = new Dialog("Security Question", GameAssetManager.getGameAssetManager().getSkin());
                try {
                    String username = view.usernameField.getText();
                    if(username.isEmpty()) view.showResult(new Result(false, "Username field is empty!"));
                    else {
                        Label label = new Label(getQuestion(username), GameAssetManager.getGameAssetManager().getSkin());
                        label.setWrap(true);
                        label.setAlignment(Align.center);
                        dialog.getContentTable().add(label).width(300).pad(20);
                        dialog.button("OK");
                        dialog.show(view.stage);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        view.confirmAnswer.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String answer = view.securityQuestionAnswer.getText();
                String username = view.usernameField.getText();
                if(username.isEmpty()) view.showResult(new Result(false, "Username field is empty!"));
                else {
                    try {
                        Result result = forgotPassword(username, answer);
                        if (!result.isSuccess())view.showResult(result);
                        else {
                            Dialog passwordDialog = new Dialog("Change Password", GameAssetManager.getGameAssetManager().getSkin());
                            Table passwordTable = passwordDialog.getContentTable();

                            Label changePass = new Label("Enter your new password:", GameAssetManager.getGameAssetManager().getSkin());
                            TextField newPasswordField = new TextField("", GameAssetManager.getGameAssetManager().getSkin());
                            newPasswordField.setMessageText("New Password");
                            newPasswordField.setPasswordMode(true);
                            newPasswordField.setPasswordCharacter('*');
                            newPasswordField.setWidth(500);

                            passwordTable.add(changePass).padBottom(10).row();
                            passwordTable.add(newPasswordField).width(250).padBottom(20).row();

                            TextButton confirmButton = new TextButton("Confirm", GameAssetManager.getGameAssetManager().getSkin());
                            TextButton cancelButton = new TextButton("Cancel", GameAssetManager.getGameAssetManager().getSkin());

                            Table buttonTable = passwordDialog.getButtonTable();
                            buttonTable.add(confirmButton).pad(10);
                            buttonTable.add(cancelButton).pad(10);

                            confirmButton.addListener(new ChangeListener() {
                                @Override
                                public void changed(ChangeEvent event, Actor actor) {
                                    String newPassword = newPasswordField.getText();
                                    if (newPassword.isEmpty()) {
                                        view.showResult(new Result(false, "Password cannot be empty."));
                                    } else {
                                        try {
                                            Result changeResult = changePassword(newPassword, username);
                                            view.showResult(changeResult);
                                            passwordDialog.hide();
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                            });

                            cancelButton.addListener(new ChangeListener() {
                                @Override
                                public void changed(ChangeEvent event, Actor actor) {
                                    passwordDialog.hide();
                                }
                            });

                            passwordDialog.show(view.stage);
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        view.backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new LoginMenu(
                    new LoginMenuController(),
                    GameAssetManager.getGameAssetManager().getSkin()
                ));
            }
        });
    }
}
