import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DeleteAllFilesInDirectory {
    public static void main(String[] args) {
        // Specify the directory path from which you want to delete files
        String directoryPath = "/path/to/directory";  // Replace with the actual directory path

        try {
            // Create a Path object for the directory
            Path directory = Paths.get(directoryPath);

            // Use DirectoryStream to list and delete files
            try (DirectoryStream<Path> files = Files.newDirectoryStream(directory)) {
                for (Path file : files) {
                    if (Files.isRegularFile(file)) {
                        Files.delete(file);
                        System.out.println("Deleted: " + file.getFileName());
                    }
                }
            }

            System.out.println("All files in the directory have been deleted.");
        } catch (IOException e) {
            System.err.println("Error deleting files: " + e.getMessage());
        }
    }
}
