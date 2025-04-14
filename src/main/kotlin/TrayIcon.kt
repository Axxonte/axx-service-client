package org.example

import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import javax.swing.JOptionPane

class TrayIcon(
    name: String,
){
    lateinit var tray : SystemTray
    lateinit var trayIcon : TrayIcon

    init {
        if (!SystemTray.isSupported()) {
            println("System tray not supported!")
        } else {

            tray = SystemTray.getSystemTray()
            val image = Toolkit.getDefaultToolkit().createImage("src/main/resources/icon.png")

            trayIcon = TrayIcon(image, name)
            trayIcon.isImageAutoSize = true

            try {
                tray.add(trayIcon)
            } catch (e: Exception) {
                println("Failed to add tray icon: ${e.message}")
            }
        }
    }

    fun showNotification(title: String, message: String) {

        try {
            println("Message : ${message}")
            trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO)
//            JOptionPane.showMessageDialog(null, "$title\n$message")
        } catch (e: Exception) {
            println("Failed to show notification: ${e.message}")
        }
    }
}
