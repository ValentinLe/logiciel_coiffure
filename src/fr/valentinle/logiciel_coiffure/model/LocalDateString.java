package fr.valentinle.logiciel_coiffure.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateString {
    
    public static String toDateString(LocalDate date) {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH);
	return date.format(formatter);
    }
    
    public static String toDateMonthYearString(LocalDate date) {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH);
	return date.format(formatter);
    }
    
    public static String toDateSlashString(LocalDate date) {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);
	return date.format(formatter);
    }
    
    public static String toDateDashString(LocalDate date) {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.FRENCH);
	return date.format(formatter);
    }

}
