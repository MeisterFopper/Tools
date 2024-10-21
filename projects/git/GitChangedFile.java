package projects.git;

import org.eclipse.jgit.lib.ObjectId;

public class GitChangedFile {
    private final ObjectId objectId;
    private final String fileName;

    public GitChangedFile(ObjectId objectId, String fileName) {
        this.objectId = objectId;
        this.fileName = fileName;
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return "ChangedFile{" +
                "objectId='" + objectId.getName() + '\'' +
                ", fileName=" + fileName +
                '}';
    }
}
