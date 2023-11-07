package com.certh.annotationtoolapp.model.usecase;

public class LanguageOption {
    
    private String value;
    private String label;

    public LanguageOption() {}

    public LanguageOption(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "LanguageOption{" +
                "value='" + value + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
