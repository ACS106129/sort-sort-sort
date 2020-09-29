package sort;

import java.util.ArrayList;
import java.util.List;

import sort.base.SortOrder;
import sort.base.SortSystem;
import sort.base.SortType;

public class MergeSort extends SortSystem {
    public <T extends Number> T[] numberSort(T[] arr, String order) throws RuntimeException {
        if (order == SortOrder.Ascending || order == SortOrder.Descending) {
            store(arr);
            num_doMergeSort(arr, order);
            return arr;
        }
        return null;
    }

    protected <T extends Number> void num_doMergeSort(T[] arr, String order) {
        List<T> temparr = new ArrayList<>();
        for (int i = 0; i < arr.length; i++)
            temparr.add(null);
        for (int count = 1; count < arr.length; count *= 2)
            for (int leftStart = 0; leftStart < arr.length; leftStart += 2 * count) {
                if (count > arr.length - leftStart)
                    break;
                num_Merge(arr, temparr, leftStart, count, leftStart + count,
                        Math.min(count, arr.length - leftStart - count), order);
            }
    }

    protected <T extends Number> void num_Merge(T[] arr, List<T> temparr, int leftStart, int leftCount, int rightStart,
            int rightCount, String order) {
        int i = leftStart, j = rightStart, leftBound = leftStart + leftCount, rightBound = rightStart + rightCount,
                index = leftStart;
        while (i < leftBound || j < rightBound) {
            if (i < leftBound && j < rightBound) {
                boolean isChange = order == SortOrder.Ascending ? numberCompare(arr[i], arr[j]) > 0
                        : numberCompare(arr[i], arr[j]) < 0;
                if (isChange) {
                    temparr.add(index, arr[j]);
                    j++;
                } else {
                    temparr.add(index, arr[i]);
                    i++;
                }
            } else if (i < leftBound) {
                temparr.add(index, arr[i]);
                i++;
            } else {
                temparr.add(index, arr[j]);
                j++;
            }
            index++;
        }
        for (i = leftStart; i < index; ++i)
            arr[i] = temparr.get(i);
        store(arr, i - 1, i - 2);
    }

    @Override
    public String[] stringSort(String[] arr, String order, boolean isCaseSensitive) throws RuntimeException {
        if (order == SortOrder.Ascending || order == SortOrder.Descending) {
            store(arr);
            str_doMergeSort(arr, order, isCaseSensitive);
            return arr;
        }
        return null;
    }

    protected void str_doMergeSort(String[] arr, String order, boolean isCaseSensitive) {
        List<String> temparr = new ArrayList<>();
        for (int i = 0; i < arr.length; i++)
            temparr.add(null);
        for (int count = 1; count < arr.length; count *= 2)
            for (int leftStart = 0; leftStart < arr.length; leftStart += 2 * count) {
                if (count > arr.length - leftStart)
                    break;
                str_Merge(arr, temparr, leftStart, count, leftStart + count,
                        Math.min(count, arr.length - leftStart - count), order, isCaseSensitive);
            }
    }

    protected void str_Merge(String[] arr, List<String> temparr, int leftStart, int leftCount, int rightStart,
            int rightCount, String order, boolean isCaseSensitive) {
        int i = leftStart, j = rightStart, leftBound = leftStart + leftCount, rightBound = rightStart + rightCount,
                index = leftStart;
        while (i < leftBound || j < rightBound) {
            if (i < leftBound && j < rightBound) {
                boolean isChange = order == SortOrder.Ascending ? stringCompare(arr[i], arr[j], isCaseSensitive) > 0
                        : stringCompare(arr[i], arr[j], isCaseSensitive) < 0;
                if (isChange) {
                    temparr.add(index, arr[j]);
                    j++;
                } else {
                    temparr.add(index, arr[i]);
                    i++;
                }
            } else if (i < leftBound) {
                temparr.add(index, arr[i]);
                i++;
            } else {
                temparr.add(index, arr[j]);
                j++;
            }
            index++;
        }
        for (i = leftStart; i < index; ++i)
            arr[i] = temparr.get(i);
        store(arr, i - 1, i - 2);
    }

    @Override
    public String getSortType() {
        return SortType.MergeSort;
    }
}