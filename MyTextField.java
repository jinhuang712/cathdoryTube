import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class MyTextField extends JTextField {
    boolean empty;
    boolean modified;
    String string;
    public MyTextField(String string) {
        super();
        this.string = string;
        setToDefault();
        if (string.length() == 0) empty = true;
        this.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                modified = true;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (getText().length() == 0)
                    modified = false;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (modified)
                    return;
                else
                    setToEdit();
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (getText().length() == 0)
                    setToDefault();
                else
                    modified = true;
            }

        });
    }

    private void setToDefault() {
        setText(string);
        setForeground(Color.GRAY);
        modified = false;
    }

    private void setToEdit() {
        setText("");
        setForeground(Color.BLACK);
    }
}
