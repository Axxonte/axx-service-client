package org.example

import oshi.SystemInfo

fun main() {

    val hwUUID = SystemInfo().hardware.computerSystem.hardwareUUID
    println(hwUUID)
    val connector = Connector(token = hwUUID, username = "Axx1")


    connector.connectToWebsocket()


}
