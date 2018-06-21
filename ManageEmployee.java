import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;

@SuppressWarnings("Serial")
public class ManageEmployee extends JFrame {

    private final int WIDTH = 700, HEIGHT = 700;
    private Manager manager;
    public ManageEmployee(Manager manager) {
        setSize(WIDTH, HEIGHT);
        setTitle("Task: Manage Employee");
        setResizable(false);
        this.manager = manager;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        draw();
    }

    private JPanel processPanel;
    private JScrollPane tablePanel;
    private buttonHandler handler;
    public void draw() {
        setLayout(new BorderLayout());
        handler = new buttonHandler();
        drawProcessPanel();
        add(processPanel, BorderLayout.SOUTH);
        drawMainPanel();
        add(tablePanel, BorderLayout.CENTER);
    }

    private JTextArea area;
    private void drawMainPanel() {
        area = new JTextArea();
        tablePanel = new JScrollPane(area);
        PrintStream out = new PrintStream(new TextAreaOutputStream(area));
        System.setOut(out);
        System.setErr(out);
        try {
            manager.showAllEmployees();
        } catch (SQLException s) {
            NotificationUI error = new NotificationUI(s.getMessage());
            error.setVisible(true);
        }
    }

    private JButton changeWage;
    private JTextField tf_wage, tf_employeeID;
    private JLabel label_wage, label_employeeID;
    private void drawProcessPanel() {
        processPanel = new JPanel();
        changeWage = new JButton("Change Wage");
        changeWage.addActionListener(handler);
        tf_wage = new JTextField(13);
        tf_employeeID = new JTextField(13);
        label_wage = new JLabel("Wage", SwingConstants.LEFT);
        label_employeeID = new JLabel("Employee ID", SwingConstants.LEFT);
        JPanel labels = new JPanel();
        GridLayout layout = new GridLayout(2, 1);
        layout.setVgap(5);
        labels.setLayout(layout);
        labels.add(label_employeeID);
        labels.add(label_wage);
        processPanel.add(labels, BorderLayout.WEST);
        JPanel fields = new JPanel();
        fields.setLayout(new GridLayout(2, 1));
        fields.add(tf_employeeID);
        fields.add(tf_wage);
        processPanel.add(fields, BorderLayout.CENTER);
        processPanel.add(changeWage, BorderLayout.WEST);
    }


    private class buttonHandler implements ActionListener {
        @Override
        // todo
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            try {
                if (source == changeWage) {
                    String id = tf_employeeID.getText();
                    String wage = tf_wage.getText();
                    if (!Constraints.ifIDFormatCorrect(id))
                        throw new FormattingException("Invalid Entry");
                    if (!Constraints.ifIDFormatCorrect(wage))
                        throw new FormattingException("Invalid Entry");
                    area.setText("");
                    manager.manageEmployeeWage(Integer.parseInt(id), Integer.parseInt(wage));
                    manager.showAllEmployees();
                }
            } catch (FormattingException f) {
                NotificationUI ui = new NotificationUI(f.getMessage());
                ui.setVisible(true);
            } catch (IOException i) {
                NotificationUI ui = new NotificationUI(i.getMessage());
                ui.setVisible(true);
            } catch (SQLException s) {
                NotificationUI ui = new NotificationUI(s.getMessage());
                ui.setVisible(true);
            }
        }
    }
}
