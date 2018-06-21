import java.io.IOException;

public class FormattingException extends IOException {
    FormattingException(String message) {
        super(message);
    }

    void printError() {
        NotificationUI ui = new NotificationUI(super.getMessage());
        ui.setVisible(true);
    }
}
