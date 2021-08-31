package Comparators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class JSONFileComparator implements FileComparator {
    @Override
    public boolean compare(List<String> content1, List<String> content2) {
        String json1 = combineLines(content1);
        String json2 = combineLines(content2);

        ObjectMapper mapper = new ObjectMapper();

        try {
            Map map1 = mapper.readValue(json1, Map.class);
            Map map2 = mapper.readValue(json2 , Map.class);

            return compare(map1, map2);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean compare(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }

        if (object1.getClass() != object2.getClass()) {
            return false;
        }

        if (object1 instanceof List) {
            List arrayList1 = (List) object1;
            List arrayList2 = (List) object2;

            if (arrayList1.size() != arrayList2.size()) {
                return false;
            }

            for (Object obj1 : arrayList1) {
                boolean found = false;
                for (Object obj2 : arrayList2) {
                    if (compare(obj1, obj2)) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    return false;
                }
            }

            return true;
        }

        if (object1 instanceof Map) {
            Map map1 = (Map) object1;
            Map map2 = (Map) object2;

            return compare(map1, map2);
        }

        return object1.equals(object2);
    }

    private boolean compare(Map<String, Object> map1, Map<String, Object> map2) {
        for (var entry : map1.entrySet()) {
            if (!map2.containsKey(entry.getKey())) {
                return false;
            }
            Object obj = map2.get(entry.getKey());

            if (!compare(entry.getValue(), obj)) {
                return false;
            }
        }
        return true;
    }

    private static String combineLines(List<String> lines) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String line : lines) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }
}