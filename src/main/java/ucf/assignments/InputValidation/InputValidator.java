/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments.InputValidation;

import com.jfoenix.controls.JFXTextField;
import javafx.geometry.Side;
import ucf.assignments.ControllerStyle.JFXTextFieldStyle;
import ucf.assignments.Models.Item;

import java.text.NumberFormat;
import java.util.List;
import java.util.regex.Pattern;

public final class InputValidator {
    private static final Pattern serialNumberPattern = Pattern.compile("^(?:([A-Z0-9]{10})(?!\\w))");
    private static final Pattern pricePattern = Pattern.compile("^(?=.)(([0-9]*)(\\.([0-9]{1,2}))?)$");

    private InputValidator() {
    }

    public static boolean validateNameField(JFXTextField textField) {
        throw new UnsupportedOperationException();
    }

    public static <T extends Item> ValidationState validateSerialNumber(String text, List<T> collection) {
        boolean isMatched = serialNumberPattern.matcher(text).find();
        boolean isFound = collection.stream().anyMatch(obj -> obj.getSerialNumber().getValue().equals(text));

        if (isFound || !isMatched) {
            if (isFound)
                return ValidationState.ALREADY_EXISTS;
            else
                return ValidationState.INCORRECT_FORMAT;
        }

        return ValidationState.PASSED;
    }

    public static ValidationState validatePrice(String text) {
        boolean isMatched = pricePattern.matcher(text).find();

        if (!isMatched)
            return ValidationState.INCORRECT_FORMAT;

        return ValidationState.PASSED;
    }
}
