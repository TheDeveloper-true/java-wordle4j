package ru.yandex.practicum;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    public static void main(String[] args) throws IOException {
        try (PrintWriter logFile = new PrintWriter(new File("log.txt"),
                StandardCharsets.UTF_8)) {
            WordleDictionaryLoader loader = new WordleDictionaryLoader();
            WordleDictionary dictionary = loader.loadDictionary("words_ru.txt");
            int steps = 6;
            WordleGame game = new WordleGame(dictionary, logFile, steps);
            Scanner scanner = new Scanner(System.in);
            System.out.println(game.getAnswer());
            while (true) {
                System.out.println("Если нужна подсказка нажмите Enter");
                System.out.println("Введите слово: ");
                String userAnswer = scanner.nextLine().trim().toLowerCase().replace('ё', 'е');
                try {
                    if (userAnswer.isBlank()) {
                        System.out.println("Подсказка " + game.getHint());
                        logFile.println("Получена подсказка " + game.getHint());
                        continue;
                    }
                    game.processUserInput(userAnswer);
                    if (game.isMatch(userAnswer)) {
                        System.out.println("Вы угадали слово");
                        break;
                    } else if (game.getSteps() == 0) {
                        System.out.println("Ходы закончились! Загаданное слово " + game.getAnswer());
                        break;
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    logFile.println("Ошибка ввода пользователя");
                }
            }
        } catch (IOException e) {
            System.out.println("Критическая ошибка " + e.getMessage());
        }
    }
}
