package com.conferenceengineer.server.exporters.iosched13;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.Talk;
import com.conferenceengineer.server.datamodel.Track;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Exporter for talk slots in the JSON format iosched wants
 */
public final class SearchSuggestionsJSON {

    private SearchSuggestionsJSON() {
        super();
    }

    /**
     * Create the export and place it in a string.
     */

    public static String export(final Conference conference) {
        Set<String> words = new HashSet<String>();
        for(Track track : conference.getTrackList()) {
            for(Talk talk : track.getTalkList()) {
                addWordSuggestions(words, talk.getName());
                addWordSuggestions(words, talk.getShortDescription());
            }
        }

        List<String> sortedWords = new ArrayList<String>();
        sortedWords.addAll(words);
        Collections.sort(sortedWords);

        JSONArray wordArray = new JSONArray();
        for(String word : sortedWords) {
            wordArray.put(word);
        }

        JSONObject root = new JSONObject();
        root.put("words", wordArray);
        return root.toString();
    }

    /**
     * Add any words over 4 characters to the word suggestion list
     *
     * @param words The Set of words.
     * @param string The string to look at.
     */

    private static void addWordSuggestions(final Set<String> words, final String string) {
        if(string == null) {
            return;
        }

        StringTokenizer tok = new StringTokenizer(string);
        while(tok.hasMoreTokens()) {
            String thisToken = tok.nextToken().trim();

            StringBuilder builder = new StringBuilder();
            for(char c:thisToken.toCharArray()) {
                if(Character.isLetter(c)) {
                    builder.append(c);
                }
            }

            String candidateWord = builder.toString();

            if(candidateWord.length() > 4) {
                words.add(candidateWord.toLowerCase());
                continue;
            }

            boolean isAllCaps = false;
            for(char c : candidateWord.toCharArray()) {
                if(Character.isLowerCase(c)) {
                    isAllCaps = false;
                    break;
                }
            }

            if(isAllCaps) {
                words.add(candidateWord.toLowerCase());
            }
        }
    }

}
