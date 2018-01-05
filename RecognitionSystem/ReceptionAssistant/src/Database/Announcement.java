package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * This class represents an announcement
 *
 * @author Renjini Ramesh
 */
public class Announcement {

    private int id;
    private String program;
    private String announcement;

    /**
     * This is a constructor to initialize an announcement object
     *
     * @param program
     * @param announcement
     */
    public Announcement(String program, String announcement) {
        this.program = program;
        this.announcement = announcement;
    }

    /**
     * This is an empty constructor
     */
    public Announcement() {

    }

    /**
     * Getter method for ID
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Setter method for ID
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter method for program
     *
     * @param program for this announcement
     */
    public void setProgram(String program) {
        this.program = program;
    }

    /**
     * Getter method for program
     *
     * @return program
     */
    public String getProgram() {
        return program;
    }

    /**
     * Setter method for announcement
     *
     * @param announcement
     */
    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    /**
     * Getter method for announcement
     *
     * @return announcement
     */
    public String getAnnouncement() {
        return announcement;
    }

    /**
     * Method to insert announcement into database
     */
    public void insert() {
        try {
            Connection con = Driver.getConnection();
            PreparedStatement posted = con.prepareStatement("INSERT INTO announcement (program, announcement) VALUES('" + program + "', '" + announcement + "')");
            posted.executeUpdate();

        } catch (Exception e) {
            System.out.println("Exception caught" + e);
            e.printStackTrace();
        } finally {
            System.out.println("Announcement Insert method over");
        }
    }

    /**
     * Override the toString method
     *
     * @return
     */
    @Override
    public String toString() {
        return announcement;
    }

}
