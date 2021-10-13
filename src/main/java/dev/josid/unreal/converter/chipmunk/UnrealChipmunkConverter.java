package dev.josid.unreal.converter.chipmunk;

import dev.josid.util.file.FilePaths;
import dev.josid.util.file.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

public class UnrealChipmunkConverter {

    public static void main(String[] args) throws IOException {
        Path workingDirectory = Paths.get("").resolve("dist");

        String chipmunkHomeString = "F:\\dev\\wrk\\c\\Chipmunk2D";
        Path chipmunkHome = Paths.get(chipmunkHomeString);

        processHeaderFiles(chipmunkHome, workingDirectory);
        processSourceFiles(chipmunkHome, workingDirectory.resolve("chipmunk"));
    }

    private static void processHeaderFiles(Path chipmunkHome, Path targetDirectory) throws IOException {
        List<FilePaths> headerPaths = FileUtils.getFilesPathsRecursive( chipmunkHome.resolve("include"), targetDirectory,
                FileUtils.pathExtensionFilter("h")
                .and(path -> !path.getFileName().toString().contains("cpHastySpace")));

        headerPaths.forEach( filePaths -> {
            try {
                FileUtils.cloneFile(filePaths.sourcePath, filePaths.targetPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void processSourceFiles(Path chipmunkHome, Path targetDirectory) throws IOException {
        List<FilePaths> sourcePaths = FileUtils.getFilesPathsRecursive( chipmunkHome.resolve("src"), targetDirectory,
                FileUtils.pathExtensionFilter("h", "c")
                .and(path -> !path.getFileName().toString().contains("cpHastySpace")));

        sourcePaths.forEach( filePaths -> {
            try {
                FileUtils.cloneFile(filePaths.sourcePath, filePaths.targetPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
