package sort;

import sort.base.*;

public class BubbleSort extends SortSystem {
	public <T extends Number> T[] numberSort(T[] arr, String order) throws RuntimeException {
		if (order == SortOrder.Ascending || order == SortOrder.Descending) {
			store(arr);
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
			return arr;
		}
		return null;
	}

	@Override
	public String[] stringSort(String[] arr, String order, boolean isCaseSensitive) throws RuntimeException {
		if (order == SortOrder.Ascending || order == SortOrder.Descending) {
			store(arr);
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
			return arr;
		}
		return null;
	}

	@Override
	public String getSortType() {
		return SortType.BubbleSort;
	}
}