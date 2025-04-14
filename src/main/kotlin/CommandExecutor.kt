package org.example

import java.io.BufferedReader
import java.io.InputStreamReader


enum class CommandID {
    SHUTDOWN,
    REBOOT,
    LOCK,
    PING,
    UNKNOWN_COMMAND;

    companion object {
        fun byNameIgnoreCaseOrNull(input: String): CommandID {
            val result = entries.firstOrNull { it.name.equals(input, true) }
            if (result == null) {
                return UNKNOWN_COMMAND
            } else {
                return result
            }
        }
    }
}

fun executeCommand(command: String): String {
    return try {
        val process = Runtime.getRuntime().exec(command)
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val output = reader.readText()
        reader.close()
        return output
    } catch (e: Exception) {
        "Erreur : ${e.message}"
    }
}

fun executeById(id: CommandID): String {
    return when (id) {
        CommandID.SHUTDOWN -> {
            trayIcon.showNotification("Axx-Client", "Arret du système demandé ...")
            executeCommand("shutdown /s /t 0")
        }

        CommandID.REBOOT -> {
            trayIcon.showNotification("Axx-Client", "Redémarrage du système demandé ...")
            executeCommand("shutdown /r /t 0")
        }

        CommandID.LOCK -> {
            trayIcon.showNotification("Axx-Client", "Vérouilage du système demandé ...")
            executeCommand("rundll32.exe user32.dll, LockWorkStation")
        }

        CommandID.PING -> {
            trayIcon.showNotification("Axx-Client", "Connexion OK")
            "PONG"
        }

        CommandID.UNKNOWN_COMMAND -> {
            trayIcon.showNotification("Axx-Client", "Commande inconnue")
            "Commande inconnue"
        }
    }
}