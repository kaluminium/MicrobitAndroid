package fr.cpe.microbitprojet;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import fr.cpe.microbitprojet.models.InfoOrder;

public class InfoOrderAdapter extends RecyclerView.Adapter<InfoOrderAdapter.InfoOrderViewHolder> {

    private final List<InfoOrder> itemList;
    private ItemTouchHelper touchHelper;


    public InfoOrderAdapter(List<InfoOrder> itemList, ItemTouchHelper touchHelper) {
        this.itemList = itemList;
        this.touchHelper = touchHelper;

    }

    public class InfoOrderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView nameView;

        public InfoOrderViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.infoImage);
            nameView = itemView.findViewById(R.id.infoAttribute);

            itemView.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchHelper.startDrag(this);
                }
                return false;
            });
        }
    }

    @Override
    public InfoOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_info, parent, false);
        return new InfoOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InfoOrderViewHolder holder, int position) {
        InfoOrder item = itemList.get(position);
        holder.nameView.setText(item.getName());
        holder.imageView.setImageResource(item.getImageId());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void swapItems(int fromPosition, int toPosition) {
        Collections.swap(itemList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public String getOrder(){
        StringBuilder order = new StringBuilder();
        for(InfoOrder infoOrder : itemList){
            order.append(infoOrder.getId());
        }
        return order.toString();
    }
}
