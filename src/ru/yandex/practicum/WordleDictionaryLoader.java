package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    public WordleDictionary loadDictionary(String fileName) throws IOException {
        WordleDictionary tempWords = new WordleDictionary();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName), StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank() && (line.trim().length() == 5)) {// Здесь обрабатываем каждую строку
                    tempWords.add(line.toLowerCase().replace('ё', 'е'));
                }
            }
        } catch (IOException e) {
            // Обработка ошибки, если файл не найден или нет доступа
            System.out.println("Ошибка при чтении словаря " + e.getMessage());
        }
        return tempWords;
    }
}
