package academia.util;

/**
 * Any entity that can be turned into a CSV line and rebuilt from one.
 */
public interface Persistable {
    String toCsv();
}
