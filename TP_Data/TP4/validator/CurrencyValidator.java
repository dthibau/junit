package org.formation.validator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class CurrencyValidator {

    public static String[] supportedCurrencies = {"EUR","YEN"};


    public static void validate(String currency) throws ValidationException {
        boolean supported = Arrays.stream(supportedCurrencies).anyMatch(x -> Objects.equals(x, currency));
        if ( !supported )
            throw new ValidationException("Currency not supported");
    }

    public static class ValidationException extends Exception {
        private static final long serialVersionUID = -134518049431883102L;

        public ValidationException(String message) {
            super(message);
        }
    }
}
