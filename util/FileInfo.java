package util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * The {@code FileInfo} class provides information about a file,
 * including creation date, creation time, last modified date,
 * last modified time, and the owner of the file.
 */
public class FileInfo {
    private Path filePath;
    private LocalDate creationDate;
    private LocalTime creationTime;
    private LocalDate lastModifiedDate;
    private LocalTime lastModifiedTime;
    private String owner;

    /**
     * Creates a {@code FileInfo} object for the specified file.
     *
     * @param filePath The path to the file.
     * @throws IOException If the file cannot be read or the path is invalid.
     */
    public FileInfo(String filePath) {
        this.filePath = Paths.get(filePath);
        setFileAttributes();
    }

    /**
     * Reads the file attributes and sets the corresponding fields.
     *
     */
    private void setFileAttributes() {
        BasicFileAttributes attrs;
        try {
            attrs = Files.readAttributes(this.filePath, BasicFileAttributes.class);
            // Determine creation date and time
            LocalDateTime creationDateTime = LocalDateTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault());
            this.creationDate = creationDateTime.toLocalDate();
            this.creationTime = creationDateTime.toLocalTime();

            // Determine last modified date and time
            LocalDateTime lastModifiedDateTime = LocalDateTime.ofInstant(attrs.lastModifiedTime().toInstant(), ZoneId.systemDefault());
            this.lastModifiedDate = lastModifiedDateTime.toLocalDate();
            this.lastModifiedTime = lastModifiedDateTime.toLocalTime();

            // Determine the owner of the file
            UserPrincipal ownerPrincipal = Files.getOwner(filePath);
            this.owner = ownerPrincipal.getName();
        } catch (IOException e) {
            this.creationDate = null;
            this.creationTime = null;
            this.lastModifiedDate = null;
            this.lastModifiedTime = null;
            this.owner = "Unknown";
        }
    }

    /**
     * Returns the creation date of the file.
     *
     * @return The creation date as {@code LocalDate}.
     */
    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    /**
     * Returns the creation time of the file.
     *
     * @return The creation time as {@code LocalTime}.
     */
    public LocalTime getCreationTime() {
        return this.creationTime;
    }

    /**
     * Returns the last modified date of the file.
     *
     * @return The last modified date as {@code LocalDate}.
     */
    public LocalDate getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    /**
     * Returns the last modified time of the file.
     *
     * @return The last modified time as {@code LocalTime}.
     */
    public LocalTime getLastModifiedTime() {
        return this.lastModifiedTime;
    }

    /**
     * Returns the owner of the file.
     *
     * @return The owner of the file as a {@code String}.
     */
    public String getOwner() {
        return this.owner;
    }

    /**
     * Returns a string representation of the {@code FileInfo} object,
     * including the file path, creation date, creation time, last modified date,
     * last modified time, and owner.
     *
     * @return A formatted string with the file attributes.
     */
    @Override
    public String toString() {
        return "{\n" +
               "  \"filePath\": \"" + this.filePath + "\",\n" +
               "  \"creationDate\": \"" + this.creationDate + "\",\n" +
               "  \"creationTime\": \"" + this.creationTime + "\",\n" +
               "  \"lastModifiedDate\": \"" + this.lastModifiedDate + "\",\n" +
               "  \"lastModifiedTime\": \"" + this.lastModifiedTime + "\",\n" +
               "  \"owner\": \"" + this.owner + "\"\n" +
               "}";
    }
    
}
