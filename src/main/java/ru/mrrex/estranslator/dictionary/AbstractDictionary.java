package ru.mrrex.estranslator.dictionary;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class AbstractDictionary<K, V> {

    protected final Map<K, V> dictionary;

    protected AbstractDictionary(Map<K, V> map) {
        this.dictionary = map;
    }

    protected AbstractDictionary() {
        this(new LinkedHashMap<>());
    }

    public void forEach(BiConsumer<K, V> consumer) {
        dictionary.forEach(consumer::accept);
    }

    public int size() {
        return dictionary.size();
    }

    public boolean isEmpty() {
        return dictionary.isEmpty();
    }

    @Override
    public int hashCode() {
        return 31 + ((dictionary == null) ? 0 : dictionary.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        AbstractDictionary<K, V> other = (AbstractDictionary<K, V>) obj;

        if (dictionary == null) {
            if (other.dictionary != null)
                return false;
        } else if (!dictionary.equals(other.dictionary))
            return false;
            
        return true;
    }

    @Override
    public String toString() {
        return "Dictionary [size=" + dictionary.size() + "]";
    }
}
