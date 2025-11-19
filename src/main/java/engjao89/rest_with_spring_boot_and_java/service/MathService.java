package engjao89.rest_with_spring_boot_and_java.service;

import engjao89.rest_with_spring_boot_and_java.exception.UnsupportedMathOperationException;
import engjao89.rest_with_spring_boot_and_java.util.NumberConverter;
import org.springframework.stereotype.Service;

@Service
public class MathService {

    public Double sum(String numberOne, String numberTwo) {
        Double num1 = NumberConverter.convertToDoubleOrThrow(numberOne);
        Double num2 = NumberConverter.convertToDoubleOrThrow(numberTwo);
        return num1 + num2;
    }

    public Double subtraction(String numberOne, String numberTwo) {
        Double num1 = NumberConverter.convertToDoubleOrThrow(numberOne);
        Double num2 = NumberConverter.convertToDoubleOrThrow(numberTwo);
        return num1 - num2;
    }

    public Double multiplication(String numberOne, String numberTwo) {
        Double num1 = NumberConverter.convertToDoubleOrThrow(numberOne);
        Double num2 = NumberConverter.convertToDoubleOrThrow(numberTwo);
        return num1 * num2;
    }

    public Double division(String numberOne, String numberTwo) {
        Double num1 = NumberConverter.convertToDoubleOrThrow(numberOne);
        Double num2 = NumberConverter.convertToDoubleOrThrow(numberTwo);
        
        if (num2 == 0) {
            throw new UnsupportedMathOperationException("Division by zero is not allowed");
        }
        
        return num1 / num2;
    }

    public Double average(String numberOne, String numberTwo) {
        Double num1 = NumberConverter.convertToDoubleOrThrow(numberOne);
        Double num2 = NumberConverter.convertToDoubleOrThrow(numberTwo);
        return (num1 + num2) / 2.0;
    }

    public Double squareRoot(String number) {
        Double value = NumberConverter.convertToDoubleOrThrow(number);
        
        if (value < 0) {
            throw new UnsupportedMathOperationException("Square root of negative number is not allowed");
        }
        
        return Math.sqrt(value);
    }
}

