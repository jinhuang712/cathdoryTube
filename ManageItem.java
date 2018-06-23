import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.sql.SQLException;

@SuppressWarnings("Serial")
public class ManageItem extends JFrame {

    private final int WIDTH = 600, HEIGHT = 500;
    private Manager manager;
    public ManageItem(Manager manager) {
        setSize(WIDTH, HEIGHT);
        this.manager = manager;
        setTitle("Task: Manage Items");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        draw();
    }

    private JPanel buttonsPanel;
    private JScrollPane tablePanel;
    private buttonHandler handler;
    public void draw() {
        setLayout(new BorderLayout());
        handler = new buttonHandler();
        drawButtonPanel();
        add(buttonsPanel, BorderLayout.SOUTH);
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
        try {
            manager.displayAllItems();
        } catch (SQLException s) {
            NotificationUI error = new NotificationUI(s.getMessage());
            error.setVisible(true);
        }
    }

    private JButton changeItemStorage, changeItemPrice;
    private void drawButtonPanel() {
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(2, 1));
        changeItemStorage = new JButton("Change Item Storage");
        changeItemStorage.addActionListener(handler);
        changeItemPrice = new JButton("Change Item Price");
        changeItemPrice.addActionListener(handler);
        buttonsPanel.add(changeItemStorage);
        buttonsPanel.add(changeItemPrice);
    }

    changeItemPrice changeItemPriceFrame;
    changeItemStorage changeItemStorageFrame;
    private class buttonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == changeItemPrice) {
                changeItemPrice();
                changeItemPriceFrame.setVisible(true);
            } else if (source == changeItemStorage) {
                changeItemStorage();
                changeItemStorageFrame.setVisible(true);
            }
        }
    }

    private void changeItemPrice() {
        changeItemPriceFrame = new changeItemPrice();
    }

    private void changeItemStorage() {
        changeItemStorageFrame = new changeItemStorage();
    }

    private class changeItemPrice extends JFrame {

        private final int WIDTH = 200, HEIGHT = 100;

        public changeItemPrice() {
            setSize(WIDTH, HEIGHT);
            setTitle("Task: change Item Price");
            setResizable(false);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
            draw();
        }

        JButton setPrice;
        JPanel labelPanel, fieldPanel;
        priceHandler handler;

        public void draw() {
            handler = new priceHandler();
            setLayout(new BorderLayout());
            drawLabelPanel();
            add(labelPanel, BorderLayout.WEST);
            drawFieldPanel();
            add(fieldPanel, BorderLayout.CENTER);
            setPrice = new JButton("Change");
            setPrice.addActionListener(handler);
            add(setPrice, BorderLayout.SOUTH);
        }

        JLabel label_itemNumber, label_price;

        public void drawLabelPanel() {
            labelPanel = new JPanel(new GridLayout(2, 1));
            label_itemNumber = new JLabel("item number");
            label_price = new JLabel("price");
            labelPanel.add(label_itemNumber);
            labelPanel.add(label_price);
        }

        JTextField tf_itemNumber, tf_price;

        public void drawFieldPanel() {
            fieldPanel = new JPanel(new GridLayout(2, 1));
            tf_itemNumber = new JTextField();
            tf_price = new MyTextField("Enter a Decimal");
            fieldPanel.add(tf_itemNumber);
            fieldPanel.add(tf_price);
        }

        private class priceHandler implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source == setPrice) {
                    try {
                        int id = Integer.parseInt(tf_itemNumber.getText());
                        double price = Double.parseDouble(tf_price.getText());
                        manager.manageItemPrice(id, price);
                        NotificationUI success = new NotificationUI("Success!", "", "complete");
                        success.setVisible(true);
                    } catch (NumberFormatException n) {
                        NotificationUI error = new NotificationUI("invalid entry");
                        error.setVisible(true);
                    } catch (FormattingException f) {
                        NotificationUI error = new NotificationUI(f.getMessage());
                        error.setVisible(true);
                    } catch (SQLException s) {
                        NotificationUI error = new NotificationUI(s.getMessage());
                        error.setVisible(true);
                    }
                }
            }
        }
    }

    private class changeItemStorage extends JFrame {

        private final int WIDTH = 200, HEIGHT = 100;
        public changeItemStorage() {
            setSize(WIDTH, HEIGHT);
            setTitle("Task: change Item Storage");
            setResizable(false);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
            draw();
        }

        JButton setStorage;
        JPanel labelPanel, fieldPanel;
        amountHandler handler;
        public void draw() {
            handler = new amountHandler();
            setLayout(new BorderLayout());
            drawLabelPanel();
            add(labelPanel, BorderLayout.WEST);
            drawFieldPanel();
            add(fieldPanel, BorderLayout.CENTER);
            setStorage = new JButton("Change");
            setStorage.addActionListener(handler);
            add(setStorage, BorderLayout.SOUTH);
        }

        JLabel label_itemNumber, label_amount;
        public void drawLabelPanel() {
            labelPanel = new JPanel(new GridLayout(2, 1));
            label_itemNumber = new JLabel("item number");
            label_amount = new JLabel("amount");
            labelPanel.add(label_itemNumber);
            labelPanel.add(label_amount);
        }

        JTextField tf_itemNumber, tf_amount;
        public void drawFieldPanel() {
            fieldPanel = new JPanel(new GridLayout(2, 1));
            tf_itemNumber = new JTextField();
            tf_amount = new JTextField();
            fieldPanel.add(tf_itemNumber);
            fieldPanel.add(tf_amount);
        }

        private class amountHandler implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source == setStorage) {
                    try{
                        int amount = Integer.parseInt(tf_amount.getText());
                        int iid = Integer.parseInt(tf_itemNumber.getText());
                        if (!manager.manageItemStorage(iid, amount)) {
                            NotificationUI ui = new NotificationUI("Invalid Action");
                            ui.setVisible(true);
                        } else {
                            NotificationUI success = new NotificationUI("Success", "", "complete");
                            success.setVisible(true);
                        }
                    } catch (NumberFormatException ee){
                        NotificationUI notificationUI = new NotificationUI("invalid input!");
                        notificationUI.setVisible(true);
                    } catch (SQLException s) {
                        NotificationUI error = new NotificationUI(s.getMessage());
                        error.setVisible(true);
                    }
                }
            }
        }
    }
}

