package sort;

import sort.base.*;

public class SelectionSort extends SortSystem {
	public <T extends Number> T[] numberSort(T[] arr, String order) throws RuntimeException {
		if (order == SortOrder.Ascending || order == SortOrder.Descending) {
			store(arr);
			num_SelectionSort(arr, order);
			return arr;
		}
		return null;
	}

	protected <T extends Number> void num_SelectionSort(T[] arr, String order) {
		int index;
		for (int i = 0; i < arr.length; i++) {
			index = i;
			for (int j = i + 1; j < arr.length; j++) {
				boolean isChange = order == SortOrder.Ascending ? numberCompare(arr[index], arr[j]) > 0
						: numberCompare(arr[index], arr[j]) < 0;
				if (isChange)
					index = j;
				if (index != i && j == arr.length - 1) {
					swap(index, i, arr);
					store(arr, i, index);
				}
			}
		}
	}

	@Override
	public String[] stringSort(String[] arr, String order, boolean isCaseSensitive) throws RuntimeException {
		if (order == SortOrder.Ascending || order == SortOrder.Descending) {
			store(arr);
			str_SelectionSort(arr, order, isCaseSensitive);
			return arr;
		}
		return null;
	}

	protected void str_SelectionSort(String[] arr, String order, boolean isCaseSensitive) {
		int index;
		for (int i = 0; i < arr.length; i++) {
			index = i;
			for (int j = i + 1; j < arr.length; j++) {
				boolean isChange = order == SortOrder.Ascending ? stringCompare(arr[index], arr[j], isCaseSensitive) > 0
						: stringCompare(arr[index], arr[j], isCaseSensitive) < 0;
				if (isChange)
					index = j;
				if (index != i && j == arr.length - 1) {
					swap(index, i, arr);
					store(arr, i, index);
				}
			}
		}
	}

	@Override
	public String getSortType() {
		return SortType.SelectionSort;
	}
}