package com.conferenceengineer.server.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class TagUtils {
    private static final List<String> EMPTY_LIST = new ArrayList<>();

    private TagUtils() {
    }

    public List<String> extractTags(final String phrase, final int maximumNumberOfTags) {
        if(phrase == null || phrase.isEmpty()) {
            return EMPTY_LIST;
        }

        Map<String, TagCounter> tagCounts = getTagCounts(phrase);
        return extractBestTags(tagCounts, maximumNumberOfTags);
    }

    private Map<String, TagCounter> getTagCounts(final String phrase) {
        String simplifiedString = removeAllPunctuation(phrase);

        Map<String, TagCounter> tagMap = new HashMap<>();
        StringTokenizer stringTokenizer = new StringTokenizer(simplifiedString);
        while(stringTokenizer.hasMoreElements()) {
            String thisTag = stringTokenizer.nextToken();
            if(shouldIgnore(thisTag)) {
                continue;
            }
            thisTag = ensureTagIsCorrectlyCapitalised(thisTag);
            TagCounter currentCount = tagMap.get(thisTag);
            if(currentCount == null) {
                tagMap.put(thisTag, new TagCounter(thisTag));
            } else {
                currentCount.mCount++;
            }
        }
        return tagMap;
    }

    private String removeAllPunctuation(final String input) {
        StringBuilder builder = new StringBuilder(input.length());
        for(char c : input.toCharArray()) {
            if(Character.isLetter(c) || Character.isSpaceChar(c)) {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    private String ensureTagIsCorrectlyCapitalised(final String string) {
        if(string == null || string.isEmpty()) {
            return string;
        }
        StringBuilder result = new StringBuilder(string.length());
        result.append(Character.toUpperCase(string.charAt(0)));
        for(int i = 0 ; i < string.length() ; i++) {
            result.append(Character.toLowerCase(string.charAt(i)));
        }
        return result.toString();
    }

    private boolean shouldIgnore(String tag) {
        return tag.length() < 5 ||  tag.toLowerCase().startsWith("what");
    }


    private List<String> extractBestTags(final Map<String, TagCounter> tagCounts, final int maximumNumberOfTags) {
        List<TagCounter> tagList = new ArrayList<>(tagCounts.values());
        int tagsToExport = Math.min(maximumNumberOfTags, tagList.size());

        List<String> popularTags = new ArrayList<>(tagsToExport);

        Collections.sort(tagList);
        for(int i = 0 ; i < tagsToExport ; i++) {
            TagCounter thisCounter = tagList.get(i);
            popularTags.add(thisCounter.mTag);
        }

        return popularTags;
    }

    private static class TagCounter implements Comparable<TagCounter> {
        String mTag;
        int mCount = 1;

        TagCounter(String tag) {
            mTag  = tag;
        }

        @Override
        public int compareTo(TagCounter o) {
            int countComparison = o.mCount - mCount;
            if(countComparison != 0) {
                return countComparison;
            }

            return o.mTag.length() - mTag.length();
        }

        @Override
        public boolean equals(Object other) {
            if(!(other instanceof TagCounter)) {
                return false;
            }
            return compareTo((TagCounter)other) == 0;
        }

        @Override
        public int hashCode() {
            return mTag.hashCode();
        }
    }


    private static final class InstanceHolder {
        static final TagUtils INSTANCE = new TagUtils();
    }

    public static TagUtils getInstance() {
        return InstanceHolder.INSTANCE;
    }
}
