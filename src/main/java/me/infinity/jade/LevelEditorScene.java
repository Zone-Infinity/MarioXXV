package me.infinity.jade;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {
    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f;

    public LevelEditorScene() {
        System.out.println("Inside LevelEditorScene");
    }

    @Override
    public void update(float dt) {
        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE))
            changingScene = true;

        if (changingScene && timeToChangeScene > 0.0f) {
            timeToChangeScene -= dt;
            Window.get().updateRGB(-dt * 5.0f);
        } else if (changingScene) {
            Window.changeScene(1);
        }
    }
}
