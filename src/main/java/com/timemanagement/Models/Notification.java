package com.timemanagement.Models;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

public class Notification {

    public void displayTray(String caption, String description) throws AWTException {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");

        TrayIcon trayIcon = new TrayIcon(image, "Tray");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("System tray");
        tray.add(trayIcon);

        trayIcon.displayMessage(caption, description, MessageType.INFO);
    }
}