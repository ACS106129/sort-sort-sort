package sort;

import sort.base.*;

public class InsertionSort extends SortSystem {
    public <T extends Number> T[] numberSort(T[] arr, String order) throws RuntimeException {
        if (order == SortOrder.Ascending || order == SortOrder.Descending) {
            store(arr);
            for (int i = 1; i < arr.length; i++) {
                T key = arr[i];
                int j = i - 1;
                while (j >= 0 && (order == SortOrder.Ascending ? numberCompare(arr[j], key) > 0
                        : numberCompare(arr[j], key) < 0)) {
                    arr[j + 1] = arr[j];
                    j--;
                    arr[j + 1] = key;
                    store(arr, j + 1, j + 2);
                }
            }
            return arr;
        }
        return null;
    }

    @Override
    public String[] stringSort(String[] arr, String order, boolean isCaseSensitive) throws RuntimeException {
        if (order == SortOrder.Ascending || order == SortOrder.Descending) {
            store(arr);
            for (int i = 1; i < arr.length; i++) {
                String key = arr[i];
                int j = i - 1;
                while (j >= 0 && (order == SortOrder.Ascending ? stringCompare(arr[j], key, isCaseSensitive) > 0
                        : stringCompare(arr[j], key, isCaseSensitive) < 0)) {
                    arr[j + 1] = arr[j];
                    j--;
                    arr[j + 1] = key;
                    store(arr, j + 1, j + 2);
                }
            }
            return arr;
        }
        return null;
    }

    @Override
    public String getSortType() {
        return SortType.Insertion;
    }
}