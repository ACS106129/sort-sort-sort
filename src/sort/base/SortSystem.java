package sort.base;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import manager.IOManager;

public abstract class SortSystem {

    // key : set of string, value : moved indice
    private Map<String, int[]> arrayStepMaps = new LinkedHashMap<>();

    /**
     * Sort the array by number
     * 
     * @param arr   any array of generic T
     * @param order order of sort
     * @return the sorted array of generic T, null if failed
     */
    public abstract <T extends Number> T[] numberSort(T[] arr, String order) throws RuntimeException;

    /**
     * Sort the array by string
     * 
     * @param arr             any array of string
     * @param order           order of sort
     * @param isCaseSensitive if true use sensitive sort
     * @return the sorted array of string, null if failed
     */
    public abstract String[] stringSort(String[] arr, String order, boolean isCaseSensitive) throws RuntimeException;

    /**
     * Get the current sort type
     * 
     * @return sort type of {@link SortType}
     */
    public abstract String getSortType();

    /**
     * Get steps of array
     * 
     * @return map of every step
     */
    public final Map<String, int[]> getArrayStepMaps() {
        return arrayStepMaps;
    }

    /**
     * Store the data to the maps
     * 
     * @param arr         the target array to be store in every steps
     * @param movedIndice the index moved in every steps
     */
    protected final <T> void store(final T[] arr, final int... movedIndice) {
        String line = "";
        for (int printstep = 0; printstep < arr.length; printstep++) {
            line += arr[printstep].toString();
            if (printstep < arr.length - 1)
                line += IOManager.displaySeparate;
        }
        if (!arrayStepMaps.containsKey(line)) {
            for (int index : movedIndice) {
                if (index < 0 || index >= arr.length) {
                    throw new IndexOutOfBoundsException("Index out of bounds in movedIndice!");
                }
            }
            arrayStepMaps.put(line, movedIndice);
        }
    }

    /**
     * Swap left and right elements by index
     */
    protected final <T> void swap(final int left, final int right, T[] arr) {
        final T temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;
    }

    protected final <T extends Number> BigDecimal sum(final T left, final T right) {
        final BigDecimal leftNum = new BigDecimal(left.toString());
        final BigDecimal rightNum = new BigDecimal(right.toString());
        return leftNum.add(rightNum);
    }

    protected final <T extends Number> BigDecimal subtract(final T left, final T right) {
        final BigDecimal leftNum = new BigDecimal(left.toString());
        final BigDecimal rightNum = new BigDecimal(right.toString());
        return leftNum.subtract(rightNum);
    }

    protected final <T extends Number> BigDecimal product(final T left, final T right) {
        final BigDecimal leftNum = new BigDecimal(left.toString());
        final BigDecimal rightNum = new BigDecimal(right.toString());
        return leftNum.multiply(rightNum);
    }

    protected final <T extends Number> BigDecimal quotient(final T left, final T right) {
        final BigDecimal leftNum = new BigDecimal(left.toString());
        final BigDecimal rightNum = new BigDecimal(right.toString());
        return leftNum.divide(rightNum);
    }

    /**
     * Same as left compareTo right function: 1 is greater, -1 is less than, 0 is
     * equal
     */
    protected final <T extends Number> int numberCompare(final T left, final T right) throws ClassCastException {
        final BigDecimal leftNum = new BigDecimal(left.toString());
        final BigDecimal rightNum = new BigDecimal(right.toString());
        return leftNum.compareTo(rightNum);
    }

    /**
     * String compare, length compare first then alphabet compare
     * 
     * @param isCaseSensitive is case sensitive or not
     * @return 1 or -1 if length not equals
     *         <p>
     *         if length equals 2 or -2 if alphabet not equals
     *         sequentially(according to {@code isCaseSensitive})
     *         <p>
     *         0 if equals(according to {@code isCaseSensitive})
     */
    protected final int stringCompare(final String left, final String right, final boolean isCaseSensitive) {
        if (left.length() > right.length())
            return 1;
        else if (left.length() < right.length())
            return -1;
        if (isCaseSensitive)
            if (left.compareTo(right) > 0)
                return 2;
            else if (left.compareTo(right) < 0)
                return -2;
        if (left.compareToIgnoreCase(right) > 0)
            return 2;
        else if (left.compareToIgnoreCase(right) < 0)
            return -2;
        return 0;
    }
}