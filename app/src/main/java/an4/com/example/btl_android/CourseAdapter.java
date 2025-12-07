package an4.com.example.btl_android;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.VH> implements Filterable {
    public interface OnItemClick {
        void onClick(Course course);
    }

    private ArrayList<Course> items;
    private ArrayList<Course> allItems;
    private OnItemClick listener;

    public List<Course> getAllItems() {
        return allItems;
    }

    public void setAllItems(ArrayList<Course> allItems) {
        this.allItems = allItems;
    }

    public CourseAdapter(ArrayList<Course> items, OnItemClick listener) {
        this.items = items;
        this.listener = listener;
        this.allItems = items;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Course c = items.get(position);
        String title = c.getMaHP()+"-"+c.getTenHP();
        holder.tvTitle.setText(title);
        String subText = c.getLoaiHP()+"-"+c.getTongTinChi()+"-"+c.getHocKy();
        holder.tvSub.setText(subText);

        holder.card.setOnClickListener(v -> {
            if (listener != null) listener.onClick(c);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public Filter getFilter() {
        return courseFilter;
    }
    private final Filter courseFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Course> filteredArray = new ArrayList<>();
            if (constraint == null || constraint.length() ==  0) {
                filteredArray.addAll(allItems);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Course item : allItems) {
                    if (item.getMaHP().toLowerCase().contains(filterPattern)) {
                        filteredArray.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredArray;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items.clear();
            items.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSub;
        CardView card;

        VH(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSub = itemView.findViewById(R.id.tv_sub);
            card = (CardView) itemView;
        }
    }
}
