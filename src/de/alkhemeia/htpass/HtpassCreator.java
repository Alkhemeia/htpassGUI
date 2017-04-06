/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.alkhemeia.htpass;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author alkhemeia
 */
public class HtpassCreator {

    private static HtpassCreatorGui gui = Main.getGui();

    //Textfields from GUI
    private static JTextField txtUser = gui.getTxtUser();
    private static JPasswordField txtPass1 = gui.getTxtPass1();
    private static JPasswordField txtPass2 = gui.getTxtPass2();
    private static JTextField txtPath = gui.getTxtPath();
    private static JTextField txtCommandOutput = gui.getTxtCommandOutput();

    //Textareas from GUI
    private static JTextArea txtFileOutput = gui.getTxtFileOutput();

    //Radiobuttons from GUI
    private static JRadioButton rbAdd = gui.getRbAdd();
    private static JRadioButton rbCreate = gui.getRbCreate();
    private static JRadioButton rbDelete = gui.getRbDelete();

    //Buttons from GUI
    private static JButton btnDo = gui.getBtnDo();
    private static JButton btnGetPath = gui.getBtnGetPath();

    private static String command = "";

// Linux Shell
    private static void linuxCmd(String cmd) throws IOException {
        ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
        Process p = builder.start();

        BufferedReader out = null;
        BufferedReader err = null;

        String s = "";

        out = new BufferedReader(new InputStreamReader(p.getInputStream()));
        err = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        while ((s = out.readLine()) != null) {
            System.out.println(s);
            JOptionPane.showMessageDialog(null, s, "Information", JOptionPane.PLAIN_MESSAGE);
        }

        while ((s = err.readLine()) != null) {
            System.out.println(s);
            JOptionPane.showMessageDialog(null, s, "Information", JOptionPane.PLAIN_MESSAGE);
        }

    }
// Start Linux Commands

    private static void doCommand(String path, String user, String password) throws IOException {
        if (rbDelete.isSelected()) {
            linuxCmd(command + " " + path + " " + user);
        } else {
            linuxCmd(command + " " + path + " " + user + " " + password);
        }
    }

//    private static void passwdCreateFile(String path, String user, String password) throws IOException {
//        String param = path + " " + user + " " + password;
//        linuxCmd("htpasswd -c -b " + param);
//    }
//
//    private static void passwdDeleteUser(String path, String user) throws IOException {
//        String param = path + " " + user;
//        linuxCmd("htpasswd -D " + param);
//
//    }
    // Starting htpasswd
    public static void doHtpasswd(String user, String pass, String path) {
        if (doFieldsFilledCheck() && doPasswordMatchingCheck()) {
            try {
                doCommand(path, user, pass);
                FileOutput(path);
            } catch (IOException ex) {
                Logger.getLogger(HtpassCreator.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
//            if (rbAdd.isSelected()) {
//                try {
//                    passwdAddUser(path, user, pass);
//                } catch (IOException ex) {
//                    Logger.getLogger(HtpassCreator.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//
//            if (rbCreate.isSelected()) {
//                try {
//                    passwdCreateFile(path, user, pass);
//                } catch (IOException ex) {
//                    Logger.getLogger(HtpassCreator.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//
//            if (rbDelete.isSelected()) {
//                try {
//                    passwdDeleteUser(path, user);
//                } catch (IOException ex) {
//                    Logger.getLogger(HtpassCreator.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }

        }
    }

    // Checking if passwords are the same
    private static Boolean doPasswordMatchingCheck() {
        if (!rbDelete.isSelected()) {

            String Pass1 = txtPass1.getText();
            String Pass2 = txtPass2.getText();

            if (!Pass1.equals(Pass2)) {
                JOptionPane.showMessageDialog(null, "Passwords do not match!", "Error!", JOptionPane.INFORMATION_MESSAGE);
            }

            return Pass1.equals(Pass2);
        } else {
            return true;
        }
    }

    // Checking if neccesarry Fields are filled
    private static Boolean doFieldsFilledCheck() {

        Boolean filled = true;

        if (txtUser.getText().equals("")) {
            txtUser.setBackground(Color.red);
            filled = false;
        } else {
            txtUser.setBackground(Color.white);
        }
        if (!gui.getRbDelete().isSelected()) {
            if (txtPass1.getText().equals("")) {
                txtPass1.setBackground(Color.red);
                filled = false;
            } else {
                txtPass1.setBackground(Color.white);
            }

            if (txtPass2.getText().equals("")) {
                txtPass2.setBackground(Color.red);
                filled = false;
            } else {
                txtPass2.setBackground(Color.white);
            }
        }
        if (txtPath.getText().equals("")) {
            txtPath.setBackground(Color.red);
            filled = false;
        } else {
            txtPath.setBackground(Color.white);
        }

        return filled;
    }

    // File selection at GUI
    public static void filePathFinder() {
        String filePath = "";
        if (rbCreate.isSelected()) {
            try {
                filePath = chooseSaveFile().getAbsolutePath();
                txtPath.setText(filePath);
            } catch (NullPointerException npe) {
            }
        } else {
            try {
                filePath = chooseOpenFile().getAbsolutePath();
                txtPath.setText(filePath);
                FileOutput(filePath);
            } catch (NullPointerException npe) {
            }
        }
        updateCommandOutputTextield();
    }

    private static File chooseOpenFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileHidingEnabled(false);
        chooser.showOpenDialog(null);
        File file = chooser.getSelectedFile();
        return file;
    }

    private static File chooseSaveFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileHidingEnabled(false);
        chooser.showSaveDialog(null);
        File file = chooser.getSelectedFile();
        return file;
    }

