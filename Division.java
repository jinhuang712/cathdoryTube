import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

class Division extends JFrame {
    private final int WIDTH = 400, HEIGHT = 100;
    private Manager manager;
    public Division(Manager manager) {
        setSize(WIDTH, HEIGHT);
        this.manager = manager;
        setTitle("Task: Find Purchases Contains All Items On Sale");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        draw();
    }
    private JScrollPane tablePanel;
    public void draw() {
        setLayout(new BorderLayout());
        drawMainPanel();
        add(tablePanel, BorderLayout.CENTER);
    }

    private JTextArea area;
    private void drawMainPanel() {
        area = new JTextArea();
        tablePanel = new JScrollPane(area);
        PrintStream out = new PrintStream(new TextAreaOutputStream(area));
        System.setOut(out);
        System.setErr(out);
        manager.findPurchasesContainsAllItemsOnSale();
    }

}