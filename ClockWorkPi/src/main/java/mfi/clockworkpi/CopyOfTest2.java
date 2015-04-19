package mfi.clockworkpi;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import mfi.clockworkpi.gui.components.AnalogClock;
 
public class CopyOfTest2 extends JFrame implements ActionListener {
 
    private JDesktopPane d_pane;
    private JButton b_close, b_larger, b_smaller, b_dialog;
    private GraphicsDevice device;
 
    public CopyOfTest2() throws HeadlessException {
        this.setLayout(new BorderLayout());
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = ge.getDefaultScreenDevice();
 
        if (device.isFullScreenSupported()){
            setUndecorated(true);
            device.setFullScreenWindow(this);
        }
        b_close = new JButton("Close");
        b_close.setBounds(20, 20, 100, 30);
        b_close.addActionListener(this);
 
        b_larger = new JButton("FullScreen");
        b_larger.setBounds(20, 70, 100, 30);
        b_larger.addActionListener(this);
        b_larger.setEnabled(false);
 
        b_smaller = new JButton("Window");
        b_smaller.setBounds(20, 120, 100, 30);
        b_smaller.addActionListener(this);
 
        b_dialog = new JButton("Show Dialog");
        b_dialog.setBounds(20, 170, 100, 30);
        b_dialog.addActionListener(this);
 
        d_pane = new JDesktopPane();
        d_pane.setBackground(null); // prevent unexpected LaF settings
        d_pane.add(b_close);
        d_pane.add(b_smaller);
        d_pane.add(b_larger);
        d_pane.add(b_dialog);
        
        AnalogClock clock = new AnalogClock();
        clock.setName("clock"); // NOI18N
        clock.setPreferredSize(new java.awt.Dimension(300, 300));
        clock.setBounds(200, 100, 300, 300);
        clock.setType(AnalogClock.TYPE.DARK);
        clock.setSecondPointerVisible(false);
        clock.setAutoType(false);
        d_pane.add(clock);
        
        this.add(d_pane);
    }
 
    @Override public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b_close) {
            device.setFullScreenWindow(null);
            System.exit(0);
        }
        if (e.getSource() == b_larger) {
            b_larger.setEnabled(false);
            b_smaller.setEnabled(true);
            if (isDisplayable()) {
                setVisible(false);
                dispose();
            }
            setUndecorated(true);
            if (!isVisible()) {
                setVisible(true);
            }
            device.setFullScreenWindow(this);
        }
        if (e.getSource() == b_smaller) {
            b_larger.setEnabled(true);
            b_smaller.setEnabled(false);
            device.setFullScreenWindow(null);
            setVisible(false);
            dispose();
            setUndecorated(false);
            setVisible(true);
        }
        if (e.getSource() == b_dialog) {
            // is window in full screen mode?
            if (device.getFullScreenWindow() == null) {
                JOptionPane.showMessageDialog(this, "This is a test dialog.");
            } else { // isFullScreen
                JOptionPane.showInternalMessageDialog(d_pane, "This is a test dialog.");
            }
        }
    }
 
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }
        new CopyOfTest2().setVisible(true);
    }
}