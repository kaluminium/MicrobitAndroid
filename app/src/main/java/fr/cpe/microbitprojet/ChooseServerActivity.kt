package fr.cpe.microbitprojet

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException

class ChooseServerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        setContentView(R.layout.choose_server)

        findViewById<Button>(R.id.connect).setOnClickListener {
            val IP = findViewById<EditText>(R.id.serveradress).text.toString() // l'IP du serveur passerel
            val PORT = Integer.parseInt(findViewById<EditText>(R.id.serverport).text.toString()) // Constante arbitraire du sujet
            val isConnected = testUdpConnection(IP, PORT)
            if (isConnected) {
                println("Connexion UDP réussie")
                findViewById<TextView>(R.id.errorconnection).visibility = View.INVISIBLE
                val intent = Intent(this,InfoList::class.java)
            } else {
                findViewById<TextView>(R.id.errorconnection).visibility = View.VISIBLE
                println("Échec de la connexion UDP")
            }
        }
    }

    fun testUdpConnection(serverAddress: String, serverPort: Int): Boolean {
        return try {
            val socket = DatagramSocket()
            socket.soTimeout = 2000 // Timeout de 2 secondes

            val sendData = "ping".toByteArray()
            val address = InetAddress.getByName(serverAddress)

            val sendPacket = DatagramPacket(sendData, sendData.size, address, serverPort)
            socket.send(sendPacket)

            val receiveData = ByteArray(1024)
            val receivePacket = DatagramPacket(receiveData, receiveData.size)

            socket.receive(receivePacket) // Attend une réponse du serveur

            socket.close()

            println("Réponse reçue du serveur: ${String(receivePacket.data, 0, receivePacket.length)}")
            true
        } catch (e: SocketTimeoutException) {
            println("Timeout: aucune réponse du serveur UDP.")
            false
        } catch (e: Exception) {
            println("Erreur: ${e.message}")
            false
        }
    }
}