package com.github.terefang.jmelange.commons.version;

public enum VersionType {
    ALPHA("alpha"), BETA("beta"), GAMMA("gamma"), DELTA("delta"), RC("rc"), RELEASE("release"), STABLE("stable");

    private final String name;

    VersionType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }
}