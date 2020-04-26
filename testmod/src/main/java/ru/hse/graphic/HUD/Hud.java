package ru.hse.graphic.HUD;

import org.joml.Vector4f;
import ru.hse.graphic.FontTexture;
import ru.hse.graphic.Model;
import ru.hse.utils.Window;

import java.awt.Font;

public class Hud implements IHud {
    private static final Font FONT = new Font("Arial", Font.PLAIN, 20);

    private static final String CHARSET = "ISO-8859-1";

    private final Model[] models;

    private final TextItem statusTextItem;

    public Hud(String statusText) throws Exception {
        FontTexture fontTexture = new FontTexture(FONT, CHARSET);
        this.statusTextItem = new TextItem(statusText, fontTexture);
        this.statusTextItem.getMesh().getMaterial().setAmbientColour(new Vector4f(1, 1, 1, 1));

        // Create list that holds the items that compose the HUD
        models = new Model[]{statusTextItem};
    }

    public void setStatusText(String statusText) {
        this.statusTextItem.setText(statusText);
    }

    @Override
    public Model[] getModels() {
        return models;
    }

    public void updateSize(Window window) {
        this.statusTextItem.setPosition(10f, window.getHeight() - 50f, 0);
    }
}
