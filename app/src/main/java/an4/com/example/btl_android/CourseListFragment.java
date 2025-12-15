package an4.com.example.btl_android;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btnResetFilter;
    Spinner spinLoaiHP;
    Spinner spinHocKy;
    RecyclerView fragRvCourses;
    //ArrayList<Course> arrayListHocPhanFull = new ArrayList<Course>();
    ArrayList<Course> arrayListHocPhan = new ArrayList<>();
    CourseAdapter courseAdapter = null;

    private final String TAG = "Course list fragment";

    public CourseListFragment() {
        // Required empty public constructor
    }

    public static CourseListFragment newInstance(String param1, String param2) {
        CourseListFragment fragment = new CourseListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void LoadData() {
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

    private void InitUIItems (@NonNull View view) {

        btnResetFilter =  view.findViewById(R.id.btnResetFilter);
        btnResetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadData();
            }
        });

        fragRvCourses =  view.findViewById(R.id.frag_rv_courses);
        // LayoutManager: vertical list
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        fragRvCourses.setLayoutManager(layoutManager);

        // Divider giữa các item
        DividerItemDecoration divider = new DividerItemDecoration(fragRvCourses.getContext(), DividerItemDecoration.VERTICAL);
        fragRvCourses.addItemDecoration(divider);

        // Adapter
        courseAdapter = new CourseAdapter(arrayListHocPhan, course -> {
            // Xử lý click: ví dụ hiển thị Toast
            Toast.makeText(this.getContext(), "Chọn: " + course.getMaHP(), Toast.LENGTH_SHORT).show();
            // TODO: link fragment data on click
        });
        fragRvCourses.setAdapter(courseAdapter);

        spinLoaiHP = view.findViewById(R.id.spinLoaiHP);
        ArrayAdapter<CharSequence> arrayAdapterLoaiHP = ArrayAdapter.createFromResource(
                this.getContext(),
                R.array.loaiHP_array,
                android.R.layout.simple_spinner_item
        );
        arrayAdapterLoaiHP.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinLoaiHP.setAdapter(arrayAdapterLoaiHP);

        spinLoaiHP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FilterCourse(parent.getItemAtPosition(position).toString(), "LoaiHP");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                LoadData();
            }
        });

        spinHocKy = view.findViewById(R.id.spinHocKy);
        ArrayAdapter<CharSequence> arrayAdapterHocKy = ArrayAdapter.createFromResource(
                this.getContext(),
                R.array.hocKy_array,
                android.R.layout.simple_spinner_item
        );
        arrayAdapterHocKy.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinHocKy.setAdapter(arrayAdapterHocKy);

        spinHocKy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FilterCourse(parent.getItemAtPosition(position).toString(), "HocKy");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                LoadData();
            }
        });


        //Log.d("TabFrag", "Init Tab fragment" + view.toString());
    }

    private void FilterCourse(String inputConstrain, String inputType) {

        Log.d(TAG, "Filter = "+ inputConstrain);
        inputConstrain = inputConstrain.toLowerCase();
        if (inputConstrain.isEmpty() || inputType.isEmpty()) {
            LoadData();
            return;
        }
        ArrayList<Course> filterArray  = new ArrayList<>();
        for (Course filterCourse: arrayListHocPhan) {
            if (inputType == "LoaiHP") {
                if (filterCourse.getLoaiHP().toLowerCase().contains(inputConstrain)) {
                    filterArray.add(filterCourse);
                }
            }
            if (inputType == "HocKy")  {
                if (filterCourse.getHocKy().toLowerCase().contains(inputConstrain)) {
                    filterArray.add(filterCourse);
                }
            }
        }
        if(filterArray.isEmpty()) {
            Toast t = Toast.makeText(this.getContext(), "Không tìm thấy học phần", Toast.LENGTH_SHORT);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        } else {
            arrayListHocPhan.clear();
            arrayListHocPhan.addAll(filterArray);
            courseAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);

        LoadData();
        InitUIItems(view);

        return view;
    }
}