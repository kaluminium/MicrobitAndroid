package fr.cpe.microbitprojet

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import kotlin.concurrent.thread
import androidx.core.content.edit

class ChooseServerActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        setContentView(R.layout.choose_server)

        sharedPref = getSharedPreferences("myCache", Context.MODE_PRIVATE)

        findViewById<EditText>(R.id.serveradress).setText(sharedPref.getString("server_adress", ""))
        findViewById<EditText>(R.id.serverport).setText(sharedPref.getString("server_port", ""))

        findViewById<Button>(R.id.connect).setOnClickListener {
            val IP = findViewById<EditText>(R.id.serveradress).text.toString() // l'IP du serveur passerel
            val PORT = Integer.parseInt(findViewById<EditText>(R.id.serverport).text.toString()) // Constante arbitraire du sujet
            testUdpConnection(IP, PORT)
        }
    }
    fun connIsSuccess(){
        println("Connexion UDP réussie")
        findViewById<TextView>(R.id.errorconnection).visibility = View.INVISIBLE
        val intent = Intent(this,InfoList::class.java)
        startActivity(intent)
    }

    fun connHasFailed(){
        findViewById<TextView>(R.id.errorconnection).visibility = View.VISIBLE
        println("Échec de la connexion UDP")
    }

    fun testUdpConnection(serverAddress: String, serverPort: Int) {

        UdpManager.getInstance().connect(serverAddress, serverPort, {
            runOnUiThread { connIsSuccess() }
            sharedPref.edit {
                putString("server_adress", serverAddress)
                putString("server_port", serverPort.toString())
                }
            }, {
            runOnUiThread { connHasFailed() }
        })
    }
}