package Database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Renjini Ramesh
 */
public class Driver {

    

    //method to get connection to mySQL database
    public static Connection getConnection() throws Exception {
        try {
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/projectone?useSSL=false";
            String username = "root";
            String password = "1234";

            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected!");
            return conn;
        } catch (SQLException e) {
            System.out.println("SQL Exception " + e);
        } catch (Exception e) {
            System.out.println("General Exception" + e);
        }
        return null;
    }

    //method to create required tables
    public static void createTable() throws Exception {
        try {
            //get connection
            Connection con = getConnection();

            //create table for Student
            PreparedStatement createStudentTable = con.prepareStatement("CREATE TABLE IF NOT EXISTS student(id varchar(255), name varchar(255), program varchar(255), visitcount int, gender varchar(255), PRIMARY KEY(id))");
            createStudentTable.executeUpdate();

            //create table for Visit as a child of Student
            PreparedStatement createVisitTable = con.prepareStatement("CREATE TABLE IF NOT EXISTS visit(id int NOT NULL AUTO_INCREMENT, studentid varchar(255), reason varchar(255), date date, PRIMARY KEY(id), FOREIGN KEY (studentid) REFERENCES student(id))");
            createVisitTable.executeUpdate();

            //create table for announcement
            PreparedStatement createAnnouncementTable = con.prepareStatement("CREATE TABLE IF NOT EXISTS announcement(id int NOT NULL AUTO_INCREMENT, program varchar(255), announcement varchar(255), PRIMARY KEY(id))");
            createAnnouncementTable.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error " + e);
            e.printStackTrace();
        } finally {
            System.out.println("create Table method complete");
        }
    }

    //method to query and student details from database
    public static Student getStudentFromDB(String ID) {
        Student objStudent = new Student();
        try {
            Connection con = getConnection();
            // create the java statement for getting number of child records of visit for the particular student
            Statement st = con.createStatement();
            String query = "Select * from student where id='" + ID + "'";
            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                objStudent.setID(Integer.parseInt(rs.getString("id")));
                objStudent.setName(rs.getString("name"));
                objStudent.setProgram(rs.getString("program"));
                objStudent.setGender(rs.getString("gender"));
                objStudent.setVisitCount(rs.getInt("visitcount"));
            }

        } catch (Exception e) {
            System.out.println("Exception caught" + e);
            e.printStackTrace();
        } finally {
            System.out.println("Get Student method over. Visits: " + objStudent.getVisitCount());
        }
        return objStudent;
    }

    //method to query and get visit report with dates, gender, program, readon
    public static ArrayList<Visit> getVisitReportFromDB(java.sql.Date fromDate, java.sql.Date toDate, String gender, String program, String reason) {
        ArrayList<Visit> arrVisit = new ArrayList<>();
        try {
            Connection con = getConnection();
            // create the java statement for getting number of child records of visit for the particular student
            Statement st = con.createStatement();
            //date is mandatory on ui
            String query = "SELECT visit.id, visit.reason, visit.studentid, student.name as StudentName, student.gender as Gender, student.program as Program, student.visitCount as VisitCount FROM visit JOIN student ON visit.studentid = student.id WHERE (visit.date BETWEEN '" + fromDate + "' AND '" + toDate + "')";
            //program can be any or specific  
            if (!program.equals("Any")) {
                query += "AND Program='" + program + "' ";
            }
            //gender can be any or specific
            if (!gender.equals("Any")) {
                query += "AND Gender='" + gender + "' ";
            }
            //reason can be any or specific
            if (!reason.equals("Any")) {
                query += "AND Reason='" + reason + "' ";
            }

            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);
            //set visit fields to return to UI
            while (rs.next()) {
                Visit objVisit = new Visit();
                objVisit.setID(rs.getString("id"));
                objVisit.setReason(rs.getString("reason"));
                objVisit.setStudentID(Integer.parseInt(rs.getString("studentid")));
                objVisit.setStudentName(rs.getString("StudentName"));
                objVisit.setStudentGender(rs.getString("Gender"));
                objVisit.setStudentProgram(rs.getString("Program"));
                objVisit.setStudentVisitCount(rs.getInt("VisitCount"));
                arrVisit.add(objVisit);
            }

        } catch (Exception e) {
            System.out.println("Exception caught" + e);
            e.printStackTrace();
        } finally {
            System.out.println("Get Visit report records method over");
        }
        return arrVisit;
    }

    //method to get count for different reasons during time period
    public static int getCountReportFromDB(java.sql.Date fromDate, java.sql.Date toDate, String reason) {
        int count = 0;
        try {
            Connection con = getConnection();
            // create the java statement for getting number of child records of visit for the particular student
            Statement st = con.createStatement();
            //date is mandatory on ui
            String query = "SELECT count(distinct studentid) from visit where reason ='" + reason + "' AND (visit.date BETWEEN '" + fromDate + "' AND '" + toDate+"')";
            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);
            //set visit fields to return to UI
            while (rs.next()) {
                count = rs.getInt("count(distinct studentid)");
            }
            
        } catch (Exception e) {
            System.out.println("Exception caught" + e);
            e.printStackTrace();
        } finally {
            System.out.println("Return count records method over");
        }
        return count;
    }

   
}