    // On GUI initialisation
    public static void initGui() {
        rbAdd.setSelected(true);
        btnDo.setText("Add User");
        command = "htpasswd -b";
        updateCommandOutputTextield();
        rbCreate.setSelected(false);
        rbDelete.setSelected(false);
    }
    // Actions if Buttons clicked

    public static void btnDoActionPerformed(java.awt.event.ActionEvent evt) {
        String user = txtUser.getText();
        String pass1 = txtPass1.getText();
        String path = txtPath.getText();

        doHtpasswd(user, pass1, path);
    }

    // Actions if Radiobuttons clicked
    public static void rbAddActionPerformed(java.awt.event.ActionEvent evt) {
        btnDo.setText("Add User");
        command = "htpasswd -b";
        updateCommandOutputTextield();
        rbAdd.setSelected(true);
        rbCreate.setSelected(false);
        rbDelete.setSelected(false);

        txtPass1.setEnabled(true);
        txtPass1.setBackground(Color.white);
        txtPass2.setEnabled(true);
        txtPass2.setBackground(Color.white);
        updateCommandOutputTextield();
    }

    public static void rbCreateActionPerformed(java.awt.event.ActionEvent evt) {
        btnDo.setText("Create File");
        command = "htpasswd -c -b";
        rbCreate.setSelected(true);
        rbAdd.setSelected(false);
        rbDelete.setSelected(false);

        txtPass1.setEnabled(true);
        txtPass1.setBackground(Color.white);
        txtPass2.setEnabled(true);
        txtPass2.setBackground(Color.white);
        updateCommandOutputTextield();
    }

    public static void rbDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        btnDo.setText("Delete User");
        command = "htpasswd -D";
        rbDelete.setSelected(true);
        rbAdd.setSelected(false);
        rbCreate.setSelected(false);

        txtPass1.setEnabled(false);
        txtPass1.setBackground(Color.lightGray);
        txtPass2.setEnabled(false);
        txtPass2.setBackground(Color.lightGray);
        updateCommandOutputTextield();
    }

    //Textfields key released
    public static void txtUserKeyReleased(java.awt.event.KeyEvent evt) {
        updateCommandOutputTextield();
    }

    public static void txtPass1KeyReleased(java.awt.event.KeyEvent evt) {
        updateCommandOutputTextield();
    }

    public static void txtPass2KeyReleased(java.awt.event.KeyEvent evt) {
        updateCommandOutputTextield();
    }

    public static void txtPathKeyReleased(java.awt.event.KeyEvent evt) {
        updateCommandOutputTextield();
    }

    private static void FileOutput(String path) {
        String text = null;
        File file = new File(path);
        if (file.exists() && !file.isDirectory()) {
            try {
                FileReader reader = new FileReader(file);
                char[] chars = new char[(int) file.length()];
                reader.read(chars);
                text = new String(chars);
                reader.close();
                txtFileOutput.setText(text);
                if (file.exists() && file.length() == 0) {

                    int optionPane = JOptionPane.showConfirmDialog(null, "File is empty, delete it?", "Delete File?", JOptionPane.YES_NO_OPTION);
                    if (optionPane == JOptionPane.YES_OPTION) {
                        file.delete();
                        if (file.exists()) {
                            JOptionPane.showMessageDialog(null, "Could not delete file!", "Error!", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "File deleted!", "Deleted!", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void updateCommandOutputTextield() {
        String user = txtUser.getText();
        String pass1 = txtPass1.getText();
        String path = txtPath.getText();
        String commandOutput = "Command: something went wrong";
        String passOut = "";

        for (int i = 0; i < pass1.length(); i++) {
            passOut = passOut + "*";
        }

        if (rbDelete.isSelected()) {
            commandOutput = "Command: " + command + " " + path + " " + user;
        } else {
            commandOutput = "Command: " + command + " " + path + " " + user + " " + passOut;
        }
        txtCommandOutput.setText(commandOutput);
    }
}
