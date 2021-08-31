package Comparators;

import java.util.List;

public interface FileComparator {
    boolean compare(List<String> content1, List<String> content2);
}
