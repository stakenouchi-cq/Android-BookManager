package com.caraquri.android_bookmanager;

import java.lang.reflect.Type;

public enum BundleKey {
    BUNDLE_KEY_IMAGESTRING("imgageString"),
    BUNDLE_KEY_TITLE("title"),
    BUNDLE_KEY_PRICE("price"),
    BUNDLE_KEY_PURCHASEDATE("purchaseDate"),
    ;

    private final String text;

    private BundleKey(final String text) {
        this.text = text;
    }

    public String getString() {
        return this.text;
    }

}