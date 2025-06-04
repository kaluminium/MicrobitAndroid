package fr.cpe.microbitprojet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.cpe.microbitprojet.models.Info
import fr.cpe.microbitprojet.models.RoomInfo
import fr.cpe.microbitprojet.models.RoomInfoList

class InfoList : AppCompatActivity() {
    private var adapter: InfoAdapter? = null
    private val handler = android.os.Handler()
    private lateinit var updateRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_info_list)

        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val backButton = findViewById<Button>(R.id.backButton)
        val refreshButton = findViewById<Button>(R.id.refresh)

        val items = RoomInfoList.jsonToList(
            ""
        )

        recyclerView.layoutManager = LinearLayoutManager(this)

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                adapter!!.swapItems(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }

            override fun isLongPressDragEnabled(): Boolean {
                return false
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)
        if(adapter == null){
            adapter = InfoAdapter(items, itemTouchHelper)
        }

        refreshRecyclerView()


        adapter!!.setOnItemClickListener { roomInfo: RoomInfo ->
            val intent = Intent(
                this,
                InfoOrderActivity::class.java
            )
            handler.removeCallbacks(updateRunnable)
            intent.putExtra("room_name", roomInfo.roomName)
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        backButton.setOnClickListener {
            handler.removeCallbacks(updateRunnable)
            val intent = Intent(this, ChooseServerActivity::class.java)
            startActivity(intent)
        }

        refreshButton.setOnClickListener {
            refreshRecyclerView()
        }

        updateRunnable = object : Runnable {
            override fun run() {
                refreshRecyclerView()
                handler.postDelayed(this, 2000)
            }
        }

        handler.post(updateRunnable)
    }

    private fun refreshRecyclerView() {
        UdpManager.getInstance().sendMessage("getValues()", { res: String ->
            runOnUiThread {
                val updatedList = RoomInfoList.jsonToList(res)
                adapter!!.compareAndApplyChanges(updatedList)
            }
        }, {
            runOnUiThread {
                //Toast.makeText(this, "Échec de la récupération des données", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateRunnable)
    }

}