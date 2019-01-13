package com.michelle.toursin;

import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {

    // Regular Expression
    // you can change the expression based on your need
    //private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    //OR logical operator in the regex | denoted = sentosa3, 34, 83sentosa, 34sentosa44, sentosa44sentosa
    private static final String LOCATION_REGEX = "([A-Za-z]+[0-9]+|[0-9]+|[0-9]++[A-Za-z]++|[0-9]++[A-Za-z]++[0-9]+|[A-Za-z]+[0-9]+[A-Za-z]+)";

    // Error Messages
    private static final String REQUIRED_MSG = "required";
    private static final String LOCATION_MSG = "invalid location";

    // call this method when you need to check email validation
    public static boolean isLocation(AutoCompleteTextView autoCompleteTextView, boolean required) {
        return isValid(autoCompleteTextView, LOCATION_REGEX, LOCATION_MSG, required);
    }


    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(AutoCompleteTextView autoCompleteTextView, String regex, String errMsg, boolean required) {

        String text = autoCompleteTextView.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        autoCompleteTextView.setError(null);

        // text required and editText is blank, so return false
        if ( required && !hasText(autoCompleteTextView) ) return false;

        // pattern doesn't match so returning false
        if (required && Pattern.matches(regex, text)) {   //CHANGED HERE TO ACCEPT NOT LOCATION_REGEX
            autoCompleteTextView.setError(errMsg);
            return false;
        }

        return true;
    }

    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(AutoCompleteTextView autoCompleteTextView) {

        String text = autoCompleteTextView.getText().toString().trim();
        autoCompleteTextView.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            autoCompleteTextView.setError(REQUIRED_MSG);
            return false;
        }
        return true;
    }
}