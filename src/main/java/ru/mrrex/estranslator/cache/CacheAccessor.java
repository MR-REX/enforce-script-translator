package ru.mrrex.estranslator.cache;

public class CacheAccessor<T> {

    private final String majorKey;

    public CacheAccessor(String majorKey) {
        if (majorKey == null || majorKey.isEmpty())
            throw new IllegalArgumentException("Major key cannot be null or an empty string");

        this.majorKey = majorKey;
    }

    private String getKey(String minorKey) {
        return majorKey + '.' + minorKey;
    }

    public T get(String minorKey) {
        return (T) Cache.INSTANCE.get(getKey(minorKey));
    }

    public boolean hasKey(String minorKey) {
        return Cache.INSTANCE.hasKey(getKey(minorKey));
    }

    public void set(String minorKey, T object) {
        Cache.INSTANCE.set(getKey(minorKey), object);
    }
}
