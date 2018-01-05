package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents a student
 * @author Renjini Ramesh
 */
public class Student {
    
    private int id;
    private String name;
    private String program;
    private int visitCount;
    private String gender;

    //getter setters
    public void setID(int ID) {
        this.id = ID;
    }

    public int getID() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setVisitCount(int count) {
        this.visitCount = count;
    }
    
    public String getName() {
        return name;
    }

    public String getProgram() {
        return program;
    }

    public String getGender() {
        return gender;
    }

    public int getVisitCount() {
        return visitCount;
    }

    //method to create a visit for a particular student
    public Visit createVisit(String reason){
        Visit objVisit = new Visit();
        try {            
            visitCount++;
            objVisit.setStudent(this);
            objVisit.setReason(reason);
            //insert visit record to DB
            objVisit.insert();
            this.update();
        } catch (Exception ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
        return objVisit;
    }

    //method to get the latest visit records for this student
    public Visit getStudentVisits(){
        ArrayList<Visit> arrVisit = new ArrayList<>();
        try {
            Connection con = Driver.getConnection();
            // create the java statement for getting number of child records of visit for the particular student
            Statement st = con.createStatement();
            String query = "Select * from visit where studentid='"+id+"' ORDER BY id desc";
            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Visit objVisit = new Visit();
                objVisit.setID(rs.getString("id"));
                objVisit.setStudentID(Integer.parseInt(rs.getString("studentid")));
                objVisit.setDate(rs.getDate("date"));
                objVisit.setReason(rs.getString("reason"));
                arrVisit.add(objVisit);
            }
        } catch (Exception e) {
            System.out.println("Exception caught" + e);
            e.printStackTrace();
        } finally {
            System.out.println("Get visits method over");
        }
        return arrVisit.get(0);
    }

    //method to insert student record to database
    public void insert() throws Exception {
        try {
            Connection con = Driver.getConnection();
            PreparedStatement posted = con.prepareStatement("INSERT INTO student (id, name, program, visitcount, gender) VALUES('" + id + "', '" + name + "', '" + program + "', '" + visitCount + "','" + gender + "')");
            posted.executeUpdate();

        } catch (Exception e) {
            System.out.println("Exception caught" + e);
            e.printStackTrace();
        } finally {
            System.out.println("Student Insert method over");
        }
    }

    //method to update the student record
    public void update() throws Exception {
        try {
            Connection con = Driver.getConnection();
            PreparedStatement posted = con.prepareStatement("UPDATE student SET name='" + name + "', program='" + program + "', visitcount='" + visitCount + "', gender='" + gender + "' WHERE id='"+id+"'");
            posted.executeUpdate();

        } catch (Exception e) {
            System.out.println("Exception caught" + e);
            e.printStackTrace();
        } finally {
            System.out.println("Student Update method over. Count: " + visitCount);
        }
    }
    
    
//method to return announcements for the student
    public ArrayList<Announcement> getAnnouncements(){
        ArrayList<Announcement> arrAnn= new ArrayList<>();
      try {
            Connection con = Driver.getConnection();
            // create the java statement for getting number of child records of visit for the particular student
            Statement st = con.createStatement();
            //date is mandatory on ui
            String query = "SELECT * FROM announcement WHERE program='"+program+"'";
            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);
            //set visit fields to return to UI
            while (rs.next()) {
                Announcement objAnn= new Announcement();
                objAnn.setId(rs.getInt("id"));
                objAnn.setAnnouncement(rs.getString("announcement"));
                objAnn.setProgram(rs.getString("program"));
                arrAnn.add(objAnn);
            }

        } catch (Exception e) {
            System.out.println("Exception caught" + e);
            e.printStackTrace();
        } finally {
            System.out.println("Get Announcement method over");
        }     
    return arrAnn;
    }

    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", name=" + name + ", program=" + program + ", visitCount=" + visitCount + ", gender=" + gender + '}';
    }

}
