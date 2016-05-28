package pl.pawkrol.academic.ftp.client.filesystem;

import javafx.scene.control.TreeItem;
import pl.pawkrol.academic.ftp.client.TreeListItem;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pawkrol on 5/15/16.
 */
public class LocalFilesystem {

    private Path workingDirectory;
    private String selectedFile = "";

    public LocalFilesystem(Path workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public Path getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(Path workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory){
        this.workingDirectory = Paths.get(workingDirectory).toAbsolutePath();
    }

    public String getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(String selectedFile) {
        this.selectedFile = selectedFile;
    }

    public boolean createDirectory(String filename){
        String path = workingDirectory
                + (workingDirectory.endsWith("/") ? "" : "/") + filename;
        File file = new File(path);
        return file.mkdir();
    }
}
