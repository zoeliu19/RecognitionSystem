package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

/**
 * This class represents a visit
 * @author Renjini Ramesh
 */
public class Visit {

    private String visitID;
    private Student student;
    private Date date;
    private String reason;
    private java.sql.Date sqlDate;
    private int studentID;
    //adding student fields to visit for display purposes:
    private String studentName;
    private String studentGender;
    private String studentProgram;
    private int studentVisitCount;

    //getter setters
    public String getVisitID() {
        return visitID;
    }

    public void setVisitID(String visitID) {
        this.visitID = visitID;
    }

    public java.sql.Date getSqlDate() {
        return sqlDate;
    }

    public void setSqlDate(java.sql.Date sqlDate) {
        this.sqlDate = sqlDate;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentGender() {
        return studentGender;
    }

    public void setStudentGender(String studentGender) {
        this.studentGender = studentGender;
    }

    public String getStudentProgram() {
        return studentProgram;
    }

    public void setStudentProgram(String studentProgram) {
        this.studentProgram = studentProgram;
    }

    public int getStudentVisitCount() {
        return studentVisitCount;
    }

    public void setStudentVisitCount(int studentVisitCount) {
        this.studentVisitCount = studentVisitCount;
    }

    public String getID() {
        return visitID;
    }

    public void setID(String ID) {
        this.visitID = ID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setStudent(Student student) {
        this.student = student;
        this.studentID = student.getID();
    }

    public void setStudentID(int id) {
        this.studentID = id;
    }

    /**
     * Constructor for Visit method
     *
     */
    public Visit() {
        this.date = new Date();
        sqlDate = new java.sql.Date(date.getTime());
    }

    /**
     * method to insert visit record to the data base
     *
     */
    public void insert() {
        try {
            Connection con = Driver.getConnection();
            PreparedStatement posted = con.prepareStatement("INSERT INTO visit (studentid, date, reason) VALUES('" + student.getID() + "', '" + sqlDate + "', '" + reason + "')");
            posted.executeUpdate();

        } catch (Exception e) {
            System.out.println("Exception caught" + e);
            e.printStackTrace();
        } finally {
            System.out.println("Visit Insert method over");
        }
    }

    @Override
    public String toString() {
        return "Visit{" + "visitID=" + visitID + ", student=" + student + ", date=" + date + ", reason=" + reason + ", sqlDate=" + sqlDate + ", studentID=" + studentID + ", studentName=" + studentName + ", studentGender=" + studentGender + ", studentProgram=" + studentProgram + ", studentVisitCount=" + studentVisitCount + '}';
    }

}
