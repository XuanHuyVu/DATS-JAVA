package GTS;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.*;


public class GUI_insertTS extends JFrame implements MouseListener, ActionListener{
    private JTextField tfSoBD, tfHoten, tfTongD;
    private JComboBox<String> cbNganhH;
    private JRadioButton rbMale, rbFemale;
    private JButton btAdd;
    private JTable tb;
    private DefaultTableModel dfModel;
    
    public GUI_insertTS() {
        setTitle("DATS");
        setSize(1000,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        BuildGUI();
    }
    
    private void BuildGUI() {
        JPanel pnLeft = new JPanel(new GridBagLayout());
        pnLeft.setBorder(new EmptyBorder(10,10,10,10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        //SoBD
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lbSoBD = new JLabel("Số báo danh: ");
        pnLeft.add(lbSoBD,gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        tfSoBD = new JTextField();
        tfSoBD.setPreferredSize(new Dimension(300,30));
        pnLeft.add(tfSoBD,gbc);
        
        //Hoten
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lbHoten = new JLabel("Họ tên: ");
        pnLeft.add(lbHoten,gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        tfHoten = new JTextField();
        tfHoten.setPreferredSize(new Dimension(300,30));
        pnLeft.add(tfHoten,gbc);
        
        //GT
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lbGT = new JLabel("Giới tính: ");
        pnLeft.add(lbGT,gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        ButtonGroup bgGT = new ButtonGroup();
        rbMale = new JRadioButton("Nam");
        bgGT.add(rbMale);
        rbFemale = new JRadioButton("Nữ");
        bgGT.add(rbFemale);
        JPanel pnGT = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnGT.add(rbMale);
        pnGT.add(rbFemale);

        pnLeft.add(pnGT, gbc);
        //NganhH
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lbNganhH = new JLabel("Ngành học: ");
        pnLeft.add(lbNganhH,gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        cbNganhH = new JComboBox<> (new String[] {"Trí tuệ nhân tạo","Cơ khí","Công trình thủy"});
        pnLeft.add(cbNganhH,gbc);
        
        //TongD
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel lbTongD = new JLabel("Tổng điểm: ");
        pnLeft.add(lbTongD,gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        tfTongD = new JTextField();
        tfTongD.setPreferredSize(new Dimension(300,30));
        pnLeft.add(tfTongD,gbc);
        
        //btAdd
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JPanel pnLeftBottom = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        btAdd = new JButton("Thêm thí sinh mới");
        btAdd.addActionListener(this);
        pnLeftBottom.add(btAdd);
        pnLeft.add(pnLeftBottom,gbc);
        
        JPanel pnLeftContainer = new JPanel(new BorderLayout());
        pnLeftContainer.add(pnLeft,BorderLayout.NORTH);
        
        //table
        
        JPanel pnRight = new JPanel(new GridLayout(1,1));
        String[] headers = {"Số báo danh", "Họ tên", "Giới tính", "Ngành học", "Tổng điểm", "Học bổng"};
        dfModel = new DefaultTableModel(headers,0);
        tb = new JTable(dfModel);
        pnRight.add(new JScrollPane(tb));
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,pnLeftContainer,pnRight);
        splitPane.setDividerLocation(300);
        this.add(splitPane);
        
        loadData(dfModel);
    }
    
    public static void main(String[] args) {
        new GUI_insertTS().setVisible(true);
        XLTS xl = new XLTS();
        xl.getCon();
    }
    
    private void loadData(DefaultTableModel dfModel) {
        try {
            XLTS xl = new XLTS();
            ResultSet res = xl.getTS();
            dfModel.setRowCount(0);
            if(res != null) {
                while (res.next()) {                    
                    Thisinh ts = new Thisinh(
                        res.getString("SoBD"),
                        res.getString("Hoten"),
                        res.getString("GT"),
                        res.getString("NganhH"),
                        res.getInt("TongD")
                    );
                    
                    String scholarShip = ts.Hocbong();
                    
                    dfModel.addRow(new Object[]{
                        ts.getSoBD(),
                        ts.getHoten(),
                        ts.getGT(),
                        ts.getNganhH(),
                        ts.getTongD(),
                        scholarShip
                    });
                }
            }
            dfModel.fireTableDataChanged();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addTS() {
        String SoBD = tfSoBD.getText().trim();
        String Hoten = tfHoten.getText().trim();
        String gender = "";
        int point = 0;
        
        if(rbMale.isSelected()) {
            gender = rbMale.getText();
        } else {
            gender = rbFemale.getText();
        }
        
        String NganhH = cbNganhH.getSelectedItem().toString();
        point = Integer.parseInt(tfTongD.getText().trim());
        
        XLTS xl = new XLTS();
        boolean res = xl.insetTS(new Thisinh(SoBD, Hoten, gender, NganhH, point));
        if(res) {
            loadData(dfModel);
            JOptionPane.showMessageDialog(null, "Them thanh cong!");
        } else {
            JOptionPane.showMessageDialog(null, "Them that bai!");
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btAdd) {
            addTS();
        }
    }
}
