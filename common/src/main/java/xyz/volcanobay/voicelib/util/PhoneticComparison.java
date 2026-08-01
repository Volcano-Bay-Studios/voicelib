package xyz.volcanobay.voicelib.util;

import org.apache.commons.codec.language.DoubleMetaphone;

import java.util.*;

public class PhoneticComparison {
    private static final DoubleMetaphone doubleMetaphone = new DoubleMetaphone();

    public static double calculate(String word1, String word2) {
        if (word1 == null || word2 == null) return 0.0;
        if (word1.equalsIgnoreCase(word2)) return 1.0;
        doubleMetaphone.setMaxCodeLen(8);

        String firstPrimary = doubleMetaphone.doubleMetaphone(word1);
        String firstAlternate = doubleMetaphone.doubleMetaphone(word1, true);

        String secondPrimary = doubleMetaphone.doubleMetaphone(word2);
        String secondAlternate = doubleMetaphone.doubleMetaphone(word2, true);

        int d1 = levenshtein(firstPrimary, secondPrimary);
        int d2 = levenshtein(firstPrimary, secondAlternate);
        int d3 = levenshtein(firstAlternate, secondPrimary);
        int d4 = levenshtein(firstAlternate, secondAlternate);

        int minDistance = Math.min(Math.min(d1, d2), Math.min(d3, d4));

        int maxLength = Math.max( Math.max(firstPrimary.length(), firstAlternate.length()),  Math.max(secondPrimary.length(), secondAlternate.length()) );

        if (maxLength == 0) return 0.0;
        return 1.0 - ((double) minDistance / maxLength);

    }

    private static int levenshtein(String t1, String t2) {
        if (t1 == null || t2 == null) {
            return Integer.MAX_VALUE;
        }
        int[] costs = new int[t2.length() + 1];
        for (int i = 0; i <= t1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= t2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else if (j > 0) {
                    int newValue = costs[j - 1];
                    if (t1.charAt(i - 1) != t2.charAt(j - 1)) {
                        newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                    }
                    costs[j - 1] = lastValue;
                    lastValue = newValue;
                }
            }
            if (i > 0) costs[t2.length()] = lastValue;
        }
        return costs[t2.length()];
    }

    public static BestFit bestFitWord(String t1, String t2) {
        String[] words = t1.split(" ");
        int size = words.length;
        double best = 999999999;
        int index = 0;
        for (int i = 0; i < size; i++) {
            String text = words[i];
            double distance = calculate(text, t2);
            if (distance < best) {
                best = distance;
                index = i;
            }
        }
        String[] next = new String[size - index];
        System.arraycopy(words, index, next, 0, size - index);
        return new BestFit(words[index], best, next);
    }

    public static BestFit bestFitWord(List<String> words, String t2) {
        int size = words.size();
        double best = 999999999;
        int index = 0;
        for (int i = 0; i < size; i++) {
            String text = words.get(i);
            double distance = calculate(text, t2);
            if (distance < best) {
                best = distance;
                index = i;
            }
        }
        String[] next = new String[size - index];
        System.arraycopy(words.toArray(), index, next, 0, size - index);
        return new BestFit(words.get(index), best, next);
    }

    public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    public static double compareSentences(String sent1, String sent2) {
        Set<String> tokens1 = tokenize(sent1);
        Set<String> tokens2 = tokenize(sent2);

        if (tokens1.isEmpty() || tokens2.isEmpty()) return 0.0;

        int matches = 0;
        Set<String> TrackedTokens2 = new HashSet<>(tokens2);

        for (String t1 : tokens1) {
            for (String t2 : TrackedTokens2) {
                if (calculate(t1, t2) >= 0.75) {
                    matches++;
                    TrackedTokens2.remove(t2);
                    break;
                }
            }
        }

        int unionSize = tokens1.size() + tokens2.size() - matches;
        return (double) matches / unionSize;
    }

    private static Set<String> tokenize(String sentence) {
        if (sentence == null) return Collections.emptySet();
        String[] words = sentence.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", "").split("\\s+");
        return new HashSet<>(Arrays.asList(words));
    }

    public static int min(int... numbers) {
        return Arrays.stream(numbers)
                .min().orElse(Integer.MAX_VALUE);
    }

    public record BestFit(String string, double distance, String[] words) {
    };
}
