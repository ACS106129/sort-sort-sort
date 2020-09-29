package utility;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.function.Function;

import filter.Filter;
import filter.FilterType;

public class Utility {
    /**
     * Joins the elements of the provided array into a single String containing the
     * provided list of elements.
     *
     * No delimiter is added before or after the list. A <code>null</code> separator
     * is the same as an empty String (""). Null objects or empty strings within the
     * array are represented by empty strings.
     *
     * <pre>
     * StringUtils.join(null, *)                = null
     * StringUtils.join([], *)                  = ""
     * StringUtils.join([null], *)              = ""
     * StringUtils.join(["a", "b", "c"], "--")  = "a--b--c"
     * StringUtils.join(["a", "b", "c"], null)  = "abc"
     * StringUtils.join(["a", "b", "c"], "")    = "abc"
     * StringUtils.join([null, "", "a"], ',')   = ",,a"
     * </pre>
     *
     * @param array      the array of values to join together, may be null
     * @param separator  the separator character to use, null treated as ""
     * @param startIndex the first index to start joining from. It is an error to
     *                   pass in an end index past the end of the array
     * @param endIndex   the index to stop joining from (exclusive). It is an error
     *                   to pass in an end index past the end of the array
     * @return the joined String, <code>null</code> if null array input
     */
    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = "";
        }

        int bufSize = (endIndex - startIndex);
        if (bufSize <= 0) {
            return "";
        }

        bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length()) + separator.length());

        // use buffer to accelerate compute
        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    /**
     * If element is be filtered successly return true
     * 
     * @param element target element to be filtered
     * @param filter  filter
     * @return if filter success return true
     */
    @SuppressWarnings("unchecked")
    public static <DataType> boolean isFilterSuccess(String element, Filter<DataType> filter) {
        if (filter.genericType == String.class) {
            if (filter.accept((DataType) element)) {
                return true;
            }
        } else if (filter.genericType == Character.class) {
            if (element.length() == 1 && filter.accept((DataType) Character.class.cast(element.charAt(0)))) {
                return true;
            }
        } else if (filter.genericType == Number.class) {
            try {
                if ((Regex.isNumber(element) || Regex.isScientificNotation(element))
                        && filter.accept((DataType) new DecimalFormat().parse(element))) {
                    return true;
                }
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        } else if (filter.genericType == BigInteger.class) {
            if ((Regex.isNumber(element) || Regex.isScientificNotation(element))
                    && filter.accept((DataType) new BigDecimal(element).toBigInteger())) {
                return true;
            }
        } else if (filter.genericType == BigDecimal.class) {
            if ((Regex.isNumber(element) || Regex.isScientificNotation(element))
                    && filter.accept((DataType) new BigDecimal(element))) {
                return true;
            }
        } else if (Regex.isInteger(element)) {
            try {
                if (filter.genericType == Integer.class && filter.accept((DataType) Integer.valueOf(element))) {
                    return true;
                } else if (filter.genericType == Long.class && filter.accept((DataType) Long.valueOf(element))) {
                    return true;
                }
            } catch (NumberFormatException nfe) {
                System.out.println(nfe.getMessage() + " integer cast to " + filter.genericType.getName() + " ignored.");
            }
        } else if (Regex.isFloat(element)) {
            try {
                if (filter.genericType == Float.class && filter.accept((DataType) Float.valueOf(element))) {
                    return true;
                } else if (filter.genericType == Double.class && filter.accept((DataType) Double.valueOf(element))) {
                    return true;
                }
            } catch (NumberFormatException nfe) {
                System.out.println(nfe.getMessage() + " float cast to " + filter.genericType.getName() + " ignored.");
            }
        }
        return false;
    }

    /**
     * Get the instance of filter
     * 
     * @param type filter type
     * @return instance of filter
     */
    public static Filter<?> getFilterInstance(FilterType type, String code) {
        Filter<?> filter = null;
        Function<Object, Boolean> func = generateFunc(code);
        if (type.getValue() == Number.class) {
            filter = new Filter<Number>() {
                @Override
                public boolean accept(Number target) {
                    return func.apply(target);
                }
            };
        } else if (type.getValue() == Integer.class) {
            filter = new Filter<Integer>() {
                @Override
                public boolean accept(Integer target) {
                    return func.apply(target);
                }
            };
        } else if (type.getValue() == Long.class) {
            filter = new Filter<Long>() {
                @Override
                public boolean accept(Long target) {
                    return func.apply(target);
                }
            };
        } else if (type.getValue() == BigInteger.class) {
            filter = new Filter<BigInteger>() {
                @Override
                public boolean accept(BigInteger target) {
                    return func.apply(target);
                }
            };
        } else if (type.getValue() == Float.class) {
            filter = new Filter<Float>() {
                @Override
                public boolean accept(Float target) {
                    return func.apply(target);
                }
            };
        } else if (type.getValue() == Double.class) {
            filter = new Filter<Double>() {
                @Override
                public boolean accept(Double target) {
                    return func.apply(target);
                }
            };
        } else if (type.getValue() == BigDecimal.class) {
            filter = new Filter<BigDecimal>() {
                @Override
                public boolean accept(BigDecimal target) {
                    return func.apply(target);
                }
            };
        } else if (type.getValue() == Character.class) {
            filter = new Filter<Character>() {
                @Override
                public boolean accept(Character target) {
                    return func.apply(target);
                }
            };
        } else if (type.getValue() == String.class) {
            filter = new Filter<String>() {
                @Override
                public boolean accept(String target) {
                    return func.apply(target);
                }
            };
        }
        return filter;
    }

    /**
     * Generate function with the code by syntax:
     * <p>
     * {@code target == datatype}: target equal datatype
     * <p>
     * {@code target > datatype | target >= datatype}: target greater (equal) than
     * datatype
     * <p>
     * {@code target < datatype | target <= datatype}: target less (equal) than
     * datatype
     * 
     * <h3><strong>Belows are only for target is {@code String} type</strong>
     * <p>
     * {@code target.len}: return target(string type) length
     * <p>
     * {@code target.contain(string)}: target(string type) contain the string or
     * not, same as {@link String} {@code contains(CharSequence)}
     * <p>
     * {@code target.in(string)}: target(string type) in the string or not
     * 
     * @param <DataType> data type
     * @param code       code
     * @return function of code generated
     */
    private static <DataType> Function<DataType, Boolean> generateFunc(String code) {
        if (code.isEmpty())
            return target -> true;
        // functions to check if pass
        ArrayList<Function<DataType, Boolean>> funcArray = new ArrayList<>();
        for (String line : code.split(";")) {
            line.trim();
            if (line.contains("contain")) {
                // target.contain("str")
                final String para = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                funcArray.add(target -> {
                    if (target instanceof String) {
                        return target.toString().contains(para);
                    }
                    throw new IllegalArgumentException("Cannot use contain(string)! source: " + target.getClass());
                });
            } else if (line.contains("in")) {
                // target.in("str")
                final String para = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                funcArray.add(target -> {
                    if (target instanceof String) {
                        return para.contains(target.toString());
                    }
                    throw new IllegalArgumentException("Cannot use in(string)! source: " + target.getClass());
                });
            } else if (line.contains("==")) {
                addEqualSign(line, funcArray);
            } else if (line.contains(">")) {
                addGreaterSign(line, funcArray);
            } else if (line.contains("<")) {
                addLessSign(line, funcArray);
            }
        }
        return target -> {
            if (!(target instanceof Number || target instanceof Character || target instanceof String)) {
                throw new IllegalArgumentException("Unknown data type in: " + target.getClass() + "!");
            }
            for (Function<DataType, Boolean> func : funcArray) {
                if (!func.apply(target)) {
                    return false;
                }
            }
            return true;
        };
    }

    // add function when equal(==) in code
    private static <DataType> void addEqualSign(String line, ArrayList<Function<DataType, Boolean>> funcArray) {
        boolean isUseLen = false;
        for (String word : line.split("==")) {
            String trimmedWord = word.trim();
            if (trimmedWord.equals("target"))
                continue;
            else if (trimmedWord.equals("target.len")) {
                isUseLen = true;
                continue;
            }
            // add function to deal with char
            if (trimmedWord.contains("\'")) {
                // 'x' token '' to x
                trimmedWord = trimmedWord.replace("\'", "");
                final char finalChar = trimmedWord.charAt(0);
                funcArray.add(target -> {
                    if (target instanceof Number) {
                        return compareNumbers(Number.class.cast(target), (int) finalChar) == 0;
                    } else if (target instanceof Character) {
                        return Character.class.cast(target).charValue() == finalChar;
                    } else {
                        throw new IllegalArgumentException("Cannot compare to character! source: " + target.getClass());
                    }
                });
                // add function to deal with string
            } else if (trimmedWord.contains("\"")) {
                // "str" token "" to str
                trimmedWord = trimmedWord.replace("\"", "");
                final String finalWord = trimmedWord;
                funcArray.add(target -> {
                    if (target instanceof String) {
                        return target.toString().equals(finalWord);
                    } else {
                        throw new IllegalArgumentException("Cannot compare to string! source: " + target.getClass());
                    }
                });
                // add function to deal with number, and target.len
            } else {
                final BigDecimal finalNum = new BigDecimal(trimmedWord);
                final boolean finalIsUseLen = isUseLen;
                funcArray.add(target -> {
                    if (target instanceof Number) {
                        return compareNumbers(Number.class.cast(target), finalNum) == 0;
                    } else if (finalIsUseLen && target instanceof String) {
                        return compareNumbers(target.toString().length(), finalNum) == 0;
                    } else {
                        throw new IllegalArgumentException("Cannot compare to number! source: " + target.getClass());
                    }
                });
            }
        }
    }

    // add function when greater(>) or greater equal(>=) in code
    private static <DataType> void addGreaterSign(String line, ArrayList<Function<DataType, Boolean>> funcArray) {
        boolean isIteredTarget = false;
        boolean isUseLen = false;
        for (String word : line.split(">")) {
            String trimmedWord = word.trim();
            if (trimmedWord.equals("target")) {
                isIteredTarget = true;
                continue;
            } else if (trimmedWord.equals("target.len")) {
                isIteredTarget = true;
                isUseLen = true;
                continue;
            }
            boolean isHasEqualSign = false;
            if (trimmedWord.charAt(0) == '=') {
                isHasEqualSign = true;
                // "= word" to "word"
                trimmedWord = trimmedWord.substring(1).trim();
            }
            final boolean finalIsIteredTarget = isIteredTarget;
            final boolean finalIsHasEqualSign = isHasEqualSign;
            // add function to deal with char
            if (trimmedWord.contains("\'")) {
                // 'x' token '' to x
                trimmedWord = trimmedWord.replace("\'", "");
                final char finalChar = trimmedWord.charAt(0);
                funcArray.add(target -> {
                    if (target instanceof Number) {
                        // target front
                        if (finalIsIteredTarget) {
                            if (finalIsHasEqualSign)
                                return compareNumbers(Number.class.cast(target), (int) finalChar) >= 0;
                            return compareNumbers(Number.class.cast(target), (int) finalChar) > 0;
                        }
                        // target back
                        if (finalIsHasEqualSign)
                            return compareNumbers((int) finalChar, Number.class.cast(target)) >= 0;
                        return compareNumbers((int) finalChar, Number.class.cast(target)) > 0;
                    } else if (target instanceof Character) {
                        // target front
                        if (finalIsIteredTarget) {
                            if (finalIsHasEqualSign)
                                return Character.class.cast(target).charValue() >= finalChar;
                            return Character.class.cast(target).charValue() > finalChar;
                        }
                        // target back
                        if (finalIsHasEqualSign)
                            return finalChar >= Character.class.cast(target).charValue();
                        return finalChar > Character.class.cast(target).charValue();
                    } else {
                        throw new IllegalArgumentException("Cannot compare to character! source: " + target.getClass());
                    }
                });
                // add function to deal with string
            } else if (trimmedWord.contains("\"")) {
                // "str" token "" to str
                trimmedWord = trimmedWord.replace("\"", "");
                final String finalWord = trimmedWord;
                funcArray.add(target -> {
                    if (target instanceof String) {
                        String targetStr = target.toString();
                        // target front
                        if (finalIsIteredTarget) {
                            if (targetStr.length() != finalWord.length())
                                return targetStr.length() > finalWord.length();
                            if (finalIsHasEqualSign)
                                return targetStr.compareTo(finalWord) >= 0;
                            return targetStr.compareTo(finalWord) > 0;
                        }
                        // target back
                        if (targetStr.length() != finalWord.length())
                            return finalWord.length() > targetStr.length();
                        if (finalIsHasEqualSign)
                            return finalWord.compareTo(targetStr) >= 0;
                        return finalWord.compareTo(targetStr) > 0;
                    } else {
                        throw new IllegalArgumentException("Cannot compare to string! source: " + target.getClass());
                    }
                });
                // add function to deal with number, and target.len
            } else {
                try {
                    final Number finalNum = new DecimalFormat().parse(trimmedWord);
                    final boolean finalIsUseLen = isUseLen;
                    funcArray.add(target -> {
                        Number targetNum = null;
                        if (target instanceof Number) {
                            targetNum = Number.class.cast(target);
                        } else if (finalIsUseLen && target instanceof String) {
                            targetNum = target.toString().length();
                        }
                        // if target is not a number
                        if (targetNum == null) {
                            throw new IllegalArgumentException(
                                    "Cannot compare to number! source: " + target.getClass());
                        }
                        // target front
                        if (finalIsIteredTarget) {
                            if (finalIsHasEqualSign)
                                return compareNumbers(targetNum, finalNum) >= 0;
                            return compareNumbers(targetNum, finalNum) > 0;
                        }
                        // target back
                        if (finalIsHasEqualSign)
                            return compareNumbers(finalNum, targetNum) >= 0;
                        return compareNumbers(finalNum, targetNum) > 0;
                    });
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            }
        }
    }

    // add function when less(<) or less equal(<=) in code
    private static <DataType> void addLessSign(String line, ArrayList<Function<DataType, Boolean>> funcArray) {
        ArrayList<Function<DataType, Boolean>> tempFuncArr = new ArrayList<>();
        addGreaterSign(line.replace("<=", ">").replace("<", ">="), tempFuncArr);
        for (Function<DataType, Boolean> func : tempFuncArr) {
            funcArray.add(target -> !func.apply(target));
        }
    }

    private static int compareNumbers(Number left, Number right) {
        BigDecimal leftNum = new BigDecimal(left.toString());
        BigDecimal rightNum = new BigDecimal(right.toString());
        return leftNum.compareTo(rightNum);
    }
}