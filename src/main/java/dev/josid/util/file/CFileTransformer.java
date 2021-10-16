package dev.josid.util.file;

import java.util.Arrays;
import java.util.function.Function;

public class CFileTransformer {

    public static Function<FileEditLine, LineOperation> includeDirectoryRemover(final String directory) {
        return fileEditLine -> {
            String currentLine = fileEditLine.currentLine();
            if (currentLine.contains("#include") && currentLine.contains(directory) ) {
                String newLine = currentLine.replaceAll(directory, "");
                return LineOperation.REPLACE(newLine);
            }
            return LineOperation.NO_OPERATION();
        };
    }

    public static Function<FileEditLine, LineOperation> addAtBeginning(String fileName, String[] lines) {
        return fileEditLine -> {
            if (fileEditLine.sourcePath.getFileName().toString().equalsIgnoreCase(fileName) && fileEditLine.getIndex() == 0) {
                String[] newLines = new String[lines.length + 1];
                System.arraycopy(lines, 0, newLines, 0, lines.length);
                newLines[lines.length] = fileEditLine.currentLine();
                return LineOperation.REPLACE(newLines);
            }
            return LineOperation.NO_OPERATION();
        };
    }

    public static Function<FileEditLine, LineOperation> cleanVariableDefinition() {
        return fileEditLine -> {
            if (fileEditLine.currentLine().contains(" = {}")){
                return LineOperation.REPLACE(fileEditLine.currentLine().replace(" = {}", ""));
            }
            return LineOperation.NO_OPERATION();
        };
    }

    public static String defineVariable(String variableName, String value) {
        return String.format("#ifndef %s\n    #define %s %s\n#endif", variableName, variableName, value);
    }

}
