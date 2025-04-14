package org.example

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader


class Connector(var token: String, var username: String) {

    lateinit var socket : Socket
    lateinit var selectorManager : SelectorManager

    lateinit var receiveChannel : ByteReadChannel
    lateinit var sendChannel : ByteWriteChannel

    fun connectToWebsocket(): Unit {

        val logger = LoggerFactory.getLogger(this::class.java)
        logger.debug("Debug : ", "Connecting to WebSocket...")

        runBlocking {
            selectorManager = SelectorManager(Dispatchers.IO)
            socket = aSocket(selectorManager).tcp().connect("192.168.1.129", 9002)

            receiveChannel = socket.openReadChannel()
            sendChannel = socket.openWriteChannel(autoFlush = true)

            launch(Dispatchers.IO) {
                runBlocking {
                    while (true) {
                        val received = receiveChannel.readUTF8Line()
                        when (received) {
                            "DeviceType" -> {
                                sendChannel.writeStringUtf8("COMPUTER\n")
                            }

                            else -> {
                                println(received)
                            }
                        }
                        if (received != null) {
                            println(received)
                        }
                    }
                }
            }.start()

            while (true){
                println("Message :")
                sendChannel.writeStringUtf8(readln() + '\n')
            }

//            launch(Dispatchers.IO) {        //Exemple de recepteur / envoyeur
//                while (true) {
//                    val greeting = receiveChannel.readUTF8Line()
//                    if (greeting != null) {
//                        println(greeting)
//                    } else {
//                        println("Server closed a connection")
//                        socket.close()
//                        selectorManager.close()
//                        exitProcess(0)
//                    }
//                }
//            }
//
//            while (true) {
//                println("Ecris ou je t'ecris : ")
//                val myMessage = readln()
//                println("myMessage : $myMessage")
//                sendChannel.writeStringUtf8(myMessage + "\n")
//            }


        }

//     return isConnected
    }

    @Deprecated(
        message = "Use connectToWebsocket instead",
        replaceWith = ReplaceWith("connectToWebsocket()"),
        level = DeprecationLevel.ERROR
    )
    suspend fun connectToWebsocketOld(client: HttpClient) {
        client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/login") {
            var incomingText = ""
            var identified = false
            var isWorking = false
            while (true) {
                if (!identified) {
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

                    if (incomingText != "ACK" && !isWorking && incomingText != "") {
                        try {
                            isWorking = true
                            val p = ProcessBuilder(
                                "cmd.exe", "/c", incomingText
                            )
                            p.redirectErrorStream(true)
                            val process = p.start()
                            val r = BufferedReader(InputStreamReader(process.inputStream))
                            var line: String?
                            while (true) {
                                line = r.readLine()
                                if (line == null) {
                                    isWorking = false
                                    break
                                }
                                println(line)
                            }
                        } catch (e: Exception) {
                            println(e.localizedMessage)
                        }
                    }
                }

            }
        }
    }
}

enum class IncorectInfo {
    INVALID_TOKEN,
    USER_NOT_FOUND,
    VALID_USERNAME,
    NONE
}