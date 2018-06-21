import java.io.IOException;
import java.sql.*;
// for reading from the command line
// for the login window

public class Member extends controller {
    int memID;
    public Member() {
        connect("ora_a1q1b", "a24581167");
    }
    public boolean validateID(int input) {
        int id;
        boolean correct = false;
        ResultSet rs;
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("SELECT points FROM MemberShip WHERE memberID = ?");
            ps.setInt(1, input);
            rs = ps.executeQuery();
            if (rs.next()) {
                memID = input;
                correct = true;
            }
            ps.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return correct;
    }

    public int checkPoint() {
        String    name;
        Statement statement;
        ResultSet result;
        int       points = 0;
        try {
            statement = con.createStatement();
            result = statement.executeQuery("SELECT * FROM Membership WHERE memberID = " + memID);
            result.next();
            result.getInt("memberID");
            // name = result.getString("name");
            points = result.getInt("points");
            return points;
        } catch (SQLException e){
            System.out.println("Message: " + e.getMessage());
            System.exit(-1);
        }
        return points;
    }
}
