package org.simple.bankingsystem.helpers;

public interface StringHelper {
    /** Generates a random string.
     * @param length The length of the string.
     * @param stringCapitalization A flag which indicates how the string should be capitalized.
     * @return A random string.
     */
    String randomStringGenerator(int length, StringContentEnum stringCapitalization);
}
