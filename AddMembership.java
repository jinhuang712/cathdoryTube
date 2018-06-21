import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;

@SuppressWarnings("Serial")
public class AddMembership extends JFrame {
    Employee employee;
    private final int WIDTH = 300, HEIGHT = 100;
    public AddMembership(Employee employee) {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Task: Add Membership");
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.employee = employee;
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        this.draw();
    }

    JButton add;
    JPanel inputPanel;
    private void draw() {
        setLayout(new BorderLayout());
        add = new JButton("Add Member");
        add.addActionListener(new buttonHandler());
        add(add, BorderLayout.SOUTH);
        drawInputPanel();
        add(inputPanel, BorderLayout.CENTER);
    }

    JLabel label_name, label_phone;
    JTextArea ta_name, ta_phoneNumber;
    boolean modified = false;
    private void drawInputPanel() {
        inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        GridLayout layout = new GridLayout(2, 1);
        layout.setVgap(5);
        JPanel unchangable = new JPanel(layout);
        label_name = new JLabel("Name: ");
        label_phone = new JLabel("Phone Number: ");
        unchangable.add(label_name);
        unchangable.add(label_phone);
        inputPanel.add(unchangable, BorderLayout.WEST);
        JPanel changable = new JPanel(layout);
        ta_name = new JTextArea();
        ta_phoneNumber = new JTextArea("xxx-xxx-xxxx");
        ta_phoneNumber.setForeground(Color.GRAY);
        ta_phoneNumber.addMouseListener(new MouseHandler());
        changable.add(ta_name);
        changable.add(ta_phoneNumber);
        inputPanel.add(changable, BorderLayout.CENTER);
    }

    private class MouseHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if (modified)
                return;
            ta_phoneNumber.setText("");
            ta_phoneNumber.setForeground(Color.BLACK);
            modified = true;
        }
    }

    private class buttonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == add) {
                try {
                    String name = ta_name.getText();
                    String phoneNumber = ta_phoneNumber.getText();
                    if (Constraints.ifNameFormattingWrong(name)) throw new FormattingException("Wrong Name Format");
                    if (Constraints.ifPhoneFormatWrong(phoneNumber)) throw new FormattingException("Wrong Phone Number Format");
                    // todo if membership is valid
//                    Member member = new Member();
//                    if (member)
                    int membershipID =  employee.manageMemberShip(name, phoneNumber);;
                    // todo add membership
                    NotificationUI ui = new NotificationUI("Member "+ta_name.getText()+" is added!",
                            "Membership# is "+membershipID, "Congratulations");
                    ui.setVisible(true);
                } catch (FormattingException f) {
                    f.printError();
                }
                // todo add member to database
                ta_name.setText("");
                ta_phoneNumber.setText("");
            }
        }
    }
}
