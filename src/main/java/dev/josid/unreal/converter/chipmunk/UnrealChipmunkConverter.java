package dev.josid.unreal.converter.chipmunk;

import dev.josid.util.file.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;


public class UnrealChipmunkConverter {

    public static void main(String[] args) throws IOException {
        Path workingDirectory = Paths.get("").resolve("dist");

        String chipmunkHomeString = "F:\\dev\\wrk\\c\\Chipmunk2D";
        Path chipmunkHome = Paths.get(chipmunkHomeString);

        processHeaderFiles(chipmunkHome, workingDirectory);
        processSourceFiles(chipmunkHome, workingDirectory.resolve("chipmunk"));
    }

    private static Function<FileEditLine, LineOperation> includeChipmunkDirectoryRemover =
            CFileTransformer.includeDirectoryRemover("chipmunk/");

    private static void processHeaderFiles(Path chipmunkHome, Path targetDirectory) throws IOException {
        List<FilePaths> headerPaths = FileUtils.getFilesPathsRecursive( chipmunkHome.resolve("include"), targetDirectory,
                FileUtils.pathExtensionFilter("h")
                .and(path -> !path.getFileName().toString().contains("cpHastySpace")));

        headerPaths.forEach( filePaths -> {
            try {
                FileUtils.cloneFile(filePaths.sourcePath, filePaths.targetPath,
                        includeChipmunkDirectoryRemover,
                        CFileTransformer.cleanVariableDefinition(),
                        CFileTransformer.addAtBeginning("chipmunk_types.h", new String[]{
                                CFileTransformer.defineVariable("DEBUG", "0"),
                                CFileTransformer.defineVariable("TARGET_OS_IPHONE", "0"),
                                CFileTransformer.defineVariable("TARGET_OS_MAC", "0"),
                                CFileTransformer.defineVariable("CP_USE_CGTYPES", "0"),
                                CFileTransformer.defineVariable("CP_USE_DOUBLES", "0"),
                                CFileTransformer.defineVariable("DRAW_EPA", "0"),
                                CFileTransformer.defineVariable("DRAW_GJK", "0"),
                                CFileTransformer.defineVariable("DRAW_CLOSEST", "0"),
                        }));
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
                FileUtils.cloneFile(filePaths.sourcePath, filePaths.targetPath,
                        CFileTransformer.cleanVariableDefinition(),
                        includeChipmunkDirectoryRemover);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
