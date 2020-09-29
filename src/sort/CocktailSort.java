package sort;

import sort.base.*;

public class CocktailSort extends SortSystem {
    public <T extends Number> T[] numberSort(T[] arr, String order) throws RuntimeException {
        if (order == SortOrder.Ascending || order == SortOrder.Descending) {
            store(arr);
            num_CocktailSort(arr, order);
            return arr;
        }
        return null;
    }

    protected <T extends Number> void num_CocktailSort(T[] arr, String order) {
        int i, left = 0, right = arr.length - 1;
        while (left < right) {
            for (i = left; i < right; i++) {
                boolean isChange = order == SortOrder.Ascending ? numberCompare(arr[i], arr[i + 1]) > 0
                        : numberCompare(arr[i], arr[i + 1]) < 0;
                if (isChange) {
                    swap(i, i + 1, arr);
                    store(arr, i, i + 1);
                }
            }
            right--;
            for (i = right; i > left; i--) {
                boolean isChange = order == SortOrder.Ascending ? numberCompare(arr[i - 1], arr[i]) > 0
                        : numberCompare(arr[i - 1], arr[i]) < 0;
                if (isChange) {
                    swap(i, i - 1, arr);
                    store(arr, i, i - 1);
                }
            }
            left++;
        }
    }

    @Override
    public String[] stringSort(String[] arr, String order, boolean isCaseSensitive) throws RuntimeException {
        if (order == SortOrder.Ascending || order == SortOrder.Descending) {
            store(arr);
            str_CocktailSort(arr, order, isCaseSensitive);
            return arr;
        }
        return null;
    }

    protected void str_CocktailSort(String[] arr, String order, boolean isCaseSensitive) {
        int i, left = 0, right = arr.length - 1;
        while (left < right) {
            for (i = left; i < right; i++) {
                boolean isChange = order == SortOrder.Ascending ? stringCompare(arr[i], arr[i + 1], isCaseSensitive) > 0
                        : stringCompare(arr[i], arr[i + 1], isCaseSensitive) < 0;
                if (isChange) {
                    swap(i, i + 1, arr);
                    store(arr, i, i + 1);
                }
            }
            right--;
            for (i = right; i > left; i--) {
                boolean isChange = order == SortOrder.Ascending ? stringCompare(arr[i - 1], arr[i], isCaseSensitive) > 0
                        : stringCompare(arr[i - 1], arr[i], isCaseSensitive) < 0;
                if (isChange) {
                    swap(i, i - 1, arr);
                    store(arr, i, i - 1);
                }
            }
            left++;
        }
    }

    @Override
    public String getSortType() {
        return SortType.CocktailSort;
    }
}