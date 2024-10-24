package projects.git;

import java.util.List;

public class GitCommitInfo {
    private final String commitId;
    private final String message;
    private final String author;
    private final String date;
    private final List<GitCommitFile> changedFiles;

    public GitCommitInfo(String commitId, String message, String author, String date, List<GitCommitFile> changedFiles) {
        this.commitId = commitId;
        this.message = message;
        this.author = author;
        this.date = date;
        this.changedFiles = changedFiles;
    }

    // Getter-Methoden für die Felder

    public String getCommitId() {
        return commitId;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public List<GitCommitFile> getChangedFiles() {
        return changedFiles;
    }

    @Override
    public String toString() {
        return "CommitInfo{" +
                "commitId='" + commitId + '\'' +
                ", message='" + message + '\'' +
                ", author='" + author + '\'' +
                ", date='" + date + '\'' +
                ", changedFiles=" + changedFiles +
                '}';
    }
}
