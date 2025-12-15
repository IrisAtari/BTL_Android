package an4.com.example.btl_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    private static final String PREFS_NAME = "CoursePrefs";
    private static final String KEY_SAVED_COURSES = "selectedCourses";
    private SharedCourseViewModel sharedViewModel;
    //private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView fragRvCourses;
    ArrayList<Course> arrayListHocPhan = new ArrayList<>();
    CourseAdapter courseAdapter = null;
    Button btnSaveLocal, btnDeleteLocal, btnCalulateTotal;
    TextView textViewTotal;

    private final String TAG =  "RegFragment";

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void saveCoursesToLocal(ArrayList<Course> courses) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(arrayListHocPhan);

        editor.putString(KEY_SAVED_COURSES, json);
        editor.apply(); // use apply() for background saving

        Toast.makeText(getContext(), "Đã lưu danh sách môn học!", Toast.LENGTH_SHORT).show();
    }
    private ArrayList<Course> loadCoursesFromLocal() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(KEY_SAVED_COURSES, null);

        // If no data is found, return an empty list
        if (json == null) {
            return new ArrayList<>();
        }

        Gson gson = new Gson();
        // Define the type of the data we want to retrieve (ArrayList<Course>)
        Type type = new TypeToken<ArrayList<Course>>() {}.getType();

        ArrayList<Course> loadedList = gson.fromJson(json, type);

        if (loadedList == null) {
            return new ArrayList<>();
        }
        return loadedList;
    }
    private void deleteLocalCourses() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(KEY_SAVED_COURSES);
        editor.apply();

        // Optional: Clear the UI list as well
        arrayListHocPhan.clear();
        courseAdapter.notifyDataSetChanged();

        sharedViewModel.deleteAllCourses();

        Toast.makeText(getContext(), "Đã xóa dữ liệu lưu trữ!", Toast.LENGTH_SHORT).show();
    }

    private void InitUIItems (View view) {

        textViewTotal =  view.findViewById(R.id.textViewTotal);

        btnSaveLocal = view.findViewById(R.id.btnSaveLocal);
        btnSaveLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCoursesToLocal(arrayListHocPhan);
            }
        });

        btnDeleteLocal = view.findViewById(R.id.btnDeleteLocal);
        btnDeleteLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLocalCourses();
            }
        });

        btnCalulateTotal = view.findViewById(R.id.btnCalulateTotal);
        btnCalulateTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float total = 0.0f;
                for (Course course: arrayListHocPhan) {
                    total += 480 * Float.parseFloat(course.getTongTinChi());
                }
                textViewTotal.setText(String.valueOf(total));
            }
        });


        sharedViewModel = new  ViewModelProvider(requireActivity()).get(SharedCourseViewModel.class);

        fragRvCourses =  view.findViewById(R.id.reg_rv_courses);
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

        });
        fragRvCourses.setAdapter(courseAdapter);
        //Log.d("TabFrag", "Init Tab fragment" + view.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<Course> savedData = loadCoursesFromLocal();
        if (!savedData.isEmpty()) {
            arrayListHocPhan.clear();
            arrayListHocPhan.addAll(savedData);
            if(courseAdapter != null) courseAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        saveCoursesToLocal(arrayListHocPhan);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        //loadData();
        InitUIItems(view);
        sharedViewModel.getCourses().observe(getViewLifecycleOwner(), courses -> {
            // Update your local list
            arrayListHocPhan.clear();
            arrayListHocPhan.addAll(courses);
            loadCoursesFromLocal();
            // Refresh the adapter
            if (courseAdapter != null) {
                courseAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}