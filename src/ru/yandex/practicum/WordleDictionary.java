package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private List<String> words = new ArrayList<>();
    private final Set<Character> wrongLetters = new HashSet<>();
    private final Map<Integer, Character> rightLetters = new LinkedHashMap<>();

    public List<String> getWords() {
        return words;
    }

    public void add(String newString) {
        this.words.add(newString);
    }

    public boolean contains(String newString) {
        if (words.contains(newString))
            return true;
        else
            return false;
    }
}
