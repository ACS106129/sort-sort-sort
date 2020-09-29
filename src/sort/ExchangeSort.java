package sort;

import sort.base.*;

public class ExchangeSort extends SortSystem {
    public <T extends Number> T[] numberSort(T[] arr, String order) throws RuntimeException {
        if (order == SortOrder.Ascending || order == SortOrder.Descending) {
            store(arr);
            num_ExchangeSort(arr, order);
            return arr;
        }
        return null;
    }

    protected <T extends Number> void num_ExchangeSort(T[] arr, String order) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                boolean isChange = order == SortOrder.Ascending ? numberCompare(arr[i], arr[j]) > 0
                        : numberCompare(arr[i], arr[j]) < 0;
                if (isChange) {
                    swap(i, j, arr);
                    store(arr, i, j);
                }
            }
        }
    }

    @Override
    public String[] stringSort(String[] arr, String order, boolean isCaseSensitive) throws RuntimeException {
        if (order == SortOrder.Ascending || order == SortOrder.Descending) {
            store(arr);
            str_ExchangeSort(arr, order, isCaseSensitive);
            return arr;
        }
        return null;
    }

    protected void str_ExchangeSort(String[] arr, String order, boolean isCaseSensitive) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                boolean isChange = order == SortOrder.Ascending ? stringCompare(arr[i], arr[j], isCaseSensitive) > 0
                        : stringCompare(arr[i], arr[j], isCaseSensitive) < 0;
                if (isChange) {
                    swap(i, j, arr);
                    store(arr, i, j);
                }
            }
        }
    }

    @Override
    public String getSortType() {
        return SortType.ExchangeSort;
    }
}