package an4.com.example.btl_android;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    Spinner spinLoaiHP;
    Spinner spinHocKy;
    RecyclerView rvCourses;
    ArrayList<Course> arrayListHocPhan = new ArrayList<Course>();
    CourseAdapter courseAdapter = null;

    private final String TAG = "Course list fragment";

    public CourseListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseListFragment newInstance(String param1, String param2) {
        CourseListFragment fragment = new CourseListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        loadData();

        rvCourses = rvCourses.findViewById(R.id.rv_courses);
        // LayoutManager: vertical list
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        rvCourses.setLayoutManager(layoutManager);

        // Divider giữa các item
        DividerItemDecoration divider = new DividerItemDecoration(rvCourses.getContext(), DividerItemDecoration.VERTICAL);
        rvCourses.addItemDecoration(divider);

        // Adapter
        courseAdapter = new CourseAdapter(arrayListHocPhan, course -> {
            // Xử lý click: ví dụ hiển thị Toast
            Toast.makeText(this.getContext(), "Chọn: " + course.getMaHP(), Toast.LENGTH_SHORT).show();

        });
        rvCourses.setAdapter(courseAdapter);

        spinLoaiHP = spinLoaiHP.findViewById(R.id.spinLoaiHP);
        ArrayAdapter<CharSequence> arrayAdapterLoaiHP = ArrayAdapter.createFromResource(
                this.getContext(),
                R.array.loaiHP_array,
                android.R.layout.simple_spinner_item
        );
        arrayAdapterLoaiHP.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinLoaiHP.setAdapter(arrayAdapterLoaiHP);

        spinHocKy = spinHocKy.findViewById(R.id.spinHocKy);
        ArrayAdapter<CharSequence> arrayAdapterHocKy = ArrayAdapter.createFromResource(
                this.getContext(),
                R.array.loaiHP_array,
                android.R.layout.simple_spinner_item
        );
        arrayAdapterLoaiHP.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinHocKy.setAdapter(arrayAdapterLoaiHP);

    }
    private void loadData() {
        db.collection("DanhSachHocPhan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            arrayListHocPhan.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Course tempCourse = document.toObject(Course.class);
                                arrayListHocPhan.add(tempCourse);
                            }
                            courseAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_list, container, false);
    }
}