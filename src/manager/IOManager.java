package manager;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

import filter.Filter;
import sort.BubbleSort;
import sort.CocktailSort;
import sort.ExchangeSort;
import sort.InsertionSort;
import sort.MergeSort;
import sort.QuickSort;
import sort.SelectionSort;
import sort.base.SortDataType;
import sort.base.SortOrder;
import sort.base.SortSystem;
import sort.base.SortType;
import utility.Regex;
import utility.Utility;

/**
 * Manager for dealing with input / output
 */
public class IOManager {

    public static volatile String displaySeparate = " ";

    public static volatile String splitRegex = "\\s+";

    public static volatile int deleteCountsPerUndo = 5;

    public static final int maxTraceBackTimes = 20;

    public static final int maxTraceForwardTimes = 20;

    public static List<Filter<?>> importFilters = new CopyOnWriteArrayList<>();

    public static List<Filter<?>> sortFilters = new CopyOnWriteArrayList<>();

    public static List<Filter<?>> exportFilters = new CopyOnWriteArrayList<>();

    // default order ascending
    public static volatile String sortOrder = SortOrder.Ascending;

    // is sort use as a string or not
    public static volatile String sortDataType = SortDataType.Number;

    // is string sort use case sensitive or not
    public static volatile boolean isCaseSensitive = true;

    // is need to save the array element of inconsistent
    public static volatile boolean isSaveInconsistentElement = false;

    // it will convert to scientific notation
    public static volatile boolean isUseScientificNotation = true;

    public static Stack<String> traceBackStrings = new Stack<>();

    public static Stack<String> traceForwardStrings = new Stack<>();

    public static final String startMessage = "Start sorting...";

    public static final String finishMessage = "Finished! The result is: ";

    private IOManager() {
    };

    /**
     * Make sort within file array
     * 
     * @param inputArr array of input
     * @param sort     system of sort
     */
    public static String[] makeSort(String[] inputArr, SortSystem sort) {
        if (sortDataType == SortDataType.String) {
            return makeStringSort(inputArr, sort);
        } else if (sortDataType == SortDataType.Number) {
            return makeNumberSort(inputArr, sort);
        } else if (sortDataType == SortDataType.Object) {
            return null;
        } else {
            throw new IllegalArgumentException("Sort data type is not exist!");
        }
    }

    /**
     * Get the sort system
     * 
     * @param sortType {@link SortType}
     * @return sort system of sort type
     */
    public static SortSystem getSortSystem(String sortType) {
        if (sortType == SortType.Insertion) {
            return new InsertionSort();
        } else if (sortType == SortType.BubbleSort) {
            return new BubbleSort();
        } else if (sortType == SortType.QuickSort) {
            return new QuickSort();
        } else if (sortType == SortType.SelectionSort) {
            return new SelectionSort();
        } else if (sortType == SortType.CocktailSort) {
            return new CocktailSort();
        } else if (sortType == SortType.ExchangeSort) {
            return new ExchangeSort();
        } else if (sortType == SortType.MergeSort) {
            return new MergeSort();
        } else {
            throw new IllegalArgumentException("Sort type not exist!");
        }
    }

    // number sort
    private static String[] makeNumberSort(String[] inputArr, SortSystem sort) throws RuntimeException {
        List<Integer> tokenIndices = new ArrayList<>();
        List<Number> unsortednumbers = new ArrayList<>();
        for (int i = 0; i < inputArr.length; ++i) {
            if (Regex.isNumber(inputArr[i]) || Regex.isScientificNotation(inputArr[i])) {
                final int index = i;
                inputArr[i] = inputArr[i].replace("+", "");
                boolean isAccept = sortFilters.parallelStream().anyMatch(filter -> {
                    return Utility.isFilterSuccess(inputArr[index], filter);
                });

                try {
                    if (isAccept || sortFilters.isEmpty()) {
                        tokenIndices.add(i);
                        DecimalFormat df = new DecimalFormat();
                        df.setParseBigDecimal(!isUseScientificNotation);
                        unsortednumbers.add(df.parse(inputArr[i]));
                    }
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            }
        }

        Number[] sortedNumbers = sort.numberSort(unsortednumbers.toArray(new Number[0]), sortOrder);
        return getOutputArray(inputArr, sortedNumbers, tokenIndices);
    }

    // string sort
    private static String[] makeStringSort(String[] inputArr, SortSystem sort) throws RuntimeException {
        List<Integer> tokenIndices = new ArrayList<>();
        List<String> unsortedStrings = new ArrayList<>();
        for (int i = 0; i < inputArr.length; ++i) {
            final int index = i;
            boolean isAccept = sortFilters.parallelStream().anyMatch(filter -> {
                return Utility.isFilterSuccess(inputArr[index], filter);
            });
            if (isAccept || sortFilters.isEmpty()) {
                tokenIndices.add(i);
                unsortedStrings.add(inputArr[i]);
            }
        }
        String[] sortedStrings = sort.stringSort(unsortedStrings.toArray(new String[0]), sortOrder, isCaseSensitive);
        return getOutputArray(inputArr, sortedStrings, tokenIndices);
    }

    private static <NumStr> String[] getOutputArray(String[] inputArr, NumStr[] sortedNumStrs,
            List<Integer> tokenIndices) {
        if (isSaveInconsistentElement) {
            int i = 0;
            for (Integer tokenIndex : tokenIndices) {
                inputArr[tokenIndex] = sortedNumStrs[i].toString();
                ++i;
            }
            return inputArr;
        } else {
            List<String> outputArr = new ArrayList<>();
            for (NumStr num : sortedNumStrs) {
                outputArr.add(num.toString());
            }
            return outputArr.toArray(new String[0]);
        }
    }
}