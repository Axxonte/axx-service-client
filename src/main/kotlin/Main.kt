package org.example

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.InputStreamReader


fun main() {
    val client = HttpClient {
        install(WebSockets)
    }

    runBlocking {
        client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/login") {
            var incomingText = ""
            var identified = false
            var isWorking = false
                while (true){
                    if (!identified){
                        val received = incoming.tryReceive()
                        if (received.isSuccess) {
                            incomingText = (received.getOrNull() as Frame.Text).readText()
                            println(incomingText)
                        }

                        if (incomingText == "identify") {
                            send("computer")
                        }

                        if (incomingText == "ACK") {
                            identified = true
                        }
                    } else {
                        val received = incoming.tryReceive()
                        if (received.isSuccess) {
                            received.getOrNull() as? Frame.Text
                            incomingText = (received.getOrNull() as Frame.Text).readText()
                        } else if (received.isFailure) {
                            incomingText = ""
                        }

                        if (incomingText != "ACK" && !isWorking && incomingText != ""){
                            try{
                                isWorking = true
                                val p = ProcessBuilder(
                                    "cmd.exe", "/c", incomingText
                                )
                                p.redirectErrorStream(true)
                                val process = p.start()
                                val r = BufferedReader(InputStreamReader(process.inputStream))
                                var line : String?
                                while (true){
                                    line = r.readLine()
                                    if (line == null) {
                                        isWorking = false
                                        break
                                    }
                                    println(line)
                                }
                            } catch (e : Exception) {
                                println(e.localizedMessage)
                            }
                        }


                    }

                }

//            while (true) {
//                val otherMessages = incoming.receive() as? Frame.Text ?: continue
//                println(otherMessages.readText())
//                send("computer")
//            }
        }
    }
    client.close()
    println("Connexion fermée")
}