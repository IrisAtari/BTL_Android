package an4.com.example.btl_android;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.VH>{
    public interface OnItemClick {
        void onClick(Course course);
    }

    private List<Course> items;
    private OnItemClick listener;

    public CourseAdapter(List<Course> items, OnItemClick listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Course c = items.get(position);
        holder.tvTitle.setText(c.getMaHP());
        String subText = c.getTenHP()+"-"+c.getTongTinChi()+"-"+c.getHocKy();
        holder.tvSub.setText(subText);

        holder.card.setOnClickListener(v -> {
            if (listener != null) listener.onClick(c);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

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
