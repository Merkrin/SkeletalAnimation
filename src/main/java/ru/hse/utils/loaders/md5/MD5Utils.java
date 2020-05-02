package ru.hse.utils.loaders.md5;

import org.joml.Quaternionf;
import org.joml.Vector3f;

class MD5Utils {
    static final String FLOAT_REGEXP = "[+-]?\\d*\\.?\\d*";

    static final String VECTOR3_REGEXP = "\\(\\s*(" + FLOAT_REGEXP +
            ")\\s*(" + FLOAT_REGEXP +
            ")\\s*(" + FLOAT_REGEXP +
            ")\\s*\\)";

    static Quaternionf calculateQuaternion(Vector3f vec) {
        Quaternionf orientation = new Quaternionf(vec.x, vec.y, vec.z, 0);
        return getQuaternionf(orientation);
    }

    private static Quaternionf getQuaternionf(Quaternionf orientation) {
        float temp = 1.0f
                - (orientation.x * orientation.x)
                - (orientation.y * orientation.y)
                - (orientation.z * orientation.z);
        if (temp < 0.0f) {
            orientation.w = 0.0f;
        } else {
            orientation.w = -(float) (Math.sqrt(temp));
        }
        return orientation;
    }

    static Quaternionf calculateQuaternion(float x, float y, float z) {
        Quaternionf orientation = new Quaternionf(x, y, z, 0);
        return getQuaternionf(orientation);
    }
}
