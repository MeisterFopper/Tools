package projects.git;

import org.eclipse.jgit.api.Status;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GitStatusActionMapper {

    // Constants for pre-commit file states
    public static final String STATUS_ADDED = "Added";
    public static final String STATUS_CHANGED = "Changed";
    public static final String STATUS_REMOVED = "Removed";
    public static final String STATUS_UNTRACKED = "Untracked";
    public static final String STATUS_MODIFIED = "Modified";
    public static final String STATUS_CONFLICTING = "Conflicting";

    // Constants for pre-commit file actions
    public static final String ACTION_COMMIT = "Commit";
    public static final String ACTION_ADD = "Add";
    public static final String ACTION_UNSTAGE = "Unstage";
    public static final String ACTION_IGNORE = "Ignore";
    public static final String ACTION_RESET = "Reset";
    public static final String ACTION_RESTORE = "Restore";
    public static final String ACTION_RESOLVE = "Resolve Conflicts";
    public static final String ACTION_UNDO = "Undo Merge";

    // Constants for post-commit states
    public static final String STATUS_COMMIT_LOCAL = "Commit Local";
    public static final String STATUS_COMMIT_REMOTE = "Commit Remote";

    // Constants for post-commit actions
    public static final String ACTION_PUSH = "Push";
    public static final String ACTION_TAG = "Tag";
    public static final String ACTION_REVERT = "Revert";
    public static final String ACTION_CHERRY_PICK = "Cherry Pick";

    /**
     * Retrieves the available actions based on the given file status.
     *
     * This method maps specific file statuses to their corresponding actions
     * and returns a list of actions that can be performed on a file with the
     * specified status. If the file status is not recognized, an empty list
     * will be returned.
     *
     * @param status The status of the file or commit (e.g., "Added", "Changed",
     *               "Removed", "Untracked", "Modified", "Conflicting", 
     *               "Commit Local", "Commit Remote").
     * @return A list of actions available for the specified status.
     */
    public static List<String> getActions(String status) {
        Map<String, List<String>> actionMap = new HashMap<>();
        actionMap.putAll(allPreCommitActions()); // Add all pre-commit actions
        actionMap.putAll(allPostCommitActions()); // Add all post-commit actions
        return actionMap.getOrDefault(status, Collections.emptyList());
    }

    /**
     * The status of files in the Git repository and returns a map of categorized files.
     *
     * @param status The status object containing information about the files in the repository.
     * @return A map where the key is the category of files (e.g., "Added Files") and the value is a list of file names.
     */
    public static Map<String, Set<String>> statusMap(Status status) {
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
     * Creates a mapping of file states to their corresponding actions.
     * 
     * This method is used internally to construct the action map for 
     * both available actions and all available actions.
     * 
     * @return A map of file states and their corresponding actions.
     */
    public static Map<String, List<String>> allPreCommitActions() {
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
     * Creates a mapping of post-commit statuses to their corresponding actions.
     * 
     * This method is used to separate the post-commit action mapping from 
     * the general action mapping.
     *
     * @return A map of post-commit states and their corresponding actions.
     */
    public static Map<String, List<String>> allPostCommitActions() {
        Map<String, List<String>> postCommitActionMap = new HashMap<>();
        postCommitActionMap.put(STATUS_COMMIT_LOCAL, Arrays.asList(ACTION_PUSH, ACTION_TAG, ACTION_REVERT, ACTION_CHERRY_PICK));
        postCommitActionMap.put(STATUS_COMMIT_REMOTE, Arrays.asList(ACTION_TAG, ACTION_REVERT, ACTION_CHERRY_PICK));
        return postCommitActionMap;
    }
}
