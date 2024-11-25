package uaspbo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Konsumen extends JFrame {
    private JTextField txtId, txtNama;
    private JButton btnAdd, btnUpdate, btnDelete, btnView;

    public Konsumen() {
        setTitle("CRUD Data Konsumen");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("ID Konsumen:"));
        txtId = new JTextField();
        add(txtId);

        add(new JLabel("Nama Konsumen:"));
        txtNama = new JTextField();
        add(txtNama);

        btnAdd = new JButton("Tambah");
        btnUpdate = new JButton("Ubah");
        btnDelete = new JButton("Hapus");
        btnView = new JButton("Lihat");

        add(btnAdd);
        add(btnUpdate);
        add(btnDelete);
        add(btnView);

        // Tambahkan ActionListener untuk tombol
        btnAdd.addActionListener(e -> addKonsumen());
        btnUpdate.addActionListener(e -> updateKonsumen());
        btnDelete.addActionListener(e -> deleteKonsumen());
        btnView.addActionListener(e -> viewKonsumen());

        setVisible(true);
    }

    private void addKonsumen() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO data_konsumen (nama) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtNama.getText());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Konsumen berhasil ditambahkan!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateKonsumen() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE data_konsumen SET nama = ? WHERE id_konsumen = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtNama.getText());
            stmt.setInt(2, Integer.parseInt(txtId.getText()));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Konsumen berhasil diperbarui!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteKonsumen() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM data_konsumen WHERE id_konsumen = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Konsumen berhasil dihapus!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void viewKonsumen() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM data_konsumen";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            StringBuilder result = new StringBuilder("ID Konsumen\tNama Konsumen\n");
            while (rs.next()) {
                result.append(rs.getInt("id_konsumen")).append("\t\t")
                      .append(rs.getString("nama")).append("\n");
            }
            JOptionPane.showMessageDialog(this, result.toString());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new Konsumen();
    }
}

