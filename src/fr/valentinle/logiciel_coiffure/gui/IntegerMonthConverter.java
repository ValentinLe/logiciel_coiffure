package fr.valentinle.logiciel_coiffure.gui;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import javafx.util.StringConverter;

public class IntegerMonthConverter extends StringConverter<Integer> {

    // Method to convert a Person-Object to a String
    @Override
    public String toString(Integer n) {
	return n == null ? null : LocalDate.of(2000, n, 1).getMonth().getDisplayName(TextStyle.FULL, Locale.FRANCE);
    }

    // Method to convert a String to a Person-Object
    @Override
    public Integer fromString(String string) {
	Integer n = null;

	if (string == null) {
	    return n;
	}
	
	n = 0;

	return n;
    }
}
