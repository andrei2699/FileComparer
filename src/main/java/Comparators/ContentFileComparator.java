package Comparators;

import java.util.List;

public class ContentFileComparator implements FileComparator {
    @Override
    public boolean compare(List<String> content1, List<String> content2) {
        if (content1.size() != content2.size()) {
            return false;
        }

        for (int i = 0; i < content1.size(); i++) {
            if (!content1.get(i).equals(content2.get(i))) {
                return false;
            }
        }
        return true;
    }
}
