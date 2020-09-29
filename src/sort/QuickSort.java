package sort;

import sort.base.*;

public class QuickSort extends SortSystem {
    public <T extends Number> T[] numberSort(T[] arr, String order) throws RuntimeException {
        if (order == SortOrder.Ascending || order == SortOrder.Descending) {
            store(arr);
            int low = 0;
            int high = arr.length - 1;
            num_doQuickSort(low, high, arr, order);
            return arr;
        }
        return null;
    }

    protected <T extends Number> void num_doQuickSort(int low, int high, T[] arr, String order) {
        int pivotpoint = 0;
        if (high > low) {
            pivotpoint = num_partition(low, high, pivotpoint, arr, order);
            num_doQuickSort(low, pivotpoint - 1, arr, order);
            num_doQuickSort(pivotpoint + 1, high, arr, order);
        }
    }

    protected <T extends Number> int num_partition(int low, int high, int pivotpoint, T[] arr, String order) {
        int i, j;
        T pivotitem;

        pivotitem = arr[low];
        j = low;
        for (i = low + 1; i <= high; i++) {
            boolean isChange = order == SortOrder.Ascending ? numberCompare(arr[i], pivotitem) < 0
                    : numberCompare(arr[i], pivotitem) > 0;
            if (isChange) {
                j++;
                swap(i, j, arr);
                if (i > j)
                    store(arr, i, j);
            }
        }
        pivotpoint = j;
        swap(low, pivotpoint, arr);
        store(arr, low, pivotpoint);
        return pivotpoint;
    }

    @Override
    public String[] stringSort(String[] arr, String order, boolean isCaseSensitive) throws RuntimeException {
        if (order == SortOrder.Ascending || order == SortOrder.Descending) {
            store(arr);
            int low = 0;
            int high = arr.length - 1;
            str_QuickSort(low, high, arr, order, isCaseSensitive);
            return arr;
        }
        return null;
    }

    protected void str_QuickSort(int low, int high, String[] arr, String order, boolean isCaseSensitive) {
        int pivotpoint = 0;
        if (high > low) {
            pivotpoint = str_partition(low, high, pivotpoint, arr, order, isCaseSensitive);
            str_QuickSort(low, pivotpoint - 1, arr, order, isCaseSensitive);
            str_QuickSort(pivotpoint + 1, high, arr, order, isCaseSensitive);
        }
    }

    protected int str_partition(int low, int high, int pivotpoint, String[] arr, String order,
            boolean isCaseSensitive) {
        int i, j;
        String pivotitem;

        pivotitem = arr[low];
        j = low;
        for (i = low + 1; i <= high; i++) {
            boolean isChange = order == SortOrder.Ascending ? stringCompare(arr[i], pivotitem, isCaseSensitive) < 0
                    : stringCompare(arr[i], pivotitem, isCaseSensitive) > 0;
            if (isChange) {
                j++;
                swap(i, j, arr);
                if (i > j)
                    store(arr, i, j);
            }
        }
        pivotpoint = j;
        swap(low, pivotpoint, arr);
        store(arr, low, pivotpoint);
        return pivotpoint;
    }

    @Override
    public String getSortType() {
        return SortType.QuickSort;
    }
}