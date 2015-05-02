package io.itch.frogcheese.sharecart;

public class Sharecart {
    private short x;
    private short y;
    private int[] misc = new int[Constraints.MISC_ITEMS_LENGTH];
    private String name;
    private boolean[] switches = new boolean[Constraints.SWITCH_ITEMS_LENGTH];

    public static Sharecart withDefaults() {
        Sharecart sharecart = new Sharecart();
        sharecart.x((short) 0);
        sharecart.y((short) 0);
        for (int i = 0; i < Constraints.MISC_ITEMS_LENGTH; i++) {
            sharecart.misc(i, 0);
        }
        sharecart.name("CHANGEME");
        for (int i = 0; i < Constraints.SWITCH_ITEMS_LENGTH; i++) {
            sharecart.switchValue(i, false);
        }
        return sharecart;
    }

    public short x() {
        return this.x;
    }

    public void x(short x) {
        this.x = x;
    }

    public short y() {
        return this.y;
    }

    public void y(short y) {
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
}
