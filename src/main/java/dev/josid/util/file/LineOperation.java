package dev.josid.util.file;

public class LineOperation {

    public enum Operation {
        NO_OPERATION, REPLACE, DELETE
    }

    Operation operation;
    String[] replaceLines;

    public LineOperation(Operation operation) {
        this.operation = operation;
    }

    public LineOperation(Operation operation, String[] replaceLines) {
        this.operation = operation;
        this.replaceLines = replaceLines;
    }

    public static LineOperation DELETE() {
        return new LineOperation(Operation.DELETE);
    }

    public static LineOperation NO_OPERATION() {
        return new LineOperation(Operation.NO_OPERATION);
    }

    public static LineOperation REPLACE(String... lines) {
        return new LineOperation(Operation.REPLACE, lines);
    }

}
