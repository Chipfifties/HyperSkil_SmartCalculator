package calculator;

public class ErrorCode {
    int errorCode;

    ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    protected String getError() {
        switch (errorCode) {
            case 1: return "Invalid assignment";
            case 2: return "Invalid identifier";
            case 3: return "Unknown variable";
            case 4: return "Invalid expression";
            case 5: return "Unknown command";
            default: return "Unknown error";
        }
    }
}
