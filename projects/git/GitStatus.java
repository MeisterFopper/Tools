package projects.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GitStatus {

    // Constants for  pre-commit file states
    public static final String STATUS_ADDED = "Added";
    public static final String STATUS_CHANGED = "Changed";
    public static final String STATUS_REMOVED = "Removed";
    public static final String STATUS_UNTRACKED = "Untracked";
    public static final String STATUS_MODIFIED = "Modified";
    public static final String STATUS_CONFLICTING = "Conflicting";

    // Constants for pre-commit actions
    public static final String ACTION_COMMIT = "Commit";
    public static final String ACTION_ADD = "Add";
    public static final String ACTION_UNSTAGE = "Unstage";
    public static final String ACTION_IGNORE = "Ignore";
    public static final String ACTION_RESET = "Reset";
    public static final String ACTION_RESTORE = "Restore";
    public static final String ACTION_RESOLVE = "Resolve Conflicts";
    public static final String ACTION_UNDO = "Undo Merge";

    // Constants for post-commit actions
    public static final String ACTION_PUSH = "Push";
    public static final String ACTION_TAG = "Tag";
    public static final String ACTION_REVERT = "Revert";
    public static final String ACTION_CHERRY_PICK = "Cherry Pick";

    private final Git git;
    private final File repoDir;

    public GitStatus(File repoDir) throws IOException {
        this.repoDir = repoDir;
        this.git = Git.open(repoDir);
    }

    /**
     * Retrieves the available actions based on the given file status.
     * 
     * This method maps specific file statuses to their corresponding actions 
     * and returns a list of actions that can be performed on a file with the 
     * specified status. If the file status is not recognized, an empty list 
     * will be returned.
     *
     * @param fileStatus The status of the file (e.g., "Added", "Changed", 
     *                   "Removed", "Untracked", "Modified", "Conflicting").
     * @return A list of actions available for the specified file status. 
     *         Possible actions include commit, unstage, reset, restore, 
     *         add, ignore, resolve conflicts, and undo.
     */
    public static List<String> getAvailableActions(String fileStatus) {
        Map<String, List<String>> actionMap = createActionMap();
        return actionMap.getOrDefault(fileStatus, Collections.emptyList());
    }

    /**
     * Retrieves all available actions for each file state.
     * 
     * This method returns a map where each key is a file state and its value 
     * is the list of actions that can be performed for that state.
     *
     * @return A map of all file states and their corresponding actions.
     */
    public static Map<String, List<String>> getAllAvailableActions() {
        return createActionMap();
    }

    /**
     * The status of files in the Git repository and returns a map of categorized files.
     *
     * @param status The status object containing information about the files in the repository.
     * @return A map where the key is the category of files (e.g., "Added Files") and the value is a list of file names.
     */
    public static Map<String, Set<String>> statusToMap(Status status) {
        Map<String, Set<String>> statusMap = new HashMap<>();

        // Collect added files
        Set<String> addedFiles = status.getAdded();
        statusMap.put(STATUS_ADDED, addedFiles);

        // Collect changed files
        Set<String> changedFiles = status.getChanged();
        statusMap.put(STATUS_CHANGED, changedFiles);

        // Collect removed files
        Set<String> removedFiles = status.getRemoved();
        statusMap.put(STATUS_REMOVED, removedFiles);

        // Collect untracked files
        Set<String> untrackedFiles = status.getUntracked();
        statusMap.put(STATUS_UNTRACKED, untrackedFiles);

        // Collect modified files
        Set<String> modifiedFiles = status.getModified();
        statusMap.put(STATUS_MODIFIED, modifiedFiles);

        // Collect conflicting files
        Set<String> conflictingFiles = status.getConflicting();
        statusMap.put(STATUS_CONFLICTING, conflictingFiles);

        return statusMap;
    }

    /**
     * Adds a file to the staging area.
     *
     * @param fileToAdd the file to be added to the staging area
     * @throws GitAPIException if there is an error adding the file
     */
    public void addFile(File fileToAdd) throws GitAPIException {
        String relativePath = getRelativePath(fileToAdd);
        git.add().addFilepattern(relativePath).call();
    }

    /**
     * Removes a file from the staging area.
     *
     * @param fileToUnstage the file to be removed from the staging area
     * @throws GitAPIException if there is an error removing the file
     */
    public void unstageFile(File fileToUnstage) throws GitAPIException {
        String relativePath = getRelativePath(fileToUnstage);
        git.reset().addPath(relativePath).call();
    }

    /**
     * Commits the staged changes with a commit message.
     *
     * @param commitMessage the commit message
     * @throws GitAPIException if there is an error during the commit
     */
    public void commit(String commitMessage) throws GitAPIException {
        git.commit().setMessage(commitMessage).call();
    }

    /**
     * Ignores a file by adding it to the .gitignore file.
     *
     * @param fileToIgnore the file to be ignored
     * @throws IOException if there is an error while updating .gitignore
     */
    public void ignoreFile(File fileToIgnore) throws IOException {
        File gitIgnoreFile = new File(repoDir, ".gitignore");
        if (!gitIgnoreFile.exists() && gitIgnoreFile.createNewFile()) {
            throw new IOException("Error while creating new .gitignore file");
        } 
        
        GitIgnore ignoreHelper = new GitIgnore(gitIgnoreFile);
        ignoreHelper.loadGitIgnore();
        ignoreHelper.addPattern(fileToIgnore.toString(), "");
        ignoreHelper.saveGitIgnore();
    }

    /**
     * Resets the specified file to the last committed state.
     *
     * @param fileToReset the file to be reset
     * @throws GitAPIException if there is an error during the reset
     */
    public void resetFile(File fileToReset) throws GitAPIException {
        String relativePath = getRelativePath(fileToReset);
        git.checkout().addPath(relativePath).call();
    }

    /**
     * Restores a removed file from the last commit.
     *
     * @param fileToRestore the file to be restored
     * @throws GitAPIException if there is an error during the restore
     */
    public void restoreFile(File fileToRestore) throws GitAPIException {
        String relativePath = getRelativePath(fileToRestore);
        git.checkout().addPath(relativePath).call();
    }

    /**
     * Resolves conflicts in the specified files manually. This requires manual intervention.
     *
     * @param files the files with conflicts
     * @throws GitAPIException if there is an error while resolving conflicts
     */
    public void resolveConflicts(Set<File> files) throws GitAPIException {
        for (File file : files) {
            // Here you can add logic to handle the resolution.
            // For now, we simply add the files back to staging.
            String relativePath = getRelativePath(file);
            git.add().addFilepattern(relativePath).call();
        }
    }

    /**
     * Undoes the last merge action.
     *
     * @throws GitAPIException if there is an error while undoing the merge
     */
    public void undoMerge() throws GitAPIException {
        git.reset().setMode(org.eclipse.jgit.api.ResetCommand.ResetType.HARD).call();
    }

    /**
     * Returns the name of the current branch.
     *
     * @return the name of the current branch
     * @throws IOException if there is an error while getting the current branch
     */
    public String getCurrentBranch() throws IOException {
        return git.getRepository().getBranch();
    }

    /**
     * Creates a mapping of file states to their corresponding actions.
     * 
     * This method is used internally to construct the action map for 
     * both available actions and all available actions.
     * 
     * @return A map of file states and their corresponding actions.
     */
    private static Map<String, List<String>> createActionMap() {
        Map<String, List<String>> actionMap = new HashMap<>();
        actionMap.put(STATUS_ADDED, Arrays.asList(ACTION_COMMIT, ACTION_UNSTAGE));
        actionMap.put(STATUS_CHANGED, Arrays.asList(ACTION_COMMIT, ACTION_UNSTAGE, ACTION_RESET));
        actionMap.put(STATUS_REMOVED, Arrays.asList(ACTION_COMMIT, ACTION_UNSTAGE, ACTION_RESTORE));
        actionMap.put(STATUS_UNTRACKED, Arrays.asList(ACTION_ADD, ACTION_IGNORE));
        actionMap.put(STATUS_MODIFIED, Arrays.asList(ACTION_ADD, ACTION_RESET));
        actionMap.put(STATUS_CONFLICTING, Arrays.asList(ACTION_RESOLVE, ACTION_UNDO));
        return actionMap;
    }

    /**
     * Helper method to get the relative path of a file with respect to the repository directory.
     * 
     * @param file the file for which the relative path is needed
     * @return the relative path as a String
     */
    private String getRelativePath(File file) {
        return repoDir.toPath().relativize(file.toPath()).toString().replace("\\", "/");
    }
}
