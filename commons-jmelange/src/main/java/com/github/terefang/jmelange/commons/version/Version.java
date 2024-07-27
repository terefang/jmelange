package com.github.terefang.jmelange.commons.version;

import java.util.Comparator;
import java.util.Objects;

public final class Version implements Comparator<Version>
{

    private final int major;
    private final int minor;
    private final int build;
    private final VersionType type;
    private final int release;

    private static final long serialVersionUID = 0L;

    public Version(int major, int minor, int build, VersionType type, int release) {
        this.major = major;
        this.minor = minor;
        this.build = build;
        this.type = type;
        this.release = release;
    }

    public int major() {
        return this.major;
    }

    public int minor() {
        return this.minor;
    }

    public int build() {
        return this.build;
    }

    public VersionType type() {
        return this.type;
    }

    public int release() {
        return this.release;
    }

    @Override
    public String toString() {
        return this.major + "." + this.minor + "." + this.build + "." + this.type.getName() + "-" + this.release;
    }

    public long toLong() {
        return ((long) this.major << 40L)
                | ((long) this.minor << 32L)
                | ((long) this.build << 16L)
                | (this.type.ordinal() << 12L)
                | ((long)this.release);
    }

    @Override
    public int compare(Version o1, Version o2)
    {
        long _1 = o1.toLong();
        long _2 = o2.toLong();
        return Long.compare(_1, _2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return this.major == version.major &&
                this.minor == version.minor &&
                this.release == version.release &&
                this.type == version.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.major, this.minor, this.type, this.release);
    }

}