import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;

@SuppressWarnings("serial")
public class BranchUI extends JFrame{
    static BranchUI branchUI ;

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
        customer = new JButton("Customer");
        employee = new JButton("Employee");
        manager = new JButton("Manager");
        // action listeners required...
        center.add(customer);
        center.add(employee);
        center.add(manager);
        add(center, BorderLayout.CENTER);
    }
}
