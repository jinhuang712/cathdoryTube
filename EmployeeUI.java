import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class EmployeeUI extends JFrame {

    final int WIDTH = 500, HEIGHT = 70;
    public EmployeeUI() {
        setSize(WIDTH, HEIGHT);
        setTitle("Work Panel");
        setVisible(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        draw();
    }


    JPanel buttonsPanel, northPanel;
    logInPanel logInPanel;
    int employeeID;
    boolean notLoggedIn;
    buttonHandler handler;
    public void draw() {
        notLoggedIn = true;
        logInPanel = new logInPanel();
        logInPanel.setVisible(true);
        logInPanel.setResizable(false);
        if (notLoggedIn)
            return;
        setVisible(true);
        setResizable(false);
        setLayout(new BorderLayout());
        handler = new buttonHandler();
        northPanel = new JPanel();
        buttonsPanel = new JPanel();
        drawNorthPanel();
        add(northPanel, BorderLayout.NORTH);
        drawButtonsPanel();
        add(buttonsPanel, BorderLayout.CENTER);
    }

    JLabel friendly, displayID;
    JButton logOut;
    private void drawNorthPanel() {
        northPanel.setLayout(new BorderLayout());
        friendly = new JLabel("Employee ID: ", SwingConstants.CENTER);
        northPanel.add(friendly, BorderLayout.NORTH);
        displayID = new JLabel(employeeID+"");
        displayID.setForeground(Color.BLUE);
        northPanel.add(displayID, BorderLayout.CENTER);
        logOut = new JButton("Log Out");
        logOut.addActionListener(handler);
        northPanel.add(logOut, BorderLayout.EAST);
    }

    JButton processPurchase, manageMembership;
    private void drawButtonsPanel() {
        buttonsPanel.setLayout(new GridLayout(1, 2));
        processPurchase = new JButton("Process Purchase");
        processPurchase.addActionListener(handler);
        manageMembership = new JButton("Manage Membership");
        manageMembership.addActionListener(handler);
        buttonsPanel.add(processPurchase);
        buttonsPanel.add(manageMembership);
    }

    processPurchasePanel purchase;
    manageMembershipPanel manage;
    private class buttonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == logOut) {

            } else if (source == processPurchase) {
//                processPurchase();
                if (purchase == null)
                    processPurchase();
                else {
                    purchase.setVisible(false);
                    purchase.dispose();
                    purchase = null;
                }
            } else if (source == manageMembership) {
//                manageMembership();
                if (manage == null)
                    manageMembership();
                else {
                    manage.setVisible(false);
                    manage.dispose();
                    manage = null;
                }
            }
        }
    }

    private void processPurchase() {
        purchase = new processPurchasePanel();
        purchase.setResizable(false);
        purchase.setVisible(true);
    }

    private void manageMembership() {
        manage = new manageMembershipPanel();
        manage.setResizable(false);
        manage.setVisible(true);
    }

    private class logInPanel extends JFrame {

        private final int WIDTH = 200, HEIGHT = 80;
        private logInPanel() {
            this.setSize(WIDTH, HEIGHT);
            this.setTitle("Log In");
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            this.draw();
        }

        JButton logIn;
        JLabel welcome;
        JTextField id_field;
        private void draw() {
            this.setLayout(new BorderLayout());
            welcome = new JLabel("Please Enter Your Employee ID", SwingConstants.CENTER);
            this.add(welcome, BorderLayout.NORTH);
            id_field = new JTextField();
            this.add(id_field, BorderLayout.CENTER);
            logIn = new JButton("Log In");
            logIn.addActionListener(new logInHandler());
            this.add(logIn, BorderLayout.EAST);
        }

        private class logInHandler implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source == logIn) {
                    int id = Integer.parseInt(id_field.getText());
//                    if (successful)
                    notLoggedIn = false;
                    employeeID = id;
                    draw();
                    logInPanel.setVisible(false);
                    logInPanel.dispose();
                }
            }
        }
    }

    private class processPurchasePanel extends JFrame {

        private final int WIDTH = 400, HEIGHT = 700;
        private processPurchasePanel() {
            this.setSize(WIDTH, HEIGHT);
            this.setTitle("Task: Process Purchase");
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.draw();
        }

        private void draw() {

        }
    }

    private class manageMembershipPanel extends JFrame {

        private final int WIDTH = 400, HEIGHT = 700;
        private manageMembershipPanel() {
            this.setSize(WIDTH, HEIGHT);
            this.setTitle("Task: Manage Membership");
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.draw();
        }

        private void draw() {

        }
    }

}
