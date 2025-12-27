package com.example.dawn.utils.validation.rules;

public interface IValidationRule {
    boolean isValid(String value);
    String getErrorMessage();
}