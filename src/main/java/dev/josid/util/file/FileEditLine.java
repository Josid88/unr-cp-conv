package dev.josid.util.file;

import java.util.List;


public class FileEditLine {

    private final List<String> fileLines;
    private final int index;

    public FileEditLine(List<String> fileLines, int index) {
        this.fileLines = fileLines;
        this.index = index;
    };

    public String currentLine() {
        return fileLines.get(index);
    }

    public String nextLine() {
        return nextLine(1);
    }

    public String nextLine(int offset) {
        return fileLines.size() > index+offset ?fileLines.get(index+offset) : null;
    }

    public String prevLine() {
        return prevLine(1);
    }

    public String prevLine(int offset) {
        return index-offset >= 0 ? fileLines.get(index+offset) : null;
    }

}
