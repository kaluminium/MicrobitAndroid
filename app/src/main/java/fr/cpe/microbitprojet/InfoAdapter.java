package fr.cpe.microbitprojet;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.cpe.microbitprojet.models.Info;
import fr.cpe.microbitprojet.models.RoomInfo;


public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.InfoViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(RoomInfo item);
    }


    private final List<RoomInfo> itemList;
    private ItemTouchHelper touchHelper;
    private OnItemClickListener listener;


    public InfoAdapter(List<RoomInfo> itemList, ItemTouchHelper touchHelper) {
        this.itemList = itemList;
        this.touchHelper = touchHelper;
    }

    public void setItemList(List<RoomInfo> newList){
        itemList.clear();
        itemList.addAll(newList);
    }

    public class InfoViewHolder extends RecyclerView.ViewHolder {
        TextView room;
        TextView humidity;
        TextView temperature;
        TextView light;
        TextView pressure;

        public InfoViewHolder(View itemView) {
            super(itemView);
            room = itemView.findViewById(R.id.room);
            humidity = itemView.findViewById(R.id.infoAttribute);
            temperature = itemView.findViewById(R.id.temp);
            light = itemView.findViewById(R.id.light);
            pressure = itemView.findViewById(R.id.pressure);
        }
    }

    @Override
    public InfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info, parent, false);
        return new InfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InfoViewHolder holder, int position) {
        RoomInfo item = itemList.get(position);

        holder.room.setText(item.getRoomName());
        holder.light.setText(item.getLight());
        holder.temperature.setText(item.getTemperature());
        holder.humidity.setText(item.getHumidity());
        holder.pressure.setText(item.getPressure());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void swapItems(int fromPosition, int toPosition) {
        Collections.swap(itemList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void updateItem(String room, Info newValue) {
        int position = this.getRoomPosition(room);
        Log.d("test", String.format("position : %s", position));
        if(position == -1) return;
        RoomInfo roomInfo = itemList.get(position);
        roomInfo.changeInfo(newValue);
        notifyItemChanged(position);
    }

    public int getRoomPosition(String room){
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getRoomName().equals(room)) {
                return i;
            }
        }
        return -1;
    }

    private void removeItem(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItem(String room) {
        int position = getRoomPosition(room);
        if(position == -1) return;
        removeItem(position);
    }

    public void addItem(RoomInfo item) {
        itemList.add(item);
        notifyItemInserted(itemList.size() - 1);
    }

    public void compareAndApplyChanges(List<RoomInfo> newList) {
        for (RoomInfo newItem : newList) {
            int position = getRoomPosition(newItem.getRoomName());
            if (position != -1) {
                RoomInfo existingItem = itemList.get(position);
                if (!existingItem.equals(newItem)) {
                    existingItem.changeInfo(newItem.getInfo());
                    notifyItemChanged(position);
                }
            } else {
                addItem(newItem);
            }
        }
        List<RoomInfo> copy = new ArrayList<>(itemList);
        for (RoomInfo existingItem : copy) {
            boolean found = false;
            for (RoomInfo newItem : newList) {
                if (existingItem.getRoomName().equals(newItem.getRoomName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                removeItem(existingItem.getRoomName());
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


}

