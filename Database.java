import java.sql.*;

public class Database {
	Connection con;
	PreparedStatement pst;
	ResultSet rs;

	Database() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:8889/annotator_sample", "root", "root");
			pst = con.prepareStatement("select * from users where username=? and password=?");

		} catch (Exception e) {
			System.out.println(e);
		}
	}
	// ip:username,password
	// return boolean

	/*
	 * public boolean checkLogin (String uname, String pwd){
	 * System.out.println(uname+"======"+pwd); return true; }
	 */

	public Boolean checkLogin(String uname, String pwd) {

		try {

			pst.setString(1, uname); // this replaces the 1st "?" in the query
										// for username
			pst.setString(2, pwd); // this replaces the 2st "?" in the query for
									// password
			// executes the prepared statement
			rs = pst.executeQuery();
			if (rs.next()) {
				// TRUE iff the query founds any corresponding data
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("error while validating" + e);
			return false;
		}
	}
	public boolean checkUsernameExists(String usr){
		try{
			PreparedStatement p =  con.prepareStatement("select * from users where username=?");
			p.setString(1, usr);
			rs = p.executeQuery();
			if(rs.next())
				return true;
			else return false;
		}
		catch(Exception e){
			System.out.println(e);
			return false;
		}
	}
	public boolean setPassword(String usr, String pwd){
		try{
			if(checkUsernameExists(usr)){
				PreparedStatement p =  con.prepareStatement("update users set password =? where username = ?");
				p.setString(1, pwd);
				p.setString(2, usr);
				rs = p.executeQuery();
				return true;
			}
			else
				return false;
		}
		catch(Exception e){
			System.out.println(e);
			return false;
		}
	}
	/*
	 * public static void main(String [] args){ Database db = new Database();
	 * System.out.println(db.checkLogin("raghu", "ram")); }
	 */
}