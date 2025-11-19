package engjao89.rest_with_spring_boot_and_java.util;

import engjao89.rest_with_spring_boot_and_java.exception.UnsupportedMathOperationException;

public class NumberConverter {

    public static Double convertToDouble(String strNumber) {
        if (strNumber == null || strNumber.isEmpty()) {
            throw new UnsupportedMathOperationException("Please set a numeric value");
        }

        String number = strNumber.replace(",", ".");
        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException e) {
            throw new UnsupportedMathOperationException("Please set a numeric value");
        }
    }

    public static boolean isNumeric(String strNumber) {
        if (strNumber == null || strNumber.isEmpty()) {
            return false;
        }

        String number = strNumber.replace(",", ".");
        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    public static Double convertToDoubleOrThrow(String strNumber) {
        if (!isNumeric(strNumber)) {
            throw new UnsupportedMathOperationException("Please set a numeric value");
        }
        return convertToDouble(strNumber);
    }
}

