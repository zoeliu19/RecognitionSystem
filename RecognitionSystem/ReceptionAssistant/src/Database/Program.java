/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author Renjini Ramesh
 */
public class Program {

    private int id;
    private String name;

    public Program(int id, String name) {
        this.id = id;
        this.name = name;
    }

    //method to insert program record to database
    public void insert() throws Exception {
        try {
            Connection con = Driver.getConnection();
            PreparedStatement posted = con.prepareStatement("INSERT INTO program (id, name) VALUES('" + id + "', '" + name + "')");
            posted.executeUpdate();

        } catch (Exception e) {
            System.out.println("Exception caught" + e);
            e.printStackTrace();
        } finally {
            System.out.println("Program Insert method over");
        }
    }
}
