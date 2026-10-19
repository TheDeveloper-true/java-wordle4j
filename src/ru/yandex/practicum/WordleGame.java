package ru.yandex.practicum;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private String answer;
    private final PrintWriter logFile;
    private final Set<Character> wrongLetters = new HashSet<>();
    private final Map<Integer, Character> rightLetters = new LinkedHashMap<>();
    private String lastHint;
    Random random = new Random();

    private int steps;

    private final WordleDictionary dictionary;

    public WordleGame(WordleDictionary dictionary, PrintWriter logFile, int steps) throws IOException {
        this.steps = steps;
        this.dictionary = dictionary;
        this.logFile = logFile;
        List<String> words = dictionary.getWords();
        logFile.println("Игра началась");
        setAnswer(words);
    }

    public void setAnswer(List<String> words) {
        if (!words.isEmpty()) {
            int randomIndex = random.nextInt(words.size());
            this.answer = words.get(randomIndex);
            logFile.println("Загадано слово " + this.answer);
        }
    }

    public boolean isMatch(String userAnswer) {
        this.steps--;
        return answer.equals(userAnswer);
    }

    public String getAnswer() {
        return answer;
    }

    public void validateInput(String userAnswer, WordleDictionary dictionary) throws WordNotFoundInDictionaryException,
            WordIsLessOrMoreThan5Letters {

        if (userAnswer.length() != 5) {
            throw new WordIsLessOrMoreThan5Letters("Слово должно состоять ровно из 5 букв.");
        }

        if (!dictionary.contains(userAnswer)) {
            throw new WordNotFoundInDictionaryException(
                    "Слова '" + userAnswer + "' нет в нашем словаре. Попробуйте другое.");
        }
    }

    public void addWrongLetterForTest(char wrongLetter) {
        wrongLetters.add(wrongLetter);
    }

    public void addRightLetterForTest(int position, char rightLetter) {
        rightLetters.put(position, rightLetter);
    }


    public void processUserInput(String userAnswer) throws WordNotFoundInDictionaryException,
            WordIsLessOrMoreThan5Letters{
        if (userAnswer.isBlank()) {
            getHint();
            return;// код будет работать и без этого if, но тест не пройдет
        }

        validateInput(userAnswer, dictionary);

        for (int i = 0; i <= 4; i++) {
            char c = userAnswer.charAt(i);
            if (c == answer.charAt(i)) {
                rightLetters.put(i, c);
            } else if (!answer.contains(String.valueOf(c))) {
                wrongLetters.add(c);
            }
        }
        System.out.println(countChars(userAnswer));
        logFile.println("Вывод проверки совпадения введенного слова с загаданным " + countChars(userAnswer));
    }

    public String countChars(String userAnswer) {
        // написано со значительной помощью ИИ (где-то в ранних уроках писали указывать на это)
        char[] resultChars = new char[5];
        Map<Character, Integer> secretCounts = new HashMap<>();
        for (char c : this.answer.toCharArray()) {
            secretCounts.put(c, secretCounts.getOrDefault(c, 0) + 1);
        }
        Map<Character, Integer> remainingCounts = new HashMap<>(secretCounts);

        for (int i = 0; i < 5; i++) {
            char c = userAnswer.charAt(i);
            if (c == this.answer.charAt(i)) {
                resultChars[i] = '+';
                rightLetters.put(i, c);
                remainingCounts.put(c, remainingCounts.get(c) - 1);
            } else {
                char ch = userAnswer.charAt(i);
                if (secretCounts.containsKey(ch) && remainingCounts.get(ch) > 0) {
                    resultChars[i] = '^';
                    remainingCounts.put(ch, remainingCounts.get(ch) - 1);
                } else {
                    resultChars[i] = '-';
                }
            }
        }
//        for (int i = 0; i < 5; i++) {
//            if (resultChars[i] != '?') continue;
//
//            char c = userAnswer.charAt(i);
//
//            if (secretCounts.containsKey(c) && remainingCounts.get(c) > 0) {
//                resultChars[i] = '^';
//                remainingCounts.put(c, remainingCounts.get(c) - 1);
//            } else {
//                resultChars[i] = '-';
//            }
//        }

        return new String(resultChars);
    }

    public String getHint() {
        List<String> hintWords = new ArrayList<>();
        for (String word : dictionary.getWords()) {
            boolean isValid = true;
            for (char c : wrongLetters) {
                if (word.indexOf(c) != (-1)) {
                    isValid = false;
                    break;
                }
            }
            for (Map.Entry<Integer, Character> entry : rightLetters.entrySet()) {
                if (word.charAt(entry.getKey()) != entry.getValue()) {
                    isValid = false;
                    break;
                }
            }
            if (isValid)
                hintWords.add(word);
        }

        if (!hintWords.isEmpty()) {
            lastHint = hintWords.get(random.nextInt(hintWords.size()));
            return lastHint;
        } else
            return null;
    }

    public int getSteps() {
        return steps;
    }

    public String getLastHint() {
        return lastHint;
    }
}
