package io.itch.frogcheese.sharecart;

import java.util.Arrays;

/**
 * Wrapper around the sharecart parameters.
 */
class Sharecart {
    static final String DEFAULT_NAME = "CHANGEME";

    private int x;
    private int y;
    private int[] misc = new int[Constraints.MISC_ITEMS_LENGTH];
    private String name = DEFAULT_NAME;
    private boolean[] switches = new boolean[Constraints.SWITCH_ITEMS_LENGTH];


    public static Sharecart withDefaults() {
        Sharecart sharecart = new Sharecart();
        sharecart.x(0);
        sharecart.y(0);
        for (int i = 0; i < Constraints.MISC_ITEMS_LENGTH; i++) {
            sharecart.misc(i, 0);
        }
        sharecart.name(DEFAULT_NAME);
        for (int i = 0; i < Constraints.SWITCH_ITEMS_LENGTH; i++) {
            sharecart.switchValue(i, false);
        }
        return sharecart;
    }

    public int x() {
        return this.x;
    }

    public void x(int x) {
        this.x = x;
    }

    public int y() {
        return this.y;
    }

    public void y(int y) {
        this.y = y;
    }

    public int misc(int index) {
        return this.misc[index];
    }

    public void misc(int index, int value) {
        this.misc[index] = value;
    }

    public int miscLength() {
        return this.misc.length;
    }

    public String name() {
        return this.name;
    }

    public void name(String name) {
        this.name = name;
    }

    public boolean switchValue(int index) {
        return this.switches[index];
    }

    public void switchValue(int index, boolean value) {
        this.switches[index] = value;
    }

    public int switchLength() {
        return this.switches.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sharecart sharecart = (Sharecart) o;

        return x == sharecart.x
                && y == sharecart.y
                && Arrays.equals(misc, sharecart.misc)
                && name.equals(sharecart.name)
                && Arrays.equals(switches, sharecart.switches);
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + Arrays.hashCode(misc);
        result = 31 * result + name.hashCode();
        result = 31 * result + Arrays.hashCode(switches);
        return result;
    }
}
