import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("Serial")
class LoginUI extends JFrame {

    private String character;
    controller controller;
    Object object;
    private final int WIDTH = 200, HEIGHT = 90;
    public LoginUI(Object object, String character, controller obj) {
        this.character = character;
        this.controller = obj;
        this.object = object;
        setSize(WIDTH, HEIGHT);
        setTitle("Log In");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        draw();
    }

    private JButton logIn, quit;
    private JLabel welcome;
    private JTextField id_field;
    public void draw() {
        setLayout(new BorderLayout());
        welcome = new JLabel("Please Enter Your "+character+" ID", SwingConstants.CENTER);
        add(welcome, BorderLayout.NORTH);
        id_field = new JTextField();
        add(id_field, BorderLayout.CENTER);
        logIn = new JButton("Log In");
        logIn.addActionListener(new buttonHandler());
        add(logIn, BorderLayout.EAST);
        quit = new JButton("to main menu");
        quit.addActionListener(new buttonHandler());
        add(quit, BorderLayout.SOUTH);

    }

    private class buttonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == logIn) {
                int id = Integer.parseInt(id_field.getText());
                if (character.equals("Employee")) {
                    EmployeeUI ui = (EmployeeUI) object;
//                    !ui.employee.validateID(id)
                    Employee employee = (Employee) controller;
                    if (!employee.validateID(id)) {
                        NotificationUI notificationUI = new NotificationUI("invalid id");
                        notificationUI.setVisible(true);
                    } else {
                        ui.employeeID = id;
                        ui.repaint();
                        ui.setVisible(true);
                        setVisible(false);
                        dispose();
                    }
                } else if (character.equals("Manager")) {
                    // todo
                    ManagerUI ui = (ManagerUI) object;
//                    !ui.employee.validateID(id)
                    Manager manager = (Manager) controller;
                    if (!manager.validateID(id)) {
                        NotificationUI notificationUI = new NotificationUI("invalid id");
                        notificationUI.setVisible(true);
                    } else {
                        ui.managerID = id;
                        ui.repaint();
                        ui.setVisible(true);
                        setVisible(false);
                        dispose();
                    }

                }
            } else if (source == quit) {
                BranchUI branchUI = new BranchUI();
                branchUI.setVisible(true);
                setVisible(false);
                dispose();
            }
        }
    }
}

