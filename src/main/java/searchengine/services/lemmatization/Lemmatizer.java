package searchengine.services.lemmatization;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Lemmatizer {
    private final LuceneMorphology luceneMorphology;
    private static final String[] particlesNames = new String[]{"МЕЖД", "ПРЕДЛ", "СОЮЗ"};

    public Lemmatizer() throws Exception{
        this.luceneMorphology = new RussianLuceneMorphology();
    }

    public HashMap<String, Integer> getLemmasFromHtml(String html){
        String text = convertHtml2text(html);
        return getLemmasFromText(text);
    }

    public HashMap<String, Integer> getLemmasFromText(String text){
        String[] wordsArray = splitTextIntoWords(text);
        HashMap<String, Integer> lemmas = new HashMap<>();

        for (String word : wordsArray) {
            if (word.isBlank()){
                continue;
            }

            List<String> wordBaseForms = luceneMorphology.getMorphInfo(word);
            if (anyWordBaseBelongToParticle(wordBaseForms)) {
                continue;
            }

            List<String> normalForms = luceneMorphology.getNormalForms(word);
            if (normalForms.isEmpty()) {
                continue;
            }

            String normalWord = normalForms.get(0);

            if (lemmas.containsKey(normalWord)) {
                lemmas.put(normalWord, lemmas.get(normalWord) + 1);
            } else {
                lemmas.put(normalWord, 1);
            }
        }
        return lemmas;
    }

    private static String convertHtml2text (String html){
        return Jsoup.parse(html).text();
    }

    private String[] splitTextIntoWords(String text) {
        String regexReplace = "[^\\sа-я]"; // все, что не пробелы и не буквы и цифры
        String[] onlyRussianWords = text
                .toLowerCase(Locale.ROOT)
                .replaceAll(regexReplace, " ")
                .trim()
                .split("\\s+");
        return onlyRussianWords;
    }

    private boolean anyWordBaseBelongToParticle(List<String> wordBaseForms){
        return wordBaseForms.stream().anyMatch(this::hasParticleProperty);
    }

    private boolean hasParticleProperty(String wordBase){
        for (String property : particlesNames){
            if (wordBase.toUpperCase().contains(property)) {
                return true;
            }
        }
        return false;
    }
}
