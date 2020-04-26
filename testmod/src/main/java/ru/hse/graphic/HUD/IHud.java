package ru.hse.graphic.HUD;

import ru.hse.graphic.Model;

public interface IHud {
    Model[] getModels();

    default void cleanup() {
        Model[] models = getModels();
        for (Model model : models) {
            model.getMesh().cleanUp();
        }
    }
}
