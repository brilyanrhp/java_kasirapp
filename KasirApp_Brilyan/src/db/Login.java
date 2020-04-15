/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;
import java.sql.*;
/**
 *
 * @author moo
 */
public class Login {
    Connection con;
    Statement st;
    ResultSet rs;
    
    public void UserLogin(String username, String password){
        Connect conn = new Connect();
        try {
            String sql = "select * from karyawan where username = '"+username+"' and password = '"+password+"'";
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                int baris = rs.getRow();
                System.out.println(baris);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
    }
}
