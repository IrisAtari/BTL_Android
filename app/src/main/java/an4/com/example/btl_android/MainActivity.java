package an4.com.example.btl_android;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvCourses;
    private TextView tvViewAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar (nếu có trong layout)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Nếu muốn ẩn icon back, giữ như mặc định hoặc customize title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        TextView title = findViewById(R.id.title);
        title.setText("Lịch Học");

        tvViewAll = findViewById(R.id.view_all_small);

        rvCourses = findViewById(R.id.rv_courses);

        // Sample data
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Phát triển ứng dụng di động", "Tự chọn - Học Kỳ 7"));
        courses.add(new Course("Lập trình Java nâng cao", "Tự chọn - Học Kỳ 7"));
        courses.add(new Course("Đồ hoạ máy tính", "Tự chọn - Học Kỳ 5"));
        courses.add(new Course("Xác suất thống kê", "Tự chọn - Học Kỳ 5"));
        courses.add(new Course("Thuật toán nâng cao", "Bắt buộc - Học Kỳ 5"));
        // Bạn có thể nạp dữ liệu thật từ API hoặc DB ở đây

        // LayoutManager: vertical list
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCourses.setLayoutManager(layoutManager);

        // Divider giữa các item
        DividerItemDecoration divider = new DividerItemDecoration(rvCourses.getContext(), DividerItemDecoration.VERTICAL);
        rvCourses.addItemDecoration(divider);

        // Adapter
        CourseAdapter adapter = new CourseAdapter(courses, course -> {
            // Xử lý click: ví dụ hiển thị Toast
            Toast.makeText(MainActivity.this, "Chọn: " + course.getTitle(), Toast.LENGTH_SHORT).show();
        });
        rvCourses.setAdapter(adapter);

        // Xử lý "Xem tất cả"
        tvViewAll.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Xem tất cả học phần", Toast.LENGTH_SHORT).show();
            // TODO: mở activity/fragment danh sách đầy đủ
        });
    }
}