package org.simple.bankingsystem.helpers;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class StringHelperImpl implements StringHelper {

    private final int lowercaseA = 97;
    private final int lowercaseZ = 123;
    private final int uppercaseA = 65;
    private final int uppercaseZ = 90;
    private final int letter0 = 48;
    private final int letter9 = 57;

    public String randomStringGenerator(int length, StringContentEnum stringCapitalization) {

        if (length < 1 || stringCapitalization == null) {
            return "";
        }

        switch (stringCapitalization) {
            case NO_LETTERS_CAPITALIZED -> {
                return getRandomString(length, lowercaseA, lowercaseZ);
            }
            case FIRST_LETTER_CAPITALIZED -> {
                String firstLetter = getRandomString(1, uppercaseA, uppercaseZ);
                length -= 1;
                return firstLetter + getRandomString(length, lowercaseA, lowercaseZ);
            }
            case ALL_LETTERS_CAPITALIZED -> {
                return getRandomString(length, uppercaseA, uppercaseZ);
            }
            case NO_LETTERS_ALL_NUMBERS ->
            {
                return getRandomString(length, letter0, letter9);
            }
            default -> {
                return "";
            }
        }
    }

    private String getRandomString(int length, int start, int end) {
        return new Random().ints(start, end)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
