package Comparators;

import java.util.HashSet;
import java.util.List;

public class CSVFileComparator implements FileComparator {

    private static final String csvDelimiters = "[;,]";

    @Override
    public boolean compare(List<String> content1, List<String> content2) {
        if (content1.size() != content2.size()) {
            return false;
        }

        CSVTable csvTable1 = new CSVTable(content1.get(0), content1.size());
        CSVTable csvTable2 = new CSVTable(content2.get(0), content2.size());

        for (int i = 1; i < content1.size(); i++) {
            csvTable1.addRow(content1.get(i));
        }

        for (int i = 1; i < content2.size(); i++) {
            csvTable2.addRow(content2.get(i));
        }

        return csvTable1.equals(csvTable2);
    }

    private static class CSVTable {

        private final String[][] elements;
        private int currentRow;

        public CSVTable(String headerLine, int totalLines) {
            String[] header = headerLine.split(csvDelimiters);
            elements = new String[totalLines][header.length];

            System.arraycopy(header, 0, elements[0], 0, header.length);
            currentRow++;
        }

        public void addRow(String rowLine) {
            String[] row = rowLine.split(csvDelimiters);
            System.arraycopy(row, 0, elements[currentRow], 0, row.length);

            currentRow++;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof CSVTable)) {
                return false;
            }
            CSVTable other = (CSVTable) obj;

            if (elements.length != other.elements.length) {
                return false;
            }

            if (elements[0].length != other.elements[0].length) {
                return false;
            }

            // place columns are in the right order
            for (int i = 0; i < elements[0].length; i++) {
                if (!elements[0][i].equals(other.elements[0][i])) {
                    int foundIndex = -1;
                    for (int j = i + 1; j < elements[0].length; j++) {
                        if (elements[0][i].equals(other.elements[0][j])) {
                            foundIndex = j;
                            break;
                        }
                    }
                    if (foundIndex == -1) {
                        return false;
                    }

                    for (int j = 0; j < elements.length; j++) {
                        String temp = other.elements[j][i];
                        other.elements[j][i] = other.elements[j][foundIndex];
                        other.elements[j][foundIndex] = temp;
                    }
                }
            }

            HashSet<Integer> searchedIndices = new HashSet<>();
            // check if table has the same rows
            for (String[] element : elements) {

                boolean sourceFound = false;
                for (int destinationIndex = 0; destinationIndex < other.elements.length; destinationIndex++) {

                    if (searchedIndices.contains(destinationIndex)) {
                        continue;
                    }

                    boolean rowFound = true;

                    for (int rowIndex = 0; rowIndex < other.elements[0].length; rowIndex++) {
                        if (!element[rowIndex].equals(other.elements[destinationIndex][rowIndex])) {
                            rowFound = false;
                            break;
                        }
                    }

                    if (rowFound) {
                        searchedIndices.add(destinationIndex);
                        sourceFound = true;
                        break;
                    }
                }

                if (!sourceFound) {
                    return false;
                }
            }

            return true;
        }
    }
}
