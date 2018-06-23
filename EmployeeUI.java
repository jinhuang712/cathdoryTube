import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
class EmployeeUI extends JFrame {

    private final int WIDTH = 400, HEIGHT = 90;
    int employeeID;
    public Employee employee;
    EmployeeUI() {
        setSize(WIDTH, HEIGHT);
        setTitle("Work Panel");
        setVisible(false);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        draw();
         this.employee = new Employee();
        LoginUI loginUI = new LoginUI(this, "Employee", employee);
        loginUI.setVisible(true);
    }

    private JPanel buttonsPanel, northPanel;
    private buttonHandler handler;
    public void draw() {
        setLayout(new BorderLayout());
        handler = new buttonHandler();
        northPanel = new JPanel();
        buttonsPanel = new JPanel();
        drawNorthPanel();
        add(northPanel, BorderLayout.NORTH);
        drawButtonsPanel();
        add(buttonsPanel, BorderLayout.CENTER);
    }

    public void repaint() {
        setVisible(true);
        displayID.setText(employeeID+"");
    }

    private JLabel friendly, displayID;
    private JButton logOut;
    private void drawNorthPanel() {
        northPanel.setLayout(new GridLayout(1, 3));
        friendly = new JLabel("Employee ID: ");
        northPanel.add(friendly);
        displayID = new JLabel(employeeID+"");
        displayID.setForeground(Color.BLUE);
        northPanel.add(displayID);
        logOut = new JButton("Log Out");
        logOut.addActionListener(handler);
        northPanel.add(logOut);

    }
    private void logOut() {

        LoginUI loginUI = new LoginUI(this, "Employee", employee);
        loginUI.setVisible(true);
        setVisible(false);
    }
    private ProcessPurchase purchase;
    private AddMembership addMem;


    private void processPurchase() {
        purchase = new ProcessPurchase(employee);
        purchase.setVisible(true);
    }

    private void addMembership() {
        addMem = new AddMembership(employee);
        addMem.setVisible(true);
    }


    JButton processPurchase, addMembership;
    private void drawButtonsPanel() {
        buttonsPanel.setLayout(new GridLayout(1, 2));
        processPurchase = new JButton("Process Purchase");
        processPurchase.addActionListener(handler);
        addMembership = new JButton("Add Membership");
        addMembership.addActionListener(handler);
        buttonsPanel.add(processPurchase);
        buttonsPanel.add(addMembership);
    }


    private class buttonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == logOut)
                logOut();
            else if (source == processPurchase) {
                if (purchase == null) {
                    processPurchase();
                }else {
                    purchase.setVisible(false);
                    purchase.dispose();
                    purchase = null;
                }
            } else if (source == addMembership) {
                if (addMem == null)
                    addMembership();
                else {
                    addMem.setVisible(false);
                    addMem.dispose();
                    addMem = null;
                }
            }
        }
    }


}
