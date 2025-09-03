package searchengine.services.lemmatization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class BasicLemma {
    String originalWord;
    String normalWord;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicLemma that = (BasicLemma) o;
        return Objects.equals(normalWord, that.normalWord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(normalWord);
    }
}
