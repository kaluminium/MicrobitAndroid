package fr.cpe.microbitprojet;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
import fr.cpe.microbitprojet.models.RoomInfo;
import fr.cpe.microbitprojet.models.RoomInfoList;

public class InfoList extends AppCompatActivity {
    private InfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info_list);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Button button = findViewById(R.id.button);

        List<RoomInfo> items = RoomInfoList.jsonToList("{\n" +
                "    \"id1\":{\n" +
                "        \"T\":\"1\",\n" +
                "        \"L\": \"1\",\n" +
                "        \"P\":\"1\",\n" +
                "        \"H\":\"1\"\n" +
                "    },\n" +
                "    \"id2\":{\n" +
                "        \"T\":\"1\",\n" +
                "        \"L\": \"1\",\n" +
                "        \"P\":\"1\",\n" +
                "        \"H\":\"1\"\n" +
                "    }\n" +
                "}");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
        adapter = new InfoAdapter(items, itemTouchHelper);

        adapter.setOnItemClickListener(roomInfo -> {
            Intent intent = new Intent(this, InfoOrderActivity.class);
            intent.putExtra("room_name", roomInfo.getRoomName());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Info info = new Info("yes", "yes", "yes", "yes");
                adapter.updateItem("Salle 2", info);
            }
        });
    }
}