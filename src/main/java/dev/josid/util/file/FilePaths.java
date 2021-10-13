package dev.josid.util.file;

import java.nio.file.Path;

public class FilePaths {

    public final Path sourcePath;
    public final Path targetPath;

    public FilePaths(Path sourcePath, Path targetPath) {
        this.sourcePath = sourcePath;
        this.targetPath = targetPath;
    }

}
