package com.certh.annotationtoolapp.model.usecase;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document(collection = "UsecaseOptions")
public class Usecase {

    @Id
    private String id;  
    private String value;  
    private String label;
    private List<LanguageOption> language;

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
