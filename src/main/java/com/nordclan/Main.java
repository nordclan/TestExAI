package com.nordclan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    private static final List<Integer> CACHE = new ArrayList<>();
    private static final Map<Integer, String> REPLACEMENTS = new HashMap<>();

    static {
        CACHE.add(1);
    }

    static {
        REPLACEMENTS.put(2, "two");
        REPLACEMENTS.put(7, "seven");
    }

    /**
     * Метод формирует строку, в которой записаны все числа из заданного диапазона
     * со следующими заменами: кратные 2 на "Two", кратные 7 на "Seven",
     * кратные 2 и 7 на "TwoSeven".
     *
     * @param limit ограничение диапазона справа
     * @return возвращает строку с заменами
     */
    private static String getNumbers(int limit) {
        if (limit < 2) {
            throw new IllegalArgumentException("Limit must be greater than or equal to 2");
        }
        return IntStream.range(1, limit + 1)
                .mapToObj((i) -> {
                    String s = REPLACEMENTS.keySet().stream()
                            .filter(j -> i % j == 0)
                            .map(REPLACEMENTS::get)
                            .collect(Collectors.joining());
                    if (s == null || s.isEmpty()) {
                        return String.valueOf(i);
                    }
                    return s;
                }).collect(Collectors.joining(" "));
    }

    /**
     * Метод рассчитывает количество сочетаний из m по r.
     *
     * @param m количество элементов
     * @param r количество элементов в одном сочетании
     * @return значение количества сочетаний
     */
    private static int combinations(int r, int m) {
        if (r < 0 || m < 0 || r > m) {
            throw new IllegalArgumentException("Params r and m must be positive and r <= m");
        }
        for (int i = CACHE.size(); i <= m + 1; i++) {
            CACHE.add(CACHE.get(i - 1) * i);
        }
        return CACHE.get(m) / CACHE.get(r) / CACHE.get(m - r);
    }

    /**
     * Метод рассчитывает частоту вхождения слов в заданную строку.
     *
     * @param text исходный литературный текст
     * @return упорядоченый по частоте набор количество слов - слово
     */
    private static Map<String, Long> wordCounter(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("text must not be null or empty");
        }
        Map<String, Long> words = Stream.of(text.toLowerCase()
                .replaceAll("[,\\.]", "").split("\\s"))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Map<String, Long> unsortedWords = words.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return unsortedWords.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o1, o2) -> o1, LinkedHashMap::new));
    }

    public static void main(String[] args) {
        System.out.println(getNumbers(100));
        System.out.println(combinations(3, 5));
        String text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when" +
                " an unknown printer took a galley of type and scrambled it to make a type specimen" +
                " book. It has survived not only five centuries, but also the leap into electronic " +
                "typesetting, remaining essentially unchanged. It was popularised in the 1960s with" +
                " the release of Letraset sheets containing Lorem Ipsum passages, and more recently" +
                " with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum";
        System.out.print(wordCounter(text));
    }
}
