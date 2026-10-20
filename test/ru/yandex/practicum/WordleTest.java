package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    WordleDictionaryLoader loader = new WordleDictionaryLoader();
    WordleDictionary dictionary = loader.loadDictionary("words_ru.txt");
    WordleGame game;
    int steps = 6;
    private final PrintWriter mockLogger = new PrintWriter(new OutputStreamWriter(System.out)) {
    };

    WordleTest() throws IOException {
    }

    @BeforeEach
    void prepareNewGame() throws IOException {
        this.game = new WordleGame(dictionary, mockLogger, steps);
    }

    @Test
    void testWrongUserWordLength() {
        String wrongWord = "аба";
        Exception exception = assertThrows(WordIsLessOrMoreThan5Letters.class, () -> {
            game.validateInput(wrongWord, dictionary);
        });//Написано с помощью ИИ (.class и лямбда выражение)
        assertEquals("Слово должно состоять ровно из 5 букв.", exception.getMessage());
    }

    @Test
    void testWordNotInDictionary() {
        String wrongWord = "песня";
        Exception exception = assertThrows(WordNotFoundInDictionaryException.class, () -> {
            game.validateInput(wrongWord, dictionary);
        });//Написано с помощью ИИ (.class и лямбда выражение)
        assertEquals("Слова '" + wrongWord + "' нет в нашем словаре. Попробуйте другое.",
                exception.getMessage());
    }

    @Test
    void testHintExcludeWrongLetter() {
        game.addWrongLetterForTest('а');
        assertFalse(game.getHint().contains("а"));
    }

    @Test
    void testHintIncludeRightLetter() {
        game.addRightLetterForTest(0, 'а');
        assertEquals('а', game.getHint().charAt(0));
    }

    @Test
    void testWithEmptyInput() throws WordNotFoundInDictionaryException {
        game.processUserInput("");
        assertEquals(6, game.getSteps());
        assertNotNull(game.getLastHint());
    }
}
