package dev.josid.util.file;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FileUtils {

    public static List<Path> getDirectoryContent(Path headerPath) throws IOException {
        return getDirectoryContent(headerPath, null);
    }

    public static List<Path> getDirectoryContent(final Path headerPath, final Predicate<Path> filter) throws IOException {
        Stream<Path> content = Files.list(headerPath);

        if (filter != null)
            content = content.filter( filter );

        return content.collect( Collectors.toList() );
    }

    public static List<FilePaths> getFilesPathsRecursive(final Path sourcePath, final Path targetPath) throws IOException {
        return getFilesPathsRecursive(sourcePath, targetPath, (Predicate<Path>) null);
    }

    public static List<FilePaths> getFilesPathsRecursive(final Path sourcePath, final Path targetPath, final String... extensionsFilter) throws IOException {
        return FileUtils.getFilesPathsRecursive(sourcePath, targetPath, pathExtensionFilter(extensionsFilter));
    }

    public static List<FilePaths> getFilesPathsRecursive(final Path sourcePath, final Path targetPath, final Predicate<Path> filter) throws IOException {
        final List<FilePaths> result = new ArrayList<>();
        final Predicate<Path> filterWithDirectory = filter == null ? null :
                filter.or( path -> Files.isDirectory(path) );
        final List<Path> paths = getDirectoryContent(sourcePath, filterWithDirectory);

        for (final Path path: paths ) {
            final Path newTargetPath = targetPath.resolve(path.getFileName());

            if(Files.isDirectory(path))
                result.addAll( getFilesPathsRecursive(path, newTargetPath, filter) );
            else
                result.add( new FilePaths(path, newTargetPath) );
        }

        return result;
    }

    public static Predicate<Path> pathExtensionFilter(String... extensions) {
        for (int i = 0; i < extensions.length; i++)
            extensions[i] = extensions[i].contains(".") ? extensions[i] : "." + extensions[i];
        return path -> {
            String fileName = path.getFileName().toString().toLowerCase();
            for(String extension : extensions) if(fileName.endsWith(extension))
                return true;
            return false;
        };
    }

    public static void cloneFile(Path source, Path target) throws IOException {
        createFile(target);
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void cloneFile(Path source, Path target, Function<FileEditLine, LineOperation>... lineTransformers) throws IOException {
        Function<FileEditLine, LineOperation> lineTransformer = fileEditLine -> {
            LineOperation lineOperation = null;
            for ( Function<FileEditLine, LineOperation> singleLineTransformer: lineTransformers ) {
                lineOperation = singleLineTransformer.apply(fileEditLine);
                if (lineOperation.operation != LineOperation.Operation.NO_OPERATION)
                    break;
            }
            return lineOperation;
        };

        List<String> fileLines = Files.readAllLines(source);
        List<String> newFileLines = new ArrayList<>( fileLines.size() );
        for (int index = 0; index < fileLines.size(); index++) {
            LineOperation lineOperation = lineTransformer.apply(new FileEditLine(source, fileLines, index));
            switch (lineOperation.operation) {
                case NO_OPERATION:
                    newFileLines.add(fileLines.get(index));
                    break;
                case REPLACE:
                    for (String newLine: lineOperation.replaceLines)
                        newFileLines.add(newLine);
                default:
                case DELETE:
                    break;
            }
        }

        createFile(target);
        try (BufferedWriter writer = Files.newBufferedWriter(target)) {
            for (String newLine: newFileLines) {
                writer.write(newLine);
                writer.write(System.lineSeparator());
            }
        }
    }

    public static void createFile(Path target) throws IOException {
        if (!Files.exists(target)) {
            Files.createDirectories(target.getParent());
            Files.createFile(target);
        }
    }

}
