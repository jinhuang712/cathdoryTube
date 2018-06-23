import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
// for reading from the command line
// for the login window

public class Member extends controller {
    int memID;
    public Member() {
        connect("ora_a1q1b", "a24581167");
    }
    public void validateID(int input) throws FormattingException {
        ResultSet rs;
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("SELECT points FROM MemberShip WHERE memberID = ?");
            ps.setInt(1, input);
            rs = ps.executeQuery();
            if (rs.next()) {
                memID = input;
                ps.close();
                return;
            }
            ps.close();
            throw new FormattingException("invalid member id");
        }catch(SQLException e){
            NotificationUI error = new NotificationUI(e.getMessage());
            error.setVisible(true);
        }
    }

    public int checkPoint() {
        Statement statement;
        ResultSet result;
        int       points = 0;
        try {
            statement = con.createStatement();
            result = statement.executeQuery("SELECT * FROM Membership WHERE memberID = " + memID);
            result.next();
            result.getInt("memberID");
            points = result.getInt("points");
        } catch (SQLException e){
            NotificationUI error = new NotificationUI(e.getMessage());
            error.setVisible(true);
        }
        return points;
    }
}
