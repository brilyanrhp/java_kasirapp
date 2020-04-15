/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.*;
import java.util.Calendar;
import javax.swing.JOptionPane;
/**
 *
 * @author moo
 */
public class Connect {
    public Connection con;
    private Statement st;
    private ResultSet rs;
    public boolean cekLogin;
    private boolean admin;
    private String name;
    private int id;
    private int total, id_transaksi, harga_barang;
    public boolean cekError;
    
    public Connect(){
        try {
            // Mengkoneksikan app kita dengan database
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/kasir_app", "root", "");
            System.out.println("Database Terkoneksi");
            
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public void disconnect(){
        try {
            con.close();
            System.out.println("Database Terputus");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    //Database Akun
    
    public void login(String uname, String pw){
        try {
            String sql = "select * from karyawan where username = '"+ uname +"' and password = '"+ pw +"'";
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            
            int cekData = 0;
            while(rs.next()){
                cekData = rs.getRow();
                this.id = rs.getInt("id_karyawan");
            }
            
            if(cekData > 0){
                cekLogin = true;
                
                String sql_tmp = "insert into tmp_karyawan(id_karyawan) values(?)";
                PreparedStatement sta = con.prepareStatement(sql_tmp);
                sta.setInt(1, this.id);
                sta.executeUpdate();
            }else{
                cekLogin = false;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public void register(String nama, String jenis, String username, String password){
        try {
            if(jenis == "admin"){
                this.admin = true;
            }else if (jenis == "pegawai"){
                this.admin = false;
            }
            
            String sql = "insert into karyawan(id_karyawan, nama_karyawan, admin, username, password, last_update) values(?, ?, ?, ?, ?, ?)";
            String sqlID= "select max(id_karyawan) as maks from karyawan";
            
            st = con.createStatement();
            rs = st.executeQuery(sqlID);
            rs.next();
            int id = rs.getInt("maks");
            id = id + 1;
            
            Calendar cal = Calendar.getInstance();
            Timestamp lastupdate = new Timestamp(cal.getTime().getTime());
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, nama);
            ps.setBoolean(3, this.admin);
            ps.setString(4, username);
            ps.setString(5, password);
            ps.setTimestamp(6, lastupdate);
            ps.execute();
            
            
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public void setDataPgw(){
        try {
            String sql="SELECT tmp_karyawan.id_karyawan, karyawan.nama_karyawan, karyawan.admin FROM tmp_karyawan INNER JOIN karyawan WHERE tmp_karyawan.id_karyawan=karyawan.id_karyawan";
            st = con.createStatement();
            rs = st.executeQuery(sql);
            rs.next();
            this.name = rs.getString("nama_karyawan");
            this.admin = rs.getBoolean("admin");
            
        } catch (SQLException e) {
        }
    }
    public String getNama(){
        return this.name;
    }
    public boolean getAdmin(){
        return this.admin;
    }
    
    public void delDataTmp(){
        try {
            PreparedStatement sta = con.prepareStatement("delete from tmp_karyawan");
            sta.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    //Database Barang
    public int getCountData(){
        int rowcount = 0;
        try {
            String q = "select count(*) as jumlah from barang";
            Statement sta = con.createStatement();
            ResultSet rs1 = sta.executeQuery(q);
            rs1.next();
            rowcount = rs1.getInt("jumlah");
        } catch (SQLException e) {
            System.out.println(e);
        }
        return rowcount;
    }
    
    public Object[][] getData(){
        Object[][] row = new Object[1000][4];
        try {
            Statement sta = con.createStatement();
            ResultSet rs1 = sta.executeQuery("select * from barang");
            int i = 0;
            while(rs1.next()){
                row[i][0] = rs1.getString("id_barang");
                row[i][1] = rs1.getString("nama_barang");
                row[i][2] = rs1.getString("harga_barang");
                row[i][3] = rs1.getString("stok");
                i++;
            }
            
        } catch (SQLException e) {
            System.out.println(e);
        }
        return row;
    }
    
    public void insertBarang(String nama, String harga, String stok){
        try {
            Statement sta = con.createStatement();
            ResultSet rs1 = sta.executeQuery("select max(id_barang) as maks from barang");
            rs1.next();
            
            int rowcount = rs1.getInt("maks");
            rowcount = rowcount + 1;
            
            Calendar cal = Calendar.getInstance();
            Timestamp lastupdate = new Timestamp(cal.getTime().getTime());
            
            String q = "insert into barang (id_barang, nama_barang, harga_barang, stok, last_update) values(?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, rowcount);
            ps.setString(2, nama);
            ps.setInt(3, Integer.valueOf(harga));
            ps.setInt(4, Integer.valueOf(stok));
            ps.setTimestamp(5, lastupdate);
            ps.executeUpdate();
            
        } catch (NumberFormatException | SQLException e) {
            System.out.println(e);
        }
    }
    
    public void updateBarang(String id, String nama, String harga, String stok){
        try {
            Calendar cal = Calendar.getInstance();
            Timestamp lastupd = new Timestamp(cal.getTime().getTime());
            
            String q = "update barang set nama_barang = ?, harga_barang = ?, stok = ?, last_update = ? where id_barang = ?";
            PreparedStatement sta = con.prepareStatement(q);
            sta.setString(1, nama);
            sta.setString(2, harga);
            sta.setString(3, stok);
            sta.setTimestamp(4, lastupd);
            sta.setString(5, id);
            sta.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public void delBarang(String id){
        try {
            PreparedStatement sta = con.prepareStatement("delete from barang where id_barang = ?");
            sta.setString(1, id);
            sta.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    // Database Pegawai
    
    public int getCountPgw(){
        int rowcount = 0;
        try {
            Statement sta = con.createStatement();
            ResultSet r = sta.executeQuery("select count(*) as jumlah from karyawan");
            r.next();
            rowcount = r.getInt("jumlah");
        } catch (SQLException e) {
            System.out.println(e);
        }
        return rowcount;
    }
    
    public Object[][] getDataPgw(){
        Object[][] data = new Object[1000][3];
        try {
            Statement s = con.createStatement();
            ResultSet r = s.executeQuery("select * from karyawan");
            int i = 0;
            while(r.next()){
                data[i][0] = r.getString("id_karyawan");
                data[i][1] = r.getString("nama_karyawan");
                data[i][2] = r.getString("admin");
                i++;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return data;
    }
    
    public void delPgw(String id){
        try {
            PreparedStatement sta = con.prepareStatement("delete from karyawan where id_karyawan = ?");
            sta.setString(1, id);
            sta.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public void updatePgw(String id, String nama){
        try {
            Calendar cal = Calendar.getInstance();
            Timestamp lastupd = new Timestamp(cal.getTime().getTime());
            
            String q = "update karyawan set nama_karyawan = ?, last_update = ? where id_karyawan = ?";
            PreparedStatement sta = con.prepareStatement(q);
            sta.setString(1, nama);
            sta.setTimestamp(2, lastupd);
            sta.setString(3, id);
            sta.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    // Database Transaksi
    
    public int getCountTrans(){
        int rowcount = 0;
        try {
            Statement sta = con.createStatement();
            ResultSet r = sta.executeQuery("select count(*) as jumlah from tmp_transaksi");
            r.next();
            rowcount = r.getInt("jumlah");
        } catch (Exception e) {
            System.out.println(e);
        }
        return rowcount;
    }
    
    public Object[][] getDataTrans(){
        Object[][] data = new Object[1000][6];
        try {
            Statement s = con.createStatement();
            ResultSet r = s.executeQuery("SELECT tmp_transaksi.id_transaksi, barang.nama_barang, barang.harga_barang, tmp_transaksi.qty, tmp_transaksi.total, karyawan.nama_karyawan FROM tmp_transaksi INNER JOIN barang ON barang.id_barang = tmp_transaksi.id_barang INNER JOIN karyawan ON karyawan.id_karyawan = tmp_transaksi.id_karyawan");
            int i = 0;
            while(r.next()){
                data[i][0] = r.getString("id_transaksi");
                data[i][1] = r.getString("nama_barang");
                data[i][2] = r.getString("harga_barang");
                data[i][3] = r.getString("qty");
                data[i][4] = r.getString("total");
                data[i][5] = this.name;
                i++;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return data;
    }
    
    public void insertTrans(String nama, String qty){
        try {
            String sql_barang = "select * from barang where nama_barang ='"+ nama +"'";
            String sql_karyawan = "select * from tmp_karyawan";
            String sql_transaksi = "insert into transaksi(id_transaksi, id_barang, id_karyawan, tanggal_transaksi, qty, total) values(?,?,?,?,?,?)";
            
            Statement stb = con.createStatement();
            ResultSet rsb = stb.executeQuery(sql_barang);
            rsb.next();
            
            Statement stk = con.createStatement();
            ResultSet rsk = stk.executeQuery(sql_karyawan);
            rsk.next();
            
            Statement stid = con.createStatement();
            ResultSet rsid = stid.executeQuery("select max(id_transaksi) as maks from transaksi");
            rsid.next();
            this.id_transaksi = rsid.getInt("maks");
            this.id_transaksi += 1;
            
            Calendar cal = Calendar.getInstance();
            Timestamp lastupdate = new Timestamp(cal.getTime().getTime());
            
            this.harga_barang = rsb.getInt("harga_barang");
            this.total = Integer.parseInt(qty) * rsb.getInt("harga_barang");
            
            PreparedStatement ps = con.prepareStatement(sql_transaksi);
            ps.setInt(1, id_transaksi);
            ps.setInt(2, rsb.getInt("id_barang"));
            ps.setInt(3, rsk.getInt("id_karyawan"));
            ps.setTimestamp(4, lastupdate);
            ps.setString(5, qty);
            ps.setInt(6, this.total);
            ps.executeUpdate();
            this.cekError = false;
        } catch (Exception e) {
            this.cekError = true;
        }
    }
    public int getTotal(){
        return this.total;
    }
    public int getIdTrans(){
        return this.id_transaksi;
    }
    public int getHarga(){
        return this.harga_barang;
    }
    
    public void delTmpTrans(){
        try {
            PreparedStatement stb = con.prepareStatement("delete from tmp_transaksi");
            stb.executeUpdate();
        } catch (Exception e) {
        }
    }
    
    public Integer getHargaTotal(){
        Integer harga = 0;
        try {
            Statement sta = con.createStatement();
            ResultSet rst = sta.executeQuery("select sum(total) as tot from tmp_transaksi");
            rst.next();
            harga = rst.getInt("tot");
        } catch (Exception e) {
        }
        return harga;
    }
    
    public void getCombo(){
        try {
            Statement sta = con.createStatement();
            ResultSet rsa = sta.executeQuery("select * from barang");
            while(rsa.next()){
                String cbName = rsa.getString("nama_barang");
            }
        } catch (Exception e) {
        }
    }
}
