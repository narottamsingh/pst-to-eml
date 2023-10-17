
package com.bce;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MoveFiles {
	public static void main(String[] args) {
		// Specify the source and destination directories
		Path sourceDirectory = Paths.get("/path/to/source/directory");
		Path destinationDirectory = Paths.get("/path/to/destination/directory");
		moveFileSourceToDest(sourceDirectory, destinationDirectory);
	}

	private static void moveFileSourceToDest(Path sourceDirectory, Path destinationDirectory) {
		List<Path> successfullyMovedFiles = new ArrayList<>();
		try {
			Files.walk(sourceDirectory).filter(Files::isRegularFile).forEach(file -> {
				try {
					Path destination = destinationDirectory.resolve(sourceDirectory.relativize(file));
					Files.createDirectories(destination.getParent());
					Files.move(file, destination, StandardCopyOption.REPLACE_EXISTING);
					successfullyMovedFiles.add(file); // Track successfully moved files
					System.out.println("Moved: " + file.getFileName());
				} catch (IOException e) {
					// Error occurred, log the error
					System.err.println("Error moving file: " + e.getMessage());
					// Roll back: Move successfully moved files back to the source directory
					rollbackMove(destinationDirectory, sourceDirectory, successfullyMovedFiles);
				}
			});

			System.out.println("All files moved successfully.");
		} catch (IOException e) {
			System.err.println("Error listing files: " + e.getMessage());
		}

	}

	public static void moveFileSourceToDest(Set<String> sourceList, Path sourceDirectory, Path destinationDirectory) {
		List<Path> successfullyMovedFiles = new ArrayList<>();
		try {
			sourceList.forEach(file -> {
				try {
					Path sourceFile = sourceDirectory.resolve(file);
					Path destination = destinationDirectory.resolve(sourceDirectory.relativize(sourceFile));
					Files.createDirectories(destination.getParent());
					Files.move(sourceFile, destination, StandardCopyOption.REPLACE_EXISTING);
					successfullyMovedFiles.add(sourceFile); // Track successfully moved files
					System.out.println("Moved: " + sourceFile.getFileName());
				} catch (IOException e) {
					System.err.println("Error moving file: " + e.getMessage());
				}
			});
			System.out.println("All files moved successfully.");
		} catch (Exception e) {
			System.err.println("Error listing files: " + e.getMessage());
			rollbackMove(destinationDirectory, sourceDirectory, successfullyMovedFiles);
		}

	}

	// Roll back: Move files from destination directory back to source directory
	private static void rollbackMove(Path sourceDirectory, Path destinationDirectory,
			List<Path> successfullyMovedFiles) {
		successfullyMovedFiles.forEach(file -> {
			try {
				Path source = sourceDirectory.resolve(destinationDirectory.relativize(file));
				Files.createDirectories(source.getParent());
				Files.move(file, source, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Rolled back: " + file.getFileName());
			} catch (IOException rollbackException) {
				System.err.println("Error rolling back file: " + rollbackException.getMessage());
			}
		});
	}
}
