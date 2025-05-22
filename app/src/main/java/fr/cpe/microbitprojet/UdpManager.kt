package fr.cpe.microbitprojet

import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException
import kotlin.concurrent.Volatile
import kotlin.concurrent.thread

class UdpManager private constructor() {
    companion object {
        @Volatile
        private var instance: UdpManager? = null // Volatile modifier is necessary

        private var IP = "";//"10.181.142.133" // Remplacer par l'IP de votre interlocuteur
        private var PORT = 0; // Constante arbitraire du sujet
        private var address: InetAddress? = null // Structure Java décrivant une adresse résolue
        private var UDPSocket: DatagramSocket? =
            null // Structure Java permettant d'accéder au réseau (UDP)


        fun getInstance() =
            instance ?: synchronized(this) { // synchronized to avoid concurrency problem
                instance ?: UdpManager().also { instance = it }
            }
    }


    public fun connect(
        serverAddress: String,
        serverPort: Int,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        try {
            IP = serverAddress
            PORT = serverPort
            UDPSocket = DatagramSocket()
            UDPSocket!!.soTimeout = 2000

            sendMessage("ping", onSuccess, onFailure)


        } catch (e: SocketTimeoutException) {
            println("Timeout: aucune réponse du serveur UDP.")

        } catch (e: Exception) {
            println("Erreur: ${e.message}")
        }
    }

    fun onResume() {
        try {
            address = InetAddress.getByName(IP)
            UDPSocket = DatagramSocket(PORT)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun onPause() {
        UDPSocket?.disconnect()
        UDPSocket?.close()
        UDPSocket = null
        address = null
    }

    public fun sendMessage(msg: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val sendData = "ping".toByteArray()
        address = InetAddress.getByName(IP)
        val sendPacket = DatagramPacket(sendData, sendData.size, address, PORT)

        val receiveData = ByteArray(1024)
        val receivePacket = DatagramPacket(receiveData, receiveData.size)

        thread {
            var success = false
            try {
                UDPSocket?.send(sendPacket)

                UDPSocket?.receive(receivePacket) // Attend une réponse du serveur

                UDPSocket?.close()
                success = true
            } catch (e: SocketTimeoutException) {
                println("Timeout: aucune réponse du serveur UDP.")

            } catch (e: Exception) {
                println("Erreur: ${e.message}")
            }
            if (success) {
                onSuccess()
            } else {
                onFailure()
            }
        }


    }

}