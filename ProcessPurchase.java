import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.util.*;

@SuppressWarnings("Serial")
public class ProcessPurchase extends JFrame {
    Employee employee;
    double price;
    double[] price_id;
    double totalPrice;
    int itemId;
    Stack<Integer> lastItem;
    int receiptNumber;

    private final int WIDTH = 600, HEIGHT = 500;
    public ProcessPurchase(Employee employee) {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Task: Process Purchase");
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        totalPrice = 0;
        itemId = 0;
        lastItem = new Stack<Integer>();
        this.employee = employee;
        receiptNumber = employee.processPurchase();
        System.out.println(receiptNumber);
        this.draw();
    }

    private JPanel processPanel;
    private JScrollPane tablePanel;
    private buttonHandler handler;
    public void draw() {
        setLayout(new BorderLayout());
        handler = new buttonHandler();
        drawProcessPanel();
        drawTablePanel();
        add(processPanel, BorderLayout.SOUTH);
        add(tablePanel, BorderLayout.CENTER);
    }

    private JButton add, delete, deleteLast, quit, finish;
    private JTextField textField;
    private void drawProcessPanel() {
        processPanel = new JPanel();
        processPanel.setLayout(new BorderLayout());
        textField = new JTextField();
        processPanel.add(textField, BorderLayout.CENTER);

        add = new JButton("Add");
        add.addActionListener(handler);
        delete = new JButton("Delete");
        delete.addActionListener(handler);
        deleteLast = new JButton("Delete Last Item");
        deleteLast.addActionListener(handler);
        quit = new JButton("Quit");
        quit.addActionListener(handler);
        finish = new JButton("Purchase done");
        finish.addActionListener(handler);

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(2, 3));
        buttons.add(add);
        buttons.add(delete);
        buttons.add(deleteLast);
        buttons.add(finish);
        buttons.add(quit);
        buttons.add(new JButton());
        processPanel.add(buttons, BorderLayout.SOUTH);
    }

    JTextArea area;
    private void drawTablePanel() {

        area = new JTextArea();
        area.setBackground(Color.WHITE);
        area.setForeground(Color.BLACK);
        tablePanel = new JScrollPane(area);
        PrintStream out = new PrintStream( new TextAreaOutputStream( area ) );
        System.setOut( out );
        //System.setErr( out );
        try {
            employee.showPurchase(receiptNumber, totalPrice);
        }catch (SQLException e){
            NotificationUI error = new NotificationUI(e.getMessage());
            error.setVisible(true);
        }
    }


    private class buttonHandler implements ActionListener {
        @Override
        // todo
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            try {
                if (source == add) {
                    if(!Constraints.ifIDFormatCorrect(textField.getText()))
                        throw  new FormattingException("Wrong ItemID Format");

                    itemId = Integer.parseInt(textField.getText());
                    price_id = employee.addItem(itemId, receiptNumber);
                    price = price_id[0];
                    totalPrice += price_id[0];
                    lastItem.push((int)price_id[1]);
                    area.setText("");
                    employee.showPurchase(receiptNumber, totalPrice);
                } else if (source == delete) {
                    if(!Constraints.ifIDFormatCorrect(textField.getText()))
                        throw  new FormattingException("Wrong ItemID Format");
                    itemId = Integer.parseInt(textField.getText());
                    totalPrice -= employee.deleteItemHelper(itemId, receiptNumber);
                    area.setText("");
                    employee.showPurchase(receiptNumber, totalPrice);
                } else if (source == deleteLast) {
                    if(lastItem.empty()){
                        NotificationUI notificationUI = new NotificationUI("Nothing in the list");
                        notificationUI.setVisible(true);
                    }else {
                        totalPrice -= employee.deleteItemHelper(lastItem.pop(), receiptNumber);
                        area.setText("");
                        employee.showPurchase(receiptNumber, totalPrice);
                    }
                } else if (source == finish){
                    employee.purchaseFinish(receiptNumber, totalPrice);
                    NotificationUI error = new NotificationUI("Thank You","Purchase complete", "Purchase done");
                    setVisible(false);
                    error.setVisible(true);
                } else if (source == quit){
                    employee.purchaseQuit(receiptNumber);
                    setVisible(false);
                }
            }catch (SQLException ex){
                NotificationUI error = new NotificationUI(ex.getMessage());
                error.setVisible(true);
            }catch (FormattingException f){
                f.printError();
            }
        }
    }
}
