import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

public class GetSalesRecord extends JFrame {


    private final int WIDTH = 600, HEIGHT = 400;
    private Manager manager;
    public GetSalesRecord(Manager manager) {
        setSize(WIDTH, HEIGHT);
        this.manager = manager;
        setTitle("Task: Get Sales Record");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        draw();
    }

    private JScrollPane tablePanel;
    private JPanel inputPanel;
    public void draw() {
        setLayout(new BorderLayout());
        drawMainPanel();
        add(tablePanel, BorderLayout.CENTER);
        drawInputPanel();
        add(inputPanel, BorderLayout.SOUTH);
    }

    private JTextArea area;
    private void drawMainPanel() {
        area = new JTextArea();
        tablePanel = new JScrollPane(area);
        PrintStream out = new PrintStream(new TextAreaOutputStream(area));
        System.setOut(out);
        System.setErr(out);
    }

    private JPanel labels, fields;
    private JLabel lb_start, lb_end;
    private JTextField tf_start, tf_end;
    private JButton button;
    private handler handler;
    private void drawInputPanel() {
        handler = new handler();
        inputPanel = new JPanel(new BorderLayout());
        labels = new JPanel(new GridLayout(2,1));
        lb_start = new JLabel("Start date");
        lb_end = new JLabel("End date");
        labels.add(lb_start);
        labels.add(lb_end);
        inputPanel.add(labels, BorderLayout.WEST);
        fields = new JPanel(new GridLayout(2,1));
        tf_start = new MyTextField("yr-mo-da");
        tf_end = new MyTextField("yr-mo-da");
        fields.add(tf_start);
        fields.add(tf_end);
        inputPanel.add(fields);
        button = new JButton("Done");
        inputPanel.add(button, BorderLayout.EAST);
        button.addActionListener(handler);
    }


    private class handler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            try {
                if (source == button) {
                    String start = tf_start.getText();
                    String end = tf_end.getText();
                    if (!Constraints.ifCorrectDateFormat(start) || !Constraints.ifCorrectDateFormat(end))
                        throw new FormattingException("invalid date format");
                    manager.getSalesRecord(start, end);
                }
            }  catch (FormattingException f) {
                NotificationUI error = new NotificationUI(f.getMessage());
                error.setVisible(true);
            }
        }
    }
}
