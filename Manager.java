// We need to import the java.sql package to use JDBC

import java.sql.*;

// for reading from the command line

@SuppressWarnings("ALL")
public class Manager extends controller {

    public int managerID;
    private int branch;

    public void validateID(int input) throws FormattingException {
        int id;
        ResultSet rs;
        PreparedStatement ps;
        try {
            id = input;
            String connectURL = "jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug";
            con = DriverManager.getConnection(connectURL, "ora_a1q1b", "a24581167");
            ps = con.prepareStatement("SELECT * FROM Clerk WHERE clerkID = ? AND type = 'Manager'");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                managerID = id;
                branch = rs.getInt("branchNumber");
                ps.close();
            } else throw new FormattingException("invalid id");
        } catch (SQLException ex) {
            NotificationUI error = new NotificationUI(ex.getMessage());
            error.setVisible(true);
            try {
                con.rollback();
            } catch (SQLException ex2) {
                NotificationUI error2 = new NotificationUI(ex2.getMessage());
                error2.setVisible(true);
            }
        }
    }

    public void manageEmployeeWage(int employeeID, int wage) throws FormattingException{
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("UPDATE Clerk SET wage = ? WHERE clerkID = ? AND branchNumber = ?");
            ps.setInt(2, employeeID);
            ps.setInt(3, branch);
            if (wage < 0) {
                throw new FormattingException("Wage Cannot be Negative");
            }
            ps.setInt(1, wage);
            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                throw new FormattingException("invalid id");
            }
            con.commit();
            ps.close();
        } catch (SQLException f) {
            NotificationUI error = new NotificationUI(f.getMessage());
            error.setVisible(true);
        }
    }

    public void displayAllEmployees() throws SQLException {
        int clerkID;
        String name;
        String type;
        int wage;
        int branchNumber;
        Statement stmt;
        ResultSet rs;

        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT * FROM Clerk");

        // get info on ResultSet
        ResultSetMetaData rsmd = rs.getMetaData();

        // get number of columns
        int numCols = rsmd.getColumnCount();

        System.out.println(" ");

        // display column names;
        for (int i = 0; i < numCols; i++) {
            // get column name and print it
            System.out.printf("%-15s", rsmd.getColumnName(i + 1));
        }

        System.out.println(" ");

        while (rs.next()) {
            // simplified output formatting; truncation may occur
            clerkID = rs.getInt("clerkID");
            System.out.printf("%-15s", clerkID);

            name = rs.getString("name");
            System.out.printf("%-15s", name);

            wage = rs.getInt("wage");
            System.out.printf("%-15s", wage);

            branchNumber = rs.getInt("branchNumber");
            System.out.printf("%-15s", branchNumber);

            type = rs.getString("type");
            System.out.printf("%-15s\n", type);
        }
        // close the statement;
        // the ResultSet will also be closed
        stmt.close();
    }

    public boolean manageItemStorage(int id, int amount) throws SQLException{
        PreparedStatement ps;
        ps = con.prepareStatement("UPDATE Storage SET amount = ? WHERE itemID = ? AND branchNumber = ?");
        ps.setInt(2, id);
        ps.setInt(3, branch);
        ps.setInt(1, amount);

        int rowCount = ps.executeUpdate();
        if (rowCount == 0) {
            ps.close();
            return false;
        }
        ps.close();
        con.commit();
        return true;
    }

    public void manageItemPrice(int id, double price) throws FormattingException, SQLException {
        PreparedStatement ps;
        ps = con.prepareStatement("UPDATE Item A SET A.price = ? WHERE A.itemID = (SELECT S.itemID FROM Storage S WHERE A.itemID = S.itemID AND S.itemID = ? AND S.branchNumber = ?)");
        ps.setInt(2, id);
        ps.setInt(3, branch);
        if (price < 0) throw new FormattingException("Invalid price");
        ps.setDouble(1, price);
        int rowCount = ps.executeUpdate();
        if (rowCount == 0) {
            ps.close();
            throw new FormattingException("Branch does not have item");
        }
        con.commit();
        ps.close();
    }

    public void displayAllItems() throws SQLException {
        String     itemID;
        String     itemName;
        String     itemPrice;
        String     itemType;
        Statement  stmt;
        ResultSet  rs;

        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT * FROM Item");
        ResultSetMetaData rsmd = rs.getMetaData();
        int numCols = rsmd.getColumnCount();
        System.out.println(" ");
        for (int i = 0; i < numCols; i++) {
            if (i == 0)
                System.out.printf("%-15s", rsmd.getColumnName(i+1));
            if (i == 1)
                System.out.printf("%-30s", rsmd.getColumnName(i+1));
            if (i == 2)
                System.out.printf("%-10s", rsmd.getColumnName(i+1));
            if (i == 3)
                System.out.printf("%-15s", rsmd.getColumnName(i+1));
        }
        System.out.println(" ");
        while(rs.next()) {
            itemID = rs.getString("itemID");
            System.out.printf("%-17s", itemID);

            itemName = rs.getString("name");
            System.out.printf("%-22s", itemName);

            itemPrice = rs.getString("price");
            System.out.printf("%-20.20s", itemPrice);

            itemType = rs.getString("type");
            System.out.printf("%-15s\n", itemType);

        }
        stmt.close();
    }

    public void deleteItemFromDeal(int itemId, String dealName) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM ItemsInDeal WHERE dealName = \'" + dealName + "\' AND itemID = ?");
            ps.setInt(1, itemId);
            ps.executeUpdate();
            con.commit();
        } catch (SQLException se) {
            NotificationUI error = new NotificationUI(se.getMessage());
            error.setVisible(true);
        }

    }

    public void addItemToDeal(int itemId, String dealName, double percent) throws FormattingException{
        try {
            if(!searchItem(itemId)) throw new FormattingException("Item not Found");
            if(!searchDeal(dealName)) throw new FormattingException("Deal Name not Found");
//            if(!searchItemInDealWithSamePercentage(itemId, dealName, percent)) throw new FormattingException("Item already in deal");
            PreparedStatement ps = con.prepareStatement("INSERT INTO ItemsInDeal VALUES (?,?,?)");
            ps.setInt(1, itemId);
            ps.setString(2, dealName);
            ps.setDouble(3, percent);
            ps.executeUpdate();
            con.commit();
            ps.close();
        } catch (SQLException s) {
//            NotificationUI error = new NotificationUI(s.getMessage());
//            error.setVisible(true);
            throw new FormattingException("Item already exist in deal");
        }
    }

    private boolean searchItem(int itemId){
        try {
            Statement s = con.createStatement();
            ResultSet res = s.executeQuery("SELECT * FROM Item WHERE itemID = " + itemId);
            if (!res.next())
                return false;
        } catch (SQLException s) {
            NotificationUI error = new NotificationUI(s.getMessage());
            error.setVisible(true);
        }
        return true;
    }

    private boolean searchDeal(String dealName) {
        try {
            PreparedStatement s = con.prepareStatement("SELECT * FROM Deal WHERE dealName = \'" + dealName + "\'");
            ResultSet res = s.executeQuery();
            if (!res.next())
                return false;
        } catch (SQLException s) {
            NotificationUI error = new NotificationUI(s.getMessage());
            error.setVisible(true);
        }
        return true;
    }

    private boolean searchItemInDealWithSamePercentage(int itemID, String dealName, double percent) {
        try {
            PreparedStatement s = con.prepareStatement("SELECT * FROM Deal WHERE itemID = \'" + itemID + "\' AND  dealName =\'" + dealName + "\'");
            ResultSet res = s.executeQuery();
            if (!res.next())
                return false;
        } catch (SQLException s) {
            NotificationUI error = new NotificationUI(s.getMessage());
            error.setVisible(true);
        }
        return true;
    }

    public void displayAllDeal() {
        String dealName;
        int itemID;
        double percentage;

        Statement stmt;
        ResultSet rs;
        try {
            stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT d.dealName AS dealName, d.STARTDATE as startdate, d.ENDDATE as enddate, id.itemId as itemId, id.percentage as persentage FROM Deal d, ItemsInDeal id WHERE d.dealName = id.dealName");
            // get info on ResultSet
            ResultSetMetaData rsmd = rs.getMetaData();
            // get number of columns
            int numCols = rsmd.getColumnCount();
            System.out.println(" ");
            // display column names;
            for (int i = 0; i < numCols; i++) {
                // get column name and print it
                System.out.printf("%-15s", rsmd.getColumnName(i + 1));
            }
            System.out.println(" ");
            while (rs.next()) {
                // for display purposes get everything from Oracle
                // as a string
                // simplified output formatting; truncation may occur
                dealName = rs.getString("dealName");
                System.out.printf("%-20.20s", dealName);
                Timestamp startdate = rs.getTimestamp("startdate");
                System.out.printf("%-20.20s", startdate.toString().substring(0, 10));
                Timestamp enddate = rs.getTimestamp("enddate");
                System.out.printf("%-20.20s", enddate.toString().substring(0, 10));
                itemID = rs.getInt("itemId");
                System.out.printf("%-10.10s", itemID);
                percentage = rs.getDouble("persentage");
                System.out.printf("%-15.15s\n", percentage);
            }
            // close the statement;
            // the ResultSet will also be closed
            stmt.close();
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
        }

    }

    public void addNewDeal(String dealName, String start, String end) throws FormattingException{
        Timestamp startDate;
        Timestamp endDate;
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("INSERT INTO Deal VALUES (?,?,?)");
            ps.setString(1, dealName);
            int[] timeStart = parseTimestamp(start);
            int[] timeEnd = parseTimestamp(end);
            startDate = new Timestamp(timeStart[0], timeStart[1], timeStart[2], 0, 0, 0, 0);
            endDate = new Timestamp(timeEnd[0], timeEnd[1], timeEnd[2], 0, 0, 0, 0);
            ps.setTimestamp(2, startDate);
            ps.setTimestamp(3, endDate);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            throw new FormattingException("Deal already exist");
        }
    }

    public void deleteDeal(String name) throws FormattingException {
        try {
            Statement ps = con.createStatement();
            ps.executeUpdate("DELETE FROM (SELECT * FROM Deal d WHERE d.dealName = \'" + name + "\')");
            con.commit();
        } catch (SQLException s) {
            throw new FormattingException("Deal does not exist");
        }
    }

    public void modifyDealPercent(int itemId, String name, double percentage) throws FormattingException {
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE ITEMSINDEAL SET percentage = ? WHERE itemID = ? AND dealName = \'" + name + "\'");
            ps.setDouble(1, percentage);
            ps.setInt(2, itemId);
            ps.executeUpdate();
            con.commit();
        } catch (SQLException se) {
            throw new FormattingException("Item does not Exist");
        }
    }

    public void modifyDealDuration(String name, String start, String end) throws FormattingException{
        try {
            int[] timeStart = parseTimestamp(start);
            int[] timeEnd = parseTimestamp(end);
            java.sql.Timestamp startDate = new java.sql.Timestamp(timeStart[0], timeStart[1], timeStart[2], 0, 0, 0, 0);
            java.sql.Timestamp endDate = new java.sql.Timestamp(timeEnd[0], timeEnd[1], timeEnd[2], 0, 0, 0, 0);
            PreparedStatement ps = con.prepareStatement("UPDATE Deal d SET d.startDate = ? , d.endDate = ? WHERE d.dealName = \'" + name + "\'");
            ps.setTimestamp(1, startDate);
            ps.setTimestamp(2, endDate);
            ps.executeUpdate();
            con.commit();
        } catch (SQLException s) {
            throw new FormattingException("Deal Does not exist");
        }
    }

    public static int[] parseTimestamp(String time) {
        // xx-xx-xx
        int[] array = new int[3];
        array[0] = Integer.parseInt(time.substring(0,2))+2000-1900;
        array[1] = Integer.parseInt(time.substring(3, 5))-1;
        array[2] = Integer.parseInt(time.substring(6, 8));
        return array;
    }


    public void getSalesRecord(String start, String end) {
        int receiptNumber;
        Timestamp purchaseTime;
        double totalPrice;
        int clerkID;
        int branchNumber;
        int[] timeStart = parseTimestamp(start);
        int[] timeEnd = parseTimestamp(end);
        java.sql.Timestamp startDate = new java.sql.Timestamp(timeStart[0], timeStart[1] , timeStart[2], 0, 0, 0, 0);
        java.sql.Timestamp endDate = new java.sql.Timestamp(timeEnd[0], timeEnd[1], timeEnd[2], 0, 0, 0, 0);
        try {
            PreparedStatement ps;
            ResultSet rs;

            ps = con.prepareStatement("SELECT * FROM Purchase WHERE purchaseTime >= ? AND purchaseTime <= ? AND branchNumber = ?");
            ps.setTimestamp(2, endDate);
            ps.setTimestamp(1, startDate);

            ps.setInt(3, branch);

            rs = ps.executeQuery();
            // get info on ResultSet

            System.out.println(" ");

            // display column names;
            System.out.printf("%-14s", "R.ID" );
            System.out.printf("%-30s","purchaseTime");
            System.out.printf("%-14s", "totalPrice");
            System.out.printf("%-13s", "clerkID");
            System.out.printf("%-10s", "branchNumber");

            System.out.println(" ");
            while (rs.next()) {
                receiptNumber = rs.getInt("receiptNumber");
                System.out.printf("%-13.13s", receiptNumber);

                purchaseTime = rs.getTimestamp("purchaseTime");
                System.out.printf("%-25.25s", purchaseTime);

                totalPrice = rs.getDouble("totalPrice");
                System.out.printf("%-16.10s", totalPrice);

                clerkID = rs.getInt("clerkID");
                System.out.printf("%-15.7s", clerkID);

                branchNumber = rs.getInt("branchNumber");
                System.out.printf("%-10.10s\n", branchNumber);

                // close the statement;
                // the ResultSet will also be closed
            }
            ps.close();
        } catch (SQLException s) {
            NotificationUI ui = new NotificationUI(s.getMessage());
            ui.setVisible(true);
        }
    }

    public void getTotalTransactionAmount(String start, String end) {
        Double totalPrice;
        int count;
        PreparedStatement ps;
        ResultSet rs;
        int[] timeStart = parseTimestamp(start);
        int[] timeEnd = parseTimestamp(end);
        java.sql.Timestamp startDate = new java.sql.Timestamp(timeStart[0], timeStart[1], timeStart[2], 0, 0, 0, 0);
        java.sql.Timestamp endDate = new java.sql.Timestamp(timeEnd[0], timeEnd[1], timeEnd[2], 0, 0, 0, 0);
        try {
//            ps = con.prepareStatement("SELECT SUM(totalPrice) AS SUM, COUNT(receiptNumber) AS CON FROM Purchase WHERE purchaseTime <= ? AND branchNumber = ?");
            ps = con.prepareStatement("SELECT SUM(totalPrice) AS SUM, COUNT(receiptNumber) AS CON FROM Purchase WHERE purchaseTime >= ? AND branchNumber = ?");
            ps.setTimestamp(1, startDate);
            ps.setInt(2, branch);
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCols = rsmd.getColumnCount();
            for (int i = 0; i < numCols; i++) {
                // get column name and print it
                System.out.printf("%-15s", rsmd.getColumnName(i + 1));
            }
            System.out.println(" ");
            while (rs.next()) {
                totalPrice = rs.getDouble("SUM");
                System.out.printf("%-15s", totalPrice);
                count = rs.getInt("CON");
                System.out.printf("%-5s", count);
            }
            ps.close();
        } catch (SQLException s) {
            NotificationUI ui = new NotificationUI(s.getMessage());
            ui.setVisible(true);
        }
    }

    public void findPurchasesContainsAllItemsOnSale() {
        int receiptNumber;
        Statement stmt;
        ResultSet rs;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("select distinct ip.receiptNumber from ItemsInPurchase ip where not exists (select iind.itemID from ItemsInDeal iind Minus select iinp.itemID from ItemsInPurchase iinp where iinp.receiptNumber = ip.receiptNumber)");
            ResultSetMetaData rsmd = rs.getMetaData();
            // get number of columns
            int numCols = rsmd.getColumnCount();
            // if (!rs.next()) {
            //     System.out.println("no such purchase found! ");
            System.out.println(" ");
            // display column names;
            for (int i = 0; i < numCols; i++) {
                // get column name and print it
                System.out.printf("%-15s", rsmd.getColumnName(i + 1));
            }
            System.out.println(" ");
            while (rs.next()) {
                // for display purposes get everything from Oracle
                // as a string
                // simplified output formatting; truncation may occur
                receiptNumber = rs.getInt("receiptNumber");
                System.out.printf("%-10.10s\n", receiptNumber);
            }
            // close the statement;
            // the ResultSet will also be closed
            stmt.close();
        }catch (SQLException e){
            NotificationUI ui = new NotificationUI(e.getMessage());
            ui.setVisible(true);
        }
    }

    public void getMaxWageFromAllBranches(){
        int wage;
        int branch;
        int clerkID;
        Statement stmt;
        ResultSet rs;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT MAX(wage), branchNumber FROM Clerk GROUP BY branchNumber");
            ResultSetMetaData rsmd = rs.getMetaData();
            // get number of columns
            int numCols = rsmd.getColumnCount();
            // display column names;
            for (int i = 0; i < numCols; i++) {
                // get column name and print it
                System.out.printf("%-15s", rsmd.getColumnName(i + 1));
            }
            System.out.println(" ");
            while (rs.next()) {
                // simplified output formatting; truncation may occur
                wage = rs.getInt("MAX(wage)");
                System.out.printf("%-25s", wage);
//                clerkID = rs.getInt("clerkID");
//                System.out.printf("%-5s", clerkID);
                branch = rs.getInt("branchNumber");
                System.out.printf("%-5s\n", branch);
            }
            // close the statement;
            // the ResultSet will also be closed
            stmt.close();
        } catch (SQLException s) {
            NotificationUI error = new NotificationUI(s.getMessage());
            error.setVisible(true);
        }
    }

    public void getMinWageFromAllBranches(){
        int wage;
        int branch;
        int clerkID;
        Statement stmt;
        ResultSet rs;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT MIN(wage), branchNumber FROM Clerk GROUP BY branchNumber");
            ResultSetMetaData rsmd = rs.getMetaData();
            // get number of columns
            int numCols = rsmd.getColumnCount();
            // display column names;
            for (int i = 0; i < numCols; i++) {
                // get column name and print it
                System.out.printf("%-15s", rsmd.getColumnName(i + 1));
            }
            System.out.println(" ");
            while (rs.next()) {
                // simplified output formatting; truncation may occur
                wage = rs.getInt("MIN(wage)");
                System.out.printf("%-25s", wage);
//                clerkID = rs.getInt("clerkID");
//                System.out.printf("%-5s", clerkID);
                branch = rs.getInt("branchNumber");
                System.out.printf("%-5s\n", branch);
            }
            // close the statement;
            // the ResultSet will also be closed
            stmt.close();
        } catch (SQLException s) {
            NotificationUI error = new NotificationUI(s.getMessage());
            error.setVisible(true);
        }
    }

    public void getMaxAvgItemPrice() {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT MAX(price) as Max_price FROM (SELECT AVG(price) AS price FROM Item I, Storage S WHERE I.itemID = S.itemID GROUP BY branchNumber)");
            rs.next();
            double max_price = rs.getDouble("Max_price");
            System.out.println("Max avg price: "+ max_price);
        } catch (SQLException s) {
            System.out.println("SQL exception MAx avg");
        }

    }
    public void getMinAvgItemPrice() {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT MIN(price) as MIN_price FROM (SELECT AVG(price) AS PRICE FROM ITEM I, Storage S WHERE I.itemID = S.itemID GROUP BY branchNumber)");
            rs.next();
            System.out.println("Min avg price: "+rs.getDouble("Min_price"));
        } catch (SQLException se){
            System.out.println("SQl exception Min avg");
        }

    }

}
