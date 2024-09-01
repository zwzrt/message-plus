package cn.redcoral.messageplus.utils.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.google.errorprone.annotations.CompatibleWith;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * MP本地缓存
 * @author mo
 **/
public class MPCache<K, V> {
    private final Cache<K, V> cache;

    public MPCache(Cache<K, V> cache) {
        this.cache = cache;
    }


    /**
     * Returns the value associated with the {@code key} in this cache, or {@code null} if there is no
     * cached value for the {@code key}.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or {@code null} if this cache contains
     *         no mapping for the key
     * @throws NullPointerException if the specified key is null
     */
    @Nullable
    public V getIfPresent(@NonNull @CompatibleWith("K") K key) {
        return cache.getIfPresent(key);
    }

    /**
     * Returns the value associated with the {@code key} in this cache, obtaining that value from the
     * {@code mappingFunction} if necessary. This method provides a simple substitute for the
     * conventional "if cached, return; otherwise create, cache and return" pattern.
     * <p>
     * If the specified key is not already associated with a value, attempts to compute its value
     * using the given mapping function and enters it into this cache unless {@code null}. The entire
     * method invocation is performed atomically, so the function is applied at most once per key.
     * Some attempted update operations on this cache by other threads may be blocked while the
     * computation is in progress, so the computation should be short and simple, and must not attempt
     * to update any other mappings of this cache.
     * <p>
     * <b>Warning:</b> as with {@link CacheLoader#load}, {@code mappingFunction} <b>must not</b>
     * attempt to update any other mappings of this cache.
     *
     * @param key the key with which the specified value is to be associated
     * @param mappingFunction the function to compute a value
     * @return the current (existing or computed) value associated with the specified key, or null if
     *         the computed value is null
     * @throws NullPointerException if the specified key or mappingFunction is null
     * @throws IllegalStateException if the computation detectably attempts a recursive update to this
     *         cache that would otherwise never complete
     * @throws RuntimeException or Error if the mappingFunction does so, in which case the mapping is
     *         left unestablished
     */
    @Nullable
    public V get(@NonNull K key, @NonNull Function<? super K, ? extends V> mappingFunction) {
        return cache.get(key, mappingFunction);
    }

    /**
     * Associates the {@code value} with the {@code key} in this cache. If the cache previously
     * contained a value associated with the {@code key}, the old value is replaced by the new
     * {@code value}.
     * <p>
     * Prefer {@link #get(Object, Function)} when using the conventional "if cached, return; otherwise
     * create, cache and return" pattern.
     *
     * @param key the key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @throws NullPointerException if the specified key or value is null
     */
    public void put(@NonNull K key, @NonNull V value) {
        cache.put(key, value);
    }

    /**
     * Discards any cached value for the {@code key}. The behavior of this operation is undefined for
     * an entry that is being loaded (or reloaded) and is otherwise not present.
     *
     * @param key the key whose mapping is to be removed from the cache
     * @throws NullPointerException if the specified key is null
     */
    public void invalidate(@NonNull @CompatibleWith("K") Object key) {
        cache.invalidate(key);
    }

    /**
     * Returns a view of the entries stored in this cache as a thread-safe map. Modifications made to
     * the map directly affect the cache.
     * <p>
     * A computation operation, such as {@link ConcurrentMap#compute}, performs the entire method
     * invocation atomically, so the function is applied at most once per key. Some attempted update
     * operations by other threads may be blocked while computation is in progress. The computation
     * must not attempt to update any other mappings of this cache.
     * <p>
     * Iterators from the returned map are at least <i>weakly consistent</i>: they are safe for
     * concurrent use, but if the cache is modified (including by eviction) after the iterator is
     * created, it is undefined which of the changes (if any) will be reflected in that iterator.
     *
     * @return a thread-safe view of this cache supporting all of the optional {@link Map} operations
     */
    @NonNull
    public ConcurrentMap<@NonNull K, @NonNull V> asMap() {
        return cache.asMap();
    }

}
