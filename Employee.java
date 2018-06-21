import java.io.IOException;
import java.sql.*;
import java.util.Random;

public class Employee extends controller {
    public int id;
    public int branch;
    public Employee() {}

    public int manageMemberShip(String name, String phone){
        PreparedStatement ps;
        ResultSet rs;
        int id = 0;
        int point;
        boolean current = false;
        Random r = new Random();
        try {
            while (!current) {
                id = r.nextInt(1000) + 100000;
                ps = con.prepareStatement("SELECT * FROM MemberShip WHERE memberID = ?");
                ps.setInt(1, id);
                rs = ps.executeQuery();
                if (!rs.next())
                    current = true;
                ps.close();
            }

            ps = con.prepareStatement("INSERT INTO MemberShip VALUES (?,?,?,0)");
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, phone);
            ps.executeUpdate();

            con.commit();
            ps.close();
        }catch (SQLException ex){
            NotificationUI error = new NotificationUI(ex.getMessage());
            error.setVisible(true);
        }
        return id;
    }

    public int processPurchase() {
        int itemid;
        int lastItem = 0;
        double[] price_id = {0,0};
        double price;
        double totalPrice = 0;
        int receiptNumber = 0;
        boolean current = false;
        boolean finish = false;
        PreparedStatement ps;
        ResultSet rs;
        Random r = new Random();
        try {
            while (!current) {
                receiptNumber = branch * 1000 + r.nextInt(100) + 1000000;
                ps = con.prepareStatement("SELECT * FROM Purchase WHERE receiptNumber = ?");
                ps.setInt(1, receiptNumber);
                rs = ps.executeQuery();
                if (!rs.next())
                    current = true;
                ps.close();
            }

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            ps = con.prepareStatement("INSERT INTO Purchase VALUES (?,?,1,?,?)");
            ps.setInt(1, receiptNumber);
            ps.setTimestamp(2, timestamp);
            ps.setInt(3, id);
            ps.setInt(4, branch);

            ps.executeUpdate();

            con.commit();
            ps.close();
        }catch (SQLException e){
            NotificationUI error = new NotificationUI(e.getMessage());
            error.setVisible(true);
            System.exit(-1);
        }
        return  receiptNumber;

    }
