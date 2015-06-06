package io.itch.frogcheese.sharecart;

import java.util.Arrays;

/**
 * Wrapper around the sharecart parameters.
 */
class ShareCart {
    static final String DEFAULT_NAME = "CHANGEME";

    private int x;
    private int y;
    private int[] misc = new int[Constraints.MISC_ITEMS_LENGTH];
    private String name = DEFAULT_NAME;
    private boolean[] switches = new boolean[Constraints.SWITCH_ITEMS_LENGTH];

    /**
     * @return A new ShareCart instance with all parameters set to their defaults.
     */
    public static ShareCart withDefaults() {
        ShareCart shareCart = new ShareCart();
        shareCart.x(0);
        shareCart.y(0);
        for (int i = 0; i < Constraints.MISC_ITEMS_LENGTH; i++) {
            shareCart.misc(i, 0);
        }
        shareCart.name(DEFAULT_NAME);
        for (int i = 0; i < Constraints.SWITCH_ITEMS_LENGTH; i++) {
            shareCart.switchValue(i, false);
        }
        return shareCart;
    }

    public int x() {
        return x;
    }

    public void x(int x) {
        this.x = x;
    }

    public int y() {
        return y;
    }

    public void y(int y) {
        this.y = y;
    }

    /**
     * @param index the index of the value. Must be less than {@link Constraints#MISC_ITEMS_LENGTH}.
     *
     * @return The misc value at the given index.
     */
    public int misc(int index) {
        return misc[index];
    }

    /**
     * Sets the misc value at the given index.
     *
     * @param index the index of the value. Must be less than {@link Constraints#MISC_ITEMS_LENGTH}.
     * @param value the value to use.
     */
    public void misc(int index, int value) {
        misc[index] = value;
    }

    /**
     * @return The amount of available misc parameters.
     */
    public int miscLength() {
        return misc.length;
    }

    public String name() {
        return name;
    }

    public void name(String name) {
        this.name = name;
    }

    /**
     * @param index the index of the value. Must be less than {@link Constraints#SWITCH_ITEMS_LENGTH}.
     *
     * @return The switch value at the given index.
     */
    public boolean switchValue(int index) {
        return switches[index];
    }

    /**
     * Sets the switch value at the given index.
     *
     * @param index the index of the value. Must be less than {@link Constraints#SWITCH_ITEMS_LENGTH}.
     * @param value the value to use.
     */
    public void switchValue(int index, boolean value) {
        switches[index] = value;
    }

    /**
     * @return The amount of available switch parameters.
     */
    public int switchLength() {
        return switches.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShareCart shareCart = (ShareCart) o;

        return x == shareCart.x
                && y == shareCart.y
                && Arrays.equals(misc, shareCart.misc)
                && name.equals(shareCart.name)
                && Arrays.equals(switches, shareCart.switches);
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
