package academia.util;

import java.util.List;

/**
 * Generic contract for services that can be searched by a keyword.
 */
public interface Searchable<T> {
    List<T> search(String keyword);
}
