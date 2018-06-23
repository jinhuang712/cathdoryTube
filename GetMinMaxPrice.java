import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

public class GetMinMaxPrice extends JFrame {

    private final int WIDTH = 400, HEIGHT = 100;
    private Manager manager;
    public GetMinMaxPrice(Manager manager) {
        setSize(WIDTH, HEIGHT);
        this.manager = manager;
        setTitle("Task: Get Employees' Min/Max Price");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        draw();
    }

    private JScrollPane tablePanel;
    private JPanel buttonsPanel;
    private handler handler;
    public void draw() {
        setLayout(new BorderLayout());
        handler = new handler();
        drawMainPanel();
        add(tablePanel, BorderLayout.CENTER);
        drawButtonsPanel();
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private JTextArea area;
    private void drawMainPanel() {
        area = new JTextArea();
        tablePanel = new JScrollPane(area);
        PrintStream out = new PrintStream(new TextAreaOutputStream(area));
        System.setOut(out);
        System.setErr(out);
    }

    private JButton min, max;
    private void drawButtonsPanel() {
        buttonsPanel = new JPanel(new GridLayout(1, 2));
        min = new JButton("Get Minimum");
        min.addActionListener(handler);
        buttonsPanel.add(min);
        max = new JButton("Get Maximum");
        max.addActionListener(handler);
        buttonsPanel.add(max);
    }

    private class handler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == max) {
                area.setText("");
                manager.getMaxAvgItemPrice();
            } else if (source == min) {
                area.setText("");
                manager.getMinAvgItemPrice();
            }
        }
    }

}