//
    public boolean validateID(int eid) {
        PreparedStatement ps;
        ResultSet rs;
        try {
            String connectURL = "jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug";
            con = DriverManager.getConnection(connectURL,"ora_a1q1b", "a24581167");
            ps = con.prepareStatement("SELECT branchNumber FROM Clerk WHERE clerkID = ?");
            ps.setInt(1, eid);
            rs = ps.executeQuery();
            if (!rs.next()) {
                return false;
            } else {
                this.branch = rs.getInt("branchNumber");
                this.id = eid;
            }
            ps.close();
        } catch (SQLException ex) {
            NotificationUI error = new NotificationUI(ex.getMessage());
            error.setVisible(true);
            try {
                con.rollback();
            }
            catch (SQLException ex2)
            {
                NotificationUI error2 = new NotificationUI(ex2.getMessage());
                error.setVisible(true);
            }
        }
        return true;
    }

    public void showPurchase(int receiptNumber, double totalPrice) throws SQLException {
        String     itemID;
        String     itemName;
        double    itemPrice;
        int itemAmount = 0;
        String     itemType;
        PreparedStatement  ps;
        ResultSet  rs;
        System.out.println("Clerk ID : " + id +"      Branch Number: " + branch + "      Time: " + new Timestamp(System.currentTimeMillis()) );
        System.out.print("Items:  ");
        try {
            ps = con.prepareStatement("SELECT * FROM Item i, ItemsInPurchase ip WHERE ip.receiptNumber = ? and i.itemID = ip.itemID");
            ps.setInt(1, receiptNumber);
            rs = ps.executeQuery();

            System.out.println(" ");


            System.out.printf("%-10s", "ITEMID");
            System.out.printf("%-30s", "NAME");
            System.out.printf("%-15s", "PRICE");
            System.out.printf("%-20s", "TYPE");
            System.out.printf("%-15s", "AMOUNT");

            System.out.println(" ");

            while (rs.next()) {

                itemID = rs.getString("itemID");
                System.out.printf("%-10s", itemID);

                itemName = rs.getString("name");
                System.out.printf("%-30s", itemName);

                itemPrice = rs.getDouble("price");
                System.out.printf("%-10s", itemPrice);

                itemType = rs.getString("type");
                System.out.printf("%-20s", itemType);

                itemAmount = rs.getInt("amount");
                System.out.printf("%-15s\n", itemAmount);

            }
            ps.close();
        }catch (SQLException e){
            NotificationUI error = new NotificationUI(e.getMessage());
            error.setVisible(true);
            System.exit(-1);
        }

        System.out.print("\nTotal: " + Math.round(totalPrice * 100.0) / 100.0);

    }

    public double[] addItem(int itemid, int receiptNumber) throws SQLException{
        PreparedStatement  ps;
        ResultSet  rs;
        double[] rvalue = new double[2];
        ps = con.prepareStatement("SELECT * FROM item WHERE itemID = ?");
        ps.setInt(1, itemid);
        rs = ps.executeQuery();
        if (!rs.next()) {
            NotificationUI notificationUI = new NotificationUI("invalid Item ID");
            notificationUI.setVisible(true);
        }
        else {
            rvalue[0] = rs.getDouble("price");
            rvalue[1] = itemid;
            ps = con.prepareStatement("SELECT * FROM itemsInPurchase WHERE itemID = ? AND receiptNumber = ?");
            ps.setInt(1, itemid);
            ps.setInt(2, receiptNumber);
            rs = ps.executeQuery();
            if (!rs.next()) {
                ps = con.prepareStatement("INSERT INTO itemsInPurchase VALUES (?,?,1)");
                ps.setInt(1, receiptNumber);
                ps.setInt(2, itemid);
                ps.executeUpdate();
            } else {
                ps = con.prepareStatement("UPDATE itemsInPurchase SET amount = ? WHERE itemID = ? AND receiptNumber = ?");
                ps.setInt(3, receiptNumber);
                ps.setInt(2, itemid);
                ps.setInt(1, rs.getInt("amount") + 1);
                ps.executeUpdate();
            }
            con.commit();
            ps.close();

        }
        ps.close();
        return rvalue;

    }


    public void deleteLastItem(int lastItem, int receiptNumber) throws SQLException{
        PreparedStatement  ps;
        ResultSet  rs;
        int amount;
        double price;
        ps = con.prepareStatement("SELECT * FROM itemsInPurchase WHERE itemID = ? AND receiptNumber = ?");
        ps.setInt(1,lastItem);
        ps.setInt(2,receiptNumber);
        rs = ps.executeQuery();
        rs.next();
        amount = rs.getInt("amount");
        if(amount == 1){
            ps = con.prepareStatement("DELETE FROM ItemsInPurchase WHERE itemID = ? AND receiptNumber = ?");
            ps.setInt(1,lastItem);
            ps.setInt(2,receiptNumber);
            ps.executeUpdate();

        }
        else {
            ps = con.prepareStatement("UPDATE itemsInPurchase SET amount = ? WHERE itemID = ? AND receiptNumber = ?");
            ps.setInt(3, receiptNumber);
            ps.setInt(2, lastItem);
            System.out.println(amount - 1);
            ps.setInt(1,amount - 1);
            ps.executeUpdate();
        }
        con.commit();
        ps.close();

    }

    public double deleteItemHelper (int itemId, int receiptNumber) throws SQLException{
        PreparedStatement  ps;
        ResultSet  rs;
        double price = 0;
        boolean vaildItem = true;
        ps = con.prepareStatement("SELECT * FROM item WHERE itemID = ?");
        ps.setInt(1, itemId);
        rs = ps.executeQuery();
        if(!rs.next())
            vaildItem = false;
        else
            price = rs.getDouble("price");
        ps = con.prepareStatement("SELECT * FROM itemsInPurchase WHERE itemID = ? AND receiptNumber = ?");
        ps.setInt(1,itemId);
        ps.setInt(2,receiptNumber);
        rs = ps.executeQuery();
        if(!rs.next()) {
            vaildItem = false;
            price = 0;
        }
        if(vaildItem)
            deleteLastItem(itemId,receiptNumber);
        else {
            NotificationUI notificationUI = new NotificationUI("invalid Item ID or Item doesn't in the list");
            notificationUI.setVisible(true);
        }
        ps.close();
        return price;
    }

    public void purchaseQuit(int receiptNumber) throws SQLException{
        PreparedStatement ps;
        ps = con.prepareStatement("DELETE FROM Purchase WHERE receiptNumber = ?");
        ps.setInt(1,receiptNumber);
        ps.executeUpdate();
        con.commit();
        ps.close();
        System.out.println("\nPurchase canceled");
    }

    public void purchaseFinish(int receiptNumber, double totalPrice) throws SQLException{
        PreparedStatement ps;
        ResultSet  rs;
        ResultSet  rs1;
        ps = con.prepareStatement("UPDATE Purchase SET totalPrice = ? WHERE receiptNumber = ?");
        ps.setDouble(1,totalPrice);
        ps.setInt(2,receiptNumber);
        ps.executeUpdate();
        ps = con.prepareStatement("SELECT * FROM Item i, ItemsInPurchase ip WHERE ip.receiptNumber = ? and i.itemID = ip.itemID");
        ps.setInt(1, receiptNumber);
        rs = ps.executeQuery();
        while (rs.next()) {
            int itemID = rs.getInt("itemID");
            int itemAmount = rs.getInt("amount");
            updateStorage(itemID,itemAmount);
        }
        ps.close();
    }

    private void updateStorage(int itemID, int amount) throws SQLException{
        PreparedStatement ps;
        ResultSet  rs;
        int totalamount;
        ps = con.prepareStatement("SELECT * FROM Storage WHERE itemID = ? AND branchNumber = ?");
        ps.setInt(1, itemID);
        ps.setInt(2, branch);
        rs = ps.executeQuery();
        rs.next();
        totalamount = rs.getInt("amount");
        ps = con.prepareStatement("UPDATE Storage SET amount = ? WHERE itemID = ? AND branchNumber = ?");
        ps.setInt(2, itemID);
        ps.setInt(3, branch);
        ps.setInt(1, totalamount - amount);
        ps.executeQuery();
        con.commit();
        ps.close();

    }


}
