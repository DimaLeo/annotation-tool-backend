package com.certh.annotationtoolapp.model.usecase;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "UsecaseOptions")
public class Usecase {

    @Id
    private String id;  
    private String value;  
    private String label;
    private List<LanguageOption> language;

    public Usecase() {}

    public Usecase(String value, String label, List<LanguageOption> language) {
        this.value = value;
        this.label = label;
        this.language = language;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<LanguageOption> getLanguage() {
        return language;
    }

    public void setLanguage(List<LanguageOption> language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "Usecase{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                ", label='" + label + '\'' +
                ", language=" + language +
                '}';
    }
}
