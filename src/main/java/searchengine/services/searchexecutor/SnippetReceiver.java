package searchengine.services.searchexecutor;

import org.jsoup.Jsoup;

import java.util.Comparator;
import java.util.Optional;


public class SnippetReceiver {
    private PageEnriched pageEnriched;
    private LemmaEnriched maxFrquenceLemmaEnriched;
    private String maxFrequencyLemmaSnippet;
    private String addonBefore;
    private String addonAfter;
    private final String NO_SNIPPET = "Snippet for this page couldn't be received";
    private final int ADDON_OF_SNIPPET_LENGTH = 15;
    private final String BOLDING_START_SYMBOL = "<b>";
    private final String BOLDING_END_SYMBOL = "</b>";

    public SnippetReceiver(PageEnriched pageEnriched){
        this.pageEnriched = pageEnriched;
        maxFrequencyLemmaSnippet = "";
        addonBefore = "";
        addonAfter = "";
    }

    public String getMaxFrequencyLemmaSnippet(){
        if (maxFrequencyLemmaSnippet.isEmpty()) {
            fillSnippetForMaxFrequencyLemma();
        }
        return maxFrequencyLemmaSnippet;
    }

    private void fillSnippetForMaxFrequencyLemma(){
        Optional<LemmaEnriched> lemmaEnrichedMaxFrequency = pageEnriched.getLemmaEnrichedSet().stream()
                .sorted((l1,l2) -> Double.compare(l2.getFrequency(), l1.getFrequency()))
                .findFirst();
        lemmaEnrichedMaxFrequency.ifPresentOrElse(
                lemmaEnriched -> {
                    maxFrquenceLemmaEnriched = lemmaEnriched;
                    fillSnippetByWord();
                    //fillSnippetByWord(lemmaEnriched.getBasicLemma().getOriginalWord());
                },
                () ->  fillDefaultSnippet()
        );
    }

    //TODO все это проверить.
    public void fillSnippetByWord(){
        String text = convertHtml2text(pageEnriched.getPage().getContent());
        String originalWord = maxFrquenceLemmaEnriched.getBasicLemma().getOriginalWord();
        text = "There is a book. This bbok нашла попала сша. экономика сша i попала s one of the greatest country ";
  //      text = "There Экономика is a Сша. This bbok fr";
        text = "There is a book. This bbok нала сша. экономика ";
        text = "There is a book. This bbok текст текс наа сша. экономика 1";
        text = "There is a book. This bbok текст текс наа сша.. экономика 1";
        text = "There is a book. This bbok текст текс наа сша.ю. экономика 1";
        text = "There экономика is a book. This bbok текст текс наа сша.ю.  1";
        text = "There экономика is a book. This bbok текст текс наа сша.ю";
        text = "There экономика is a сшА. Сша  текст попалаа сша.ю";
        int indexStart = text.toLowerCase().indexOf(originalWord.toLowerCase());
        int indexEnd = indexStart + originalWord.toLowerCase().length();

        if (indexStart == -1 || indexEnd == -1) {
            fillDefaultSnippet();
            return;
        }

        if (indexStart < ADDON_OF_SNIPPET_LENGTH) {
            addonBefore = text.substring(0, indexStart);
            if (text.length() - indexEnd >= ADDON_OF_SNIPPET_LENGTH + (ADDON_OF_SNIPPET_LENGTH - indexStart)) {
                addonAfter = text.substring(indexEnd, indexEnd + ADDON_OF_SNIPPET_LENGTH +  (ADDON_OF_SNIPPET_LENGTH - indexStart));
            } else {
                addonAfter = text.substring(indexEnd, text.length());
            }
        } else if (indexEnd + ADDON_OF_SNIPPET_LENGTH > text.length()) {
            if (ADDON_OF_SNIPPET_LENGTH + (ADDON_OF_SNIPPET_LENGTH - (text.length() - indexEnd)) <= indexStart) {
                addonBefore = text.substring(indexStart - (ADDON_OF_SNIPPET_LENGTH + (ADDON_OF_SNIPPET_LENGTH - (text.length() - indexEnd))), indexStart);
            } else {
                addonBefore = text.substring(0, indexStart);
            }
            addonAfter = text.substring(indexEnd, text.length());
        } else {
            addonBefore = text.substring(indexStart - ADDON_OF_SNIPPET_LENGTH, indexStart);
            addonAfter = text.substring(indexEnd, indexEnd + ADDON_OF_SNIPPET_LENGTH);
        }
        boldingAddons();
        fillSnippet();
    }

    public void fillDefaultSnippet(){
        maxFrequencyLemmaSnippet = NO_SNIPPET;
    }

    public String convertHtml2text (String html){
        return Jsoup.parse(html).text();
    }

    private void boldingAddons(){
        pageEnriched.getLemmaEnrichedSet().forEach(lemmaEnriched -> {
            String searchWord = lemmaEnriched.getBasicLemma().getOriginalWord();
            addonBefore = boldingString(addonBefore, searchWord);
            addonAfter = boldingString(addonAfter, searchWord);
        });

    }

    private String boldingString(String string, String searchWord){
        int indexStart = string.toLowerCase().indexOf(searchWord.toLowerCase());
        if (indexStart != -1) {
            int indexEnd = indexStart + searchWord.length();
            string = new StringBuilder(string).insert(indexStart, BOLDING_START_SYMBOL).toString();
            string = new StringBuilder(string).insert(indexEnd + BOLDING_START_SYMBOL.length(), BOLDING_END_SYMBOL).toString();
        }
        return string;
    }

    private void fillSnippet() {
        maxFrequencyLemmaSnippet = new StringBuilder(addonBefore)
                .append(BOLDING_START_SYMBOL)
                .append(maxFrquenceLemmaEnriched.getBasicLemma().getOriginalWord())
                .append(BOLDING_END_SYMBOL)
                .append(addonAfter)
                .toString();
    }
}
