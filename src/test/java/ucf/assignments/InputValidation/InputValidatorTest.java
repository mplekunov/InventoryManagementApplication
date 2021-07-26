/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments.InputValidation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import ucf.assignments.Models.Item;
import ucf.assignments.Models.ItemModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class InputValidatorTest {
    private final List<Item> items = List.of(
            new Item("Prince", "1", 1d, null),
            new Item("Did", "2", 2d, null),
            new Item("Nothing", "3", 3d, null),
            new Item("Wrong", "4", 4d, null));


    @ParameterizedTest
    @ValueSource(strings = {"qweqwe", "123123", "QWERTYUIOw", "wQWERTYUIO", "Q1ERTYUIO-", "Q1ERTYUIOe", "Q1ERTYUIOPP", "Q1ERTYUIOP1"})
    @DisplayName("Incorrect Format in Serial Name Validation")
    void TestValidateSerialNumberIncorrectFormat(String input) {
        assertEquals(ValidationState.INCORRECT_FORMAT, InputValidator.validateSerialNumber(input, items));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "3"})
    @DisplayName("Duplicates in Serial Name Validation")
    void TestValidateSerialNumberDuplicate(String input) {
        assertEquals(ValidationState.ALREADY_EXISTS, InputValidator.validateSerialNumber(input, items));
    }

    @ParameterizedTest
    @ValueSource(strings = {"QWERTYUIOP", "QWERTYUI2P"})
    @DisplayName("Correct Format in Serial Name Validation")
    void TestValidateSerialNumberCorrectFormat(String input) {
        assertEquals(ValidationState.PASSED, InputValidator.validateSerialNumber(input, items));
    }

    @ParameterizedTest
    @ValueSource(strings = {"12.", "12.d", "d12", "12.1d", "12.11d", "12d", "12.123"})
    @DisplayName("Incorrect Format in Price Validation")
    void TestValidatePriceIncorrectFormat(String input) {
        assertEquals(ValidationState.INCORRECT_FORMAT, InputValidator.validatePrice(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"12", "12.1", "12.0", "12.12", "1"})
    @DisplayName("Incorrect Format in Price Validation")
    void TestValidatePriceCorrectFormat(String input) {
        assertEquals(ValidationState.PASSED, InputValidator.validatePrice(input));
    }
}