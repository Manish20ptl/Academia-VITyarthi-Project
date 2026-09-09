package academia.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple in-memory incrementing ID generator, seeded from existing data
 * on load so IDs never collide after a restart.
 */
public class IdGenerator {
    private final AtomicInteger counter;
    private final String prefix;

    public IdGenerator(String prefix, int startAfter) {
        this.prefix = prefix;
        this.counter = new AtomicInteger(startAfter);
    }

    public String next() {
        return prefix + String.format("%04d", counter.incrementAndGet());
    }

    public void ensureAtLeast(int value) {
        counter.updateAndGet(current -> Math.max(current, value));
    }
}
