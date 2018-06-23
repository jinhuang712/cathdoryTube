import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

@SuppressWarnings("serial")
class BranchUI extends JFrame{

    private final int WIDTH = 200, HEIGHT = 200;
    BranchUI() {
        setSize(WIDTH, HEIGHT);
        setTitle("Branch.exe");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        draw();
    }

    private JPanel north;
    private JPanel center;
    public void draw() {
        setLayout(new BorderLayout());
        drawCentre();
        drawSouth();
    }


    private JLabel friendly;
    private JLabel logo;
    private void drawCentre() {
        north = new JPanel();
        north.setLayout(new BorderLayout());
        friendly = new JLabel("Tell us about you...");
        try {
            Image image = ImageIO.read(new File("logo.jpeg"));
            image = image.getScaledInstance(70, 70, Image.SCALE_DEFAULT);
            logo = new JLabel(new ImageIcon(image));
            north.add(logo, BorderLayout.CENTER);
        } catch (IOException i) {

        }
        north.add(friendly, BorderLayout.EAST);
        add(north, BorderLayout.CENTER);
    }

    private JButton customer, employee, manager;
    private void drawSouth() {
        center = new JPanel();
        center.setLayout(new GridLayout(3, 1));
        // creating the buttons
        customer = new JButton("Customer");
        employee = new JButton("Employee");
        manager = new JButton("Manager");
        // adding action listeners
        ButtonHandler handler = new ButtonHandler();
        customer.addActionListener(handler);
        employee.addActionListener(handler);
        manager.addActionListener(handler);
        // adding buttons to the canvas
        center.add(customer);
        center.add(employee);
        center.add(manager);
        // adding the panel to the main frame
        add(center, BorderLayout.SOUTH);
    }

    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == customer) {
//                System.out.print("hi customer");
                MemberUI customerUI = new MemberUI();
                customerUI.setVisible(true);
            } else if (source == employee) {
//                EmployeeUI employeeUI = new EmployeeUI(new Employee());
                  EmployeeUI employeeUI = new EmployeeUI();
//                employeeUI.setVisible(true);
            } else if (source == manager) {
//                System.out.print("hi manager");
                ManagerUI managerUI = new ManagerUI();
               // managerUI.setVisible(true);
            }
            setVisible(false);
            dispose();
        }
    }
}