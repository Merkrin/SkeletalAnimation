package ru.hse.graphic.animation;

import ru.hse.graphic.*;

import java.util.List;

import org.joml.Matrix4f;

public class AnimatedModel extends Model {
    private int currentFrame;

    private final List<AnimatedFrame> frames;

    public AnimatedModel(Mesh[] meshes,
                         List<AnimatedFrame> frames) {
        super(meshes);
        this.frames = frames;
        currentFrame = 0;
    }

    public AnimatedFrame getCurrentFrame() {
        return this.frames.get(currentFrame);
    }

    public void nextFrame() {
        int nextFrame = currentFrame + 1;
        if (nextFrame > frames.size() - 1) {
            currentFrame = 0;
        } else {
            currentFrame = nextFrame;
        }
    }
}
