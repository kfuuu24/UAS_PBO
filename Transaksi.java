package uaspbo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Transaksi extends JFrame {
    private JTextField txtIdTransaksi, txtIdKonsumen, txtIdBarang, txtQuantity, txtTotal;
    private JButton btnAdd, btnUpdate, btnDelete, btnView, btnCalculate;

    public Transaksi() {
        setTitle("CRUD Data Transaksi");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Gunakan BorderLayout untuk fleksibilitas

        // Panel untuk Form Input
        JPanel panelForm = new JPanel(new GridLayout(5, 2, 5, 5));
        panelForm.add(new JLabel("ID Transaksi:"));
        txtIdTransaksi = new JTextField();
        panelForm.add(txtIdTransaksi);

        panelForm.add(new JLabel("ID Konsumen:"));
        txtIdKonsumen = new JTextField();
        panelForm.add(txtIdKonsumen);

        panelForm.add(new JLabel("ID Barang:"));
        txtIdBarang = new JTextField();
        panelForm.add(txtIdBarang);

        panelForm.add(new JLabel("Quantity:"));
        txtQuantity = new JTextField();
        panelForm.add(txtQuantity);

        panelForm.add(new JLabel("Total Biaya:"));
        txtTotal = new JTextField();
        txtTotal.setEditable(false); // Total dihitung otomatis
        panelForm.add(txtTotal);

        add(panelForm, BorderLayout.CENTER); // Tambahkan panel form ke tengah

        // Panel untuk Tombol
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAdd = new JButton("Tambah");
        btnUpdate = new JButton("Ubah");
        btnDelete = new JButton("Hapus");
        btnView = new JButton("Lihat");
        btnCalculate = new JButton("Hitung Total");

        panelButton.add(btnAdd);
        panelButton.add(btnUpdate);
        panelButton.add(btnDelete);
        panelButton.add(btnView);
        panelButton.add(btnCalculate);

        add(panelButton, BorderLayout.SOUTH); // Tambahkan panel tombol ke bawah

        // Tambahkan ActionListener untuk tombol
        btnAdd.addActionListener(e -> addTransaksi());
        btnUpdate.addActionListener(e -> updateTransaksi());
        btnDelete.addActionListener(e -> deleteTransaksi());
        btnView.addActionListener(e -> viewTransaksi());
        btnCalculate.addActionListener(e -> calculateTotal());

        setVisible(true);
    }

    private void addTransaksi() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO data_transaksi (id_konsumen, id_barang, quantity, total_biaya) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(txtIdKonsumen.getText()));
            stmt.setInt(2, Integer.parseInt(txtIdBarang.getText()));
            stmt.setInt(3, Integer.parseInt(txtQuantity.getText()));
            stmt.setDouble(4, Double.parseDouble(txtTotal.getText()));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Transaksi berhasil ditambahkan!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateTransaksi() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE data_transaksi SET id_konsumen = ?, id_barang = ?, quantity = ?, total_biaya = ? WHERE id_transaksi = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(txtIdKonsumen.getText()));
            stmt.setInt(2, Integer.parseInt(txtIdBarang.getText()));
            stmt.setInt(3, Integer.parseInt(txtQuantity.getText()));
            stmt.setDouble(4, Double.parseDouble(txtTotal.getText()));
            stmt.setInt(5, Integer.parseInt(txtIdTransaksi.getText()));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Transaksi berhasil diperbarui!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteTransaksi() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM data_transaksi WHERE id_transaksi = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(txtIdTransaksi.getText()));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Transaksi berhasil dihapus!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void viewTransaksi() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM data_transaksi";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            StringBuilder result = new StringBuilder("ID Transaksi\tID Konsumen\tID Barang\tQuantity\tTotal Biaya\n");
            while (rs.next()) {
                result.append(rs.getInt("id_transaksi")).append("\t\t")
                      .append(rs.getInt("id_konsumen")).append("\t\t")
                      .append(rs.getInt("id_barang")).append("\t\t")
                      .append(rs.getInt("quantity")).append("\t\t")
                      .append(rs.getDouble("total_biaya")).append("\n");
            }
            JOptionPane.showMessageDialog(this, result.toString());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void calculateTotal() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT harga FROM data_barang WHERE id_barang = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(txtIdBarang.getText()));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double harga = rs.getDouble("harga");
                int quantity = Integer.parseInt(txtQuantity.getText());
                double total = harga * quantity;
                txtTotal.setText(String.valueOf(total));
            } else {
                JOptionPane.showMessageDialog(this, "Barang tidak ditemukan!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new Transaksi();
    }
}

