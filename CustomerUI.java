import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class CustomerUI extends JFrame {

    final int WIDTH = 250, HEIGHT = 130;
    public CustomerUI() {
        setSize(WIDTH, HEIGHT);
        setTitle("Member");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        draw();
    }

    JLabel friendly, value;
    JTextField memberID;
    JButton checkout, quit;
    JPanel north;
    public void draw() {
        setLayout(new BorderLayout());
        north = new JPanel();
        north.setLayout(new BorderLayout());
        friendly = new JLabel("Your available points are...", SwingConstants.CENTER);
        friendly.setForeground(Color.BLUE);
        north.add(friendly, BorderLayout.NORTH);
        memberID = new JTextField();
        memberID.setForeground(Color.BLUE);
        checkout = new JButton("Enter");
        checkout.addActionListener(new buttonHandler());
        north.add(memberID, BorderLayout.CENTER);
        north.add(checkout, BorderLayout.EAST);
        value = new JLabel("", SwingConstants.CENTER);
        Font font = new Font("Arial", Font.BOLD, 50);
        value.setFont(font);
        quit = new JButton("exit");
        quit.addActionListener(new buttonHandler());
        add(north, BorderLayout.NORTH);
        add(value, BorderLayout.CENTER);
        add(quit, BorderLayout.SOUTH);
    }

    private class buttonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == quit) {
                System.exit(0);
            } else if (source == checkout) {
                // todo: extract value for member
                value.setText("1111");
            }
            repaint();
        }
    }
}
