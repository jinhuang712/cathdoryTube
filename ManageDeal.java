import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

@SuppressWarnings("Serial")
public class ManageDeal extends JFrame {
    private final int WIDTH = 700, HEIGHT = 300;

    private Manager manager;
    public ManageDeal(Manager manager) {
        setSize(WIDTH, HEIGHT);
        setTitle("Task: Manage Items");
        this.manager = manager;
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

    JButton addNewDeal, modifyDeal;
    private void drawButtonPanel() {
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(2, 1));
        addNewDeal = new JButton("Add or Delete Deal");
        addNewDeal.addActionListener(handler);
        modifyDeal = new JButton("Modify Deal Items");
        modifyDeal.addActionListener(handler);
        buttonsPanel.add(addNewDeal);
        buttonsPanel.add(modifyDeal);
    }

    JTextArea area;
    private void drawMainPanel() {
        area = new JTextArea();
        tablePanel = new JScrollPane(area);
        PrintStream out = new PrintStream(new TextAreaOutputStream(area));
        System.setOut(out);
        System.setErr(out);
        manager.displayAllDeal();
    }

    AddDeleteModifyDeal addDeleteDealFrame;
    modifyItems modifyDealFrame;
    private class buttonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == addNewDeal) {
                addDeleteDealFrame = new AddDeleteModifyDeal();
                addDeleteDealFrame.setVisible(true);
            } else if (source == modifyDeal) {
                modifyDealFrame = new modifyItems();
                modifyDealFrame.setVisible(true);
            }
        }
    }

    private class AddDeleteModifyDeal extends JFrame {
        private final int WIDTH = 300, HEIGHT = 130;
        public AddDeleteModifyDeal() {
            setSize(WIDTH, HEIGHT);
            setTitle("Task: Add/Delete/Modify Deal");
            setResizable(false);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            draw();
        }

        addHandler handler;
        JPanel labelPanel, fieldPanel, buttonsPanel;
        public void draw() {
            handler = new addHandler();
            setLayout(new BorderLayout());
            drawLabelPanel();
            add(labelPanel, BorderLayout.WEST);
            drawFieldPanel();
            add(fieldPanel, BorderLayout.CENTER);
            drawButtonsPanel();
            add(buttonsPanel, BorderLayout.SOUTH);
        }

        JButton add, delete, modify;
        private void drawButtonsPanel() {
            buttonsPanel = new JPanel(new GridLayout(1, 3));
            add = new JButton("Add");
            add.addActionListener(handler);
            delete = new JButton("Delete");
            delete.addActionListener(handler);
            modify = new JButton("Modify");
            modify.addActionListener(handler);
            buttonsPanel.add(add);
            buttonsPanel.add(delete);
            buttonsPanel.add(modify);
        }

        JLabel label_dealName, label_startTime, label_endTime;
        private void drawLabelPanel() {
            labelPanel = new JPanel(new GridLayout(3, 1));
            label_dealName = new JLabel("name");
            label_startTime = new JLabel("start time");
            label_endTime = new JLabel("end time");
            labelPanel.add(label_dealName);
            labelPanel.add(label_startTime);
            labelPanel.add(label_endTime);
        }


        JTextField tf_dealName, tf_startTime, tf_endTime;
        private void drawFieldPanel() {
            fieldPanel = new JPanel(new GridLayout(3, 1));
            tf_dealName = new JTextField();
            tf_startTime = new MyTextField("yr-mo-da");
            tf_endTime = new MyTextField("yr-mo-da");
            fieldPanel.add(tf_dealName);
            fieldPanel.add(tf_startTime);
            fieldPanel.add(tf_endTime);
        }

        private class addHandler implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                String name = tf_dealName.getText();
                String start = tf_startTime.getText();
                String end = tf_endTime.getText();
                try {
                    if (source == add) {
                        if (!Constraints.ifCorrectDateFormat(start) || !Constraints.ifCorrectDateFormat(end))
                            throw new FormattingException("invalid time entry");
                        manager.addNewDeal(name, start, end);
                    } else if (source == delete) {
                        manager.deleteDeal(name);
                    } else if (source == modify) {
                        if (!Constraints.ifCorrectDateFormat(start) || !Constraints.ifCorrectDateFormat(end))
                            throw new FormattingException("invalid time entry");
                        manager.modifyDealDuration(name, start, end);
                    }
                    area.setText("");
                    manager.displayAllDeal();
                    NotificationUI success = new NotificationUI("Success", "", "complete");
                    success.setVisible(true);
                } catch (FormattingException f) {
                    NotificationUI error = new NotificationUI(f.getMessage());
                    error.setVisible(true);
                }
            }
        }
    }

    private class modifyItems extends JFrame {
        private final int WIDTH = 300, HEIGHT = 130;
        public modifyItems() {
            setSize(WIDTH, HEIGHT);
            setTitle("Task: Modify Deal Items");
            setResizable(false);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            draw();
        }

        addHandler handler;
        JPanel labelPanel, fieldPanel, buttonsPanel;
        public void draw() {
            handler = new addHandler();
            setLayout(new BorderLayout());
            drawLabelPanel();
            add(labelPanel, BorderLayout.WEST);
            drawFieldPanel();
            add(fieldPanel, BorderLayout.CENTER);
            drawButtonsPanel();
            add(buttonsPanel, BorderLayout.SOUTH);
        }

        JButton add, delete, modify;
        private void drawButtonsPanel() {
            buttonsPanel = new JPanel(new GridLayout(1, 3));
            add = new JButton("Add");
            add.addActionListener(handler);
            delete = new JButton("Delete");
            delete.addActionListener(handler);
            modify = new JButton("Modify");
            modify.addActionListener(handler);
            buttonsPanel.add(add);
            buttonsPanel.add(delete);
            buttonsPanel.add(modify);
        }

        JLabel label_itemID, label_dealName, label_discount;
        private void drawLabelPanel() {
            labelPanel = new JPanel(new GridLayout(3, 1));
            label_itemID = new JLabel("itemID");
            label_dealName = new JLabel("deal name");
            label_discount = new JLabel("discount");
            labelPanel.add(label_itemID);
            labelPanel.add(label_dealName);
            labelPanel.add(label_discount);
        }


        JTextField tf_itemID, tf_dealName, tf_discount;
        private void drawFieldPanel() {
            fieldPanel = new JPanel(new GridLayout(3, 1));
            tf_itemID = new JTextField();
            tf_dealName = new JTextField();
            tf_discount = new MyTextField("Enter a decimal");
            fieldPanel.add(tf_itemID);
            fieldPanel.add(tf_dealName);
            fieldPanel.add(tf_discount);
        }

        private class addHandler implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                try {
                    int itemId = Integer.parseInt(tf_itemID.getText());
                    String dealName = tf_dealName.getText();
                    double discount = Double.parseDouble(tf_discount.getText());
                    if (source == add) {
                        manager.addItemToDeal(itemId, dealName, discount);
                    } else if (source == delete) {
                        manager.deleteItemFromDeal(itemId, dealName);
                    } else if (source == modify) {
                        manager.modifyDealPercent(itemId, dealName, discount);
                    }
                    area.setText("");
                    manager.displayAllDeal();
                    NotificationUI success = new NotificationUI("Success!", "", "complete");
                    success.setVisible(true);
                } catch (FormattingException f) {
                    NotificationUI error = new NotificationUI(f.getMessage());
                    error.setVisible(true);
                } catch (NumberFormatException n) {
                    NotificationUI error = new NotificationUI("Invalid Format");
                    error.setVisible(true);
                }
            }
        }
    }
}