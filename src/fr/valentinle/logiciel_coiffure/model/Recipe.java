package fr.valentinle.logiciel_coiffure.model;

import java.time.LocalDate;

public class Recipe {
    
    protected LocalDate date;
    
    protected String text;
    
    protected String duration;
    
    public Recipe(LocalDate date, String text, String duration) {
	this.date = date;
	this.text = text;
	this.duration = duration;
    }

    public LocalDate getDate() {
	return date;
    }

    public String getText() {
	return text;
    }

    public String getDuration() {
	return duration;
    }

    public void setDate(LocalDate date) {
	this.date = date;
    }

    public void setText(String text) {
	this.text = text;
    }

    public void setDuration(String duration) {
	this.duration = duration;
    }

    @Override
    public String toString() {
	return LocalDateString.toDateString(date);
    }
}
