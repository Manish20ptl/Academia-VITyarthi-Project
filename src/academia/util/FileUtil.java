package academia.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Handles everything that touches the filesystem: reading/writing CSV
 * files under data/, and creating timestamped backups.
 */
public class FileUtil {

    public static final Path DATA_DIR = Paths.get("data");

    public static void ensureDataDir() {
        try {
            if (!Files.exists(DATA_DIR)) {
                Files.createDirectories(DATA_DIR);
            }
        } catch (IOException e) {
            System.err.println("Could not create data directory: " + e.getMessage());
        }
    }

    /** Reads all lines from a data file, skipping the header row. Returns empty list if file is missing. */
    public static List<String> readLines(String fileName) {
        Path path = DATA_DIR.resolve(fileName);
        if (!Files.exists(path)) {
            return List.of();
        }
        try (Stream<String> lines = Files.lines(path)) {
            return lines.skip(1).filter(l -> !l.isBlank()).collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error reading " + fileName + ": " + e.getMessage());
            return List.of();
        }
    }

    /** Overwrites a data file with a header line followed by one CSV line per record. */
    public static void writeLines(String fileName, String header, List<String> records) {
        ensureDataDir();
        Path path = DATA_DIR.resolve(fileName);
        try (var writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write(header);
            writer.newLine();
            for (String record : records) {
                writer.write(record);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing " + fileName + ": " + e.getMessage());
        }
    }

    /** Copies every file currently in data/ into a new timestamped backup folder. Returns the backup folder path. */
    public static Path backupAll() {
        ensureDataDir();
        String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path backupDir = DATA_DIR.resolve("backup_" + stamp);
        try {
            Files.createDirectories(backupDir);
            try (Stream<Path> files = Files.list(DATA_DIR)) {
                for (Path file : files.filter(Files::isRegularFile).collect(Collectors.toList())) {
                    Files.copy(file, backupDir.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (IOException e) {
            System.err.println("Backup failed: " + e.getMessage());
        }
        return backupDir;
    }
}
