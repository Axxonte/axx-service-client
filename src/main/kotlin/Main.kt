package org.example

import oshi.SystemInfo

lateinit var trayIcon: TrayIcon

fun main() {

    val hwUUID = SystemInfo().hardware.computerSystem.hardwareUUID
    println(hwUUID)
    val connector = Connector(token = hwUUID, username = "Axx1")

    trayIcon = TrayIcon("Axx-Client")

    connector.connectToWebsocket()
}
