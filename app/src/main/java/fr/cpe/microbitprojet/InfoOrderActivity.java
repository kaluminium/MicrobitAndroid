package fr.cpe.microbitprojet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fr.cpe.microbitprojet.models.Info;
import fr.cpe.microbitprojet.models.InfoOrder;

public class InfoOrderActivity extends AppCompatActivity {
    private InfoOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info_order);
        Intent intent = getIntent();
        String roomName = intent.getStringExtra("room_name");
        TextView room = findViewById(R.id.choosenRoom);
        room.setText(roomName);

        RecyclerView recyclerView = findViewById(R.id.infoOrderRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<InfoOrder> orders = new ArrayList<>();
        orders.add(new InfoOrder("Capteur Température", "T", R.drawable.temperature));
        orders.add(new InfoOrder("Capteur Humidité", "H", R.drawable.humidity));
        orders.add(new InfoOrder("Capteur Luminosité", "L", R.drawable.light));
        orders.add(new InfoOrder("Capteur Pression", "P", R.drawable.pressure));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                adapter.swapItems(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter = new InfoOrderAdapter(orders, itemTouchHelper);
        recyclerView.setAdapter(adapter);
        Button buttonValidate = findViewById(R.id.validate);

        buttonValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendValues(String.format("%s:%s", roomName, adapter.getOrder()));
                returnToInfoListActivity();
            }
        });

        Button buttonReturn = findViewById(R.id.retourInfoOrder);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToInfoListActivity();
            }
        });
    }

    private void returnToInfoListActivity(){
        Intent intent = new Intent(this, InfoList.class);
        startActivity(intent);
    }

    private void sendValues(String values) {
        UdpManager.Companion.getInstance().sendMessage(
            values,
            msg -> {return null;},
            () -> {
                runOnUiThread(() -> {});
                return null;
            }
        );
    }
}