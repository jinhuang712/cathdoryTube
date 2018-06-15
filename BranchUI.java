import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

@SuppressWarnings("serial")
public class BranchUI extends JFrame{

    final int WIDTH = 200, HEIGHT = 150;
    public BranchUI() {
        setSize(WIDTH, HEIGHT);
        setTitle("Branch.exe");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        draw();
    }

    JPanel north;
    JPanel center;
    public void draw() {
        setLayout(new BorderLayout());
        drawNorth();
        drawCentre();
    }


    JLabel friendly;
    JLabel logo;
    private void drawNorth() {
        north = new JPanel();
        north.setLayout(new BorderLayout());
        friendly = new JLabel("Tell us about you...");
        try {
            Image image = ImageIO.read(new File("logo.jpeg"));
            image = image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            logo = new JLabel(new ImageIcon(image));
            north.add(logo, BorderLayout.WEST);
        } catch (IOException i) {

        }
        north.add(friendly, BorderLayout.CENTER);
        add(north, BorderLayout.NORTH);
    }

    JButton customer, employee, manager;
    private void drawCentre() {
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
        add(center, BorderLayout.CENTER);
    }

    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == customer) {
//                System.out.print("hi customer");
                CustomerUI customerUI = new CustomerUI();
                customerUI.setResizable(false);
                customerUI.setVisible(true);
            } else if (source == employee) {
//                System.out.print("hi employee");
                EmployeeUI employeeUI = new EmployeeUI();
                employeeUI.setResizable(false);
                employeeUI.setVisible(true);
            } else if (source == manager) {
//                System.out.print("hi manager");
                ManagerUI managerUI = new ManagerUI();
                managerUI.setResizable(false);
                managerUI.setVisible(true);
            }
            setVisible(false);
            dispose();
        }
    }
}