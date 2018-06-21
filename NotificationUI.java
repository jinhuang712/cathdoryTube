import javax.swing.*;
import java.awt.*;

@SuppressWarnings("Serial")
public class NotificationUI extends JFrame {

    private String message1;
    private String message2;
    private final int WIDTH = 500, HEIGHT = 100;
    public NotificationUI(String message1) {
        this.message1 = message1;
        message2 = "";
        setSize(WIDTH, HEIGHT);
        setTitle("Error");
        setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        draw();
    }


    public NotificationUI(String message1, String message2, String title) {
        this.message1 = message1;
        this.message2 = message2;
        setSize(WIDTH, HEIGHT);
        setTitle(title);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        drawTwoMessages();
    }

    private JLabel label;
    public void draw() {
        setLayout(new BorderLayout());
        label = new JLabel(message1, SwingConstants.CENTER);
        label.setForeground(Color.RED);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        add(label, BorderLayout.CENTER);
    }

    private JLabel label2;
    public void drawTwoMessages() {
        setLayout(new GridLayout(2, 1));
        label = new JLabel(message1, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.BLUE);
        label2 = new JLabel(message2, SwingConstants.CENTER);
        label2.setFont(new Font("Arial", Font.BOLD, 18));
        label2.setForeground(Color.BLUE);
        add(label);
        add(label2);
    }
}
