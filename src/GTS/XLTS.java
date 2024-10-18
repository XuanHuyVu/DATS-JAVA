package GTS;

import java.sql.*;

public class XLTS implements IThisinh {
    private static Connection cn;
    
    @Override
    public void getCon() {
        if(cn == null) {
            try {
                cn = DriverManager.getConnection("jdbc:sqlserver://XUY\\SQLEXPRESS;database=DLTS;user=sa;password=12345678;trustServerCertificate=true;");
                System.out.println("Connected.");
            } catch (Exception e) {
                System.out.println("Not connect.");
            }
        }
    }
    
    @Override
    public ResultSet getTS() {
        getCon();
        try {
            Statement st = cn.createStatement();
            return st.executeQuery("SELECT * FROM tbThisinh");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean insetTS(Thisinh ts) {
        getCon();
        try {
            PreparedStatement pst = cn.prepareStatement("INSERT INTO tbThisinh (SoBD, Hoten, GT, NganhH, TongD) VALUES (?, ?, ?,?,?)");
            pst.setString(1, ts.getSoBD());
            pst.setString(2, ts.getHoten());
            pst.setString(3, ts.getGT());
            pst.setString(4, ts.getNganhH());
            pst.setInt(5, ts.getTongD());
            int res = pst.executeUpdate();
            return res>0;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}
