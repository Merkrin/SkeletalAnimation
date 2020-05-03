package ru.hse.utils.loaders.md5;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joml.Vector3f;

public class MD5BoundInfo {
    private List<MD5Bound> bounds;

    private void setBounds(List<MD5Bound> bounds) {
        this.bounds = bounds;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("bounds [" + System.lineSeparator());
        for (MD5Bound bound : bounds) {
            str.append(bound).append(System.lineSeparator());
        }
        str.append("]").append(System.lineSeparator());
        return str.toString();
    }

    public static MD5BoundInfo parse(List<String> blockBody) {
        MD5BoundInfo result = new MD5BoundInfo();
        List<MD5Bound> bounds = new ArrayList<>();
        for (String line : blockBody) {
            MD5Bound bound = MD5Bound.parseLine(line);
            if (bound != null) {
                bounds.add(bound);
            }
        }
        result.setBounds(bounds);
        return result;
    }

    private static class MD5Bound {

        private static final Pattern PATTERN_BOUND =
                Pattern.compile("\\s*" + MD5Utils.VECTOR3_REGEXP +
                        "\\s*" + MD5Utils.VECTOR3_REGEXP + ".*");

        private Vector3f minBound;
        private Vector3f maxBound;

        void setMinBound(Vector3f minBound) {
            this.minBound = minBound;
        }

        void setMaxBound(Vector3f maxBound) {
            this.maxBound = maxBound;
        }

        @Override
        public String toString() {
            return "[minBound: " + minBound + ", maxBound: " + maxBound + "]";
        }

        static MD5Bound parseLine(String line) {
            MD5Bound result = null;
            Matcher matcher = PATTERN_BOUND.matcher(line);
            if (matcher.matches()) {
                result = new MD5Bound();
                float x = Float.parseFloat(matcher.group(1));
                float y = Float.parseFloat(matcher.group(2));
                float z = Float.parseFloat(matcher.group(3));
                result.setMinBound(new Vector3f(x, y, z));

                x = Float.parseFloat(matcher.group(4));
                y = Float.parseFloat(matcher.group(5));
                z = Float.parseFloat(matcher.group(6));
                result.setMaxBound(new Vector3f(x, y, z));
            }
            return result;
        }

    }
}
