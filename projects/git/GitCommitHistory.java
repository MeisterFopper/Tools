package projects.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GitCommitHistory {

    private final Git git;

    public GitCommitHistory(File repoDir) throws IOException {
        this.git = Git.open(repoDir);
    }

    /**
     * Returns the content of the blob as a byte array for the given ObjectId.
     *
     * @param repository The repository containing the blob.
     * @param objectId The ObjectId of the blob.
     * @return The content of the blob as a byte array.
     * @throws IOException if an error occurs while reading the blob content.
     */
    public byte[] getBlobFileContentAsBytes(Repository repository, ObjectId objectId) throws IOException {
        ObjectReader reader = repository.newObjectReader();
        try {
            ObjectLoader loader = reader.open(objectId);
            return loader.getBytes();
        } catch (MissingObjectException e) {
            throw new IOException("Blob not found for ObjectId: " + objectId.getName(), e);
        } finally {
            reader.close();
        }
    }

    /**
     * Returns a list of CommitInfo objects for the last few commits.
     *
     * @param commitCount The number of recent commits to display.
     * @return A list of CommitInfo objects containing commit details.
     * @throws GitAPIException 
     * @throws NoHeadException 
     * @throws IOException 
     * @throws CorruptObjectException 
     * @throws IncorrectObjectTypeException 
     * @throws MissingObjectException 
     * @throws Exception if there is an error accessing the repository.
     */
    public List<GitCommitInfo> getChangedFilesInCommits(int commitCount) throws GitAPIException, IOException {
        List<GitCommitInfo> commitInfos = new ArrayList<>();
        Repository repository = git.getRepository();

        try (RevWalk revWalk = new RevWalk(repository)) {
            Iterable<RevCommit> commits = git.log().setMaxCount(commitCount).call();

            for (RevCommit commit : commits) {
                List<GitChangedFile> changedFiles = new ArrayList<>();

                try (TreeWalk treeWalk = new TreeWalk(repository)) {
                    treeWalk.addTree(commit.getTree());
                    treeWalk.setRecursive(true);

                    while (treeWalk.next()) {
                        // Informationen über die Datei
                        String pathString = treeWalk.getPathString();
                        FileMode mode = treeWalk.getFileMode();
                        ObjectId objectId = treeWalk.getObjectId(0); // Hier wird der BlobID abgerufen

                        // Optional: Prüfe den Typ der Datei
                        if (mode.equals(FileMode.REGULAR_FILE)) {
                            changedFiles.add(new GitChangedFile(objectId, pathString));
                        }
                    }
                }

                // Erstelle ein CommitInfo-Objekt
                commitInfos.add(new GitCommitInfo(
                        commit.getId().getName(),
                        commit.getFullMessage(),
                        commit.getAuthorIdent().getName(),
                        commit.getAuthorIdent().getWhen().toString(),
                        changedFiles
                ));
            }
        }
        return commitInfos;
    }
}
