package an4.com.example.btl_android;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.*;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CRUDActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText editTMaHP, editTTenHP, editTTongTinChi, editTSearchMaHP;
    SearchView searchViewMaHP;
    Button btnAdd,  btnUpdate, btnDelete, btnSearch;
    Spinner spinnerLoaiHP;
    Spinner spinnerHocKy;
    RecyclerView rvCourses;
    String LoaiHP = null;
    String HocKy = null;
    private static final String TAG = "MyActivity";
    ArrayList<Course> arrayListHocPhan = new ArrayList<Course>();
    CourseAdapter courseAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crud);
        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        InitiateUIItems();

        searchViewMaHP.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                //courseAdapter.getFilter().filter(newText);
                return false;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                courseAdapter.getFilter().filter(query);
                Log.d(TAG,"Finish filter with: "+courseAdapter.getAllItems().size());
                for (Course courseInList:  courseAdapter.getAllItems()) {
                    Log.d(TAG, courseInList.getMaHP());
                }
                return false;
            }
        });

        spinnerLoaiHP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoaiHP = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerHocKy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HocKy = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> HocPhan = new HashMap<>();
                HocPhan.put("MaHP", editTMaHP.getText().toString());
                HocPhan.put("TenHP", editTTenHP.getText().toString());
                HocPhan.put("TongTinChi", editTTongTinChi.getText().toString());
                HocPhan.put("LoaiHP",LoaiHP);
                HocPhan.put("HocKy",HocKy);

                db.collection("DanhSachHocPhan").document(editTMaHP.getText().toString())
                        .set(HocPhan)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                MakeToast("Thêm học phần thành công");
                                courseAdapter.notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("DanhSachHocPhan").document(editTMaHP.getText().toString())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                courseAdapter.notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
            }
        });




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void InitiateUIItems() {
        editTMaHP = findViewById(R.id.editTMaHP);
        editTTenHP = findViewById(R.id.editTTenHP);
        editTTongTinChi = findViewById(R.id.editTTongTinChi);
        //editTSearchMaHP = findViewById(R.id.editTSearchMaHP);

        searchViewMaHP = findViewById(R.id.searchViewMaHP);
        searchViewMaHP.setImeOptions(EditorInfo.IME_ACTION_DONE);

        btnAdd =  findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        //btnSearch =  findViewById(R.id.btnSearch);

        spinnerLoaiHP = findViewById(R.id.spinnerLoaiHP);
        ArrayAdapter<CharSequence> arrayAdapterLoaiHP = ArrayAdapter.createFromResource(
                this,
                R.array.loaiHP_array,
                android.R.layout.simple_spinner_item
        );
        arrayAdapterLoaiHP.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerLoaiHP.setAdapter(arrayAdapterLoaiHP);

        spinnerHocKy = findViewById(R.id.spinnerHocKy);
        ArrayAdapter<CharSequence> arrayAdapterHocKy = ArrayAdapter.createFromResource(
                this,
                R.array.hocKy_array,
                android.R.layout.simple_spinner_item
        );
        arrayAdapterHocKy.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerHocKy.setAdapter(arrayAdapterHocKy);

        rvCourses = findViewById(R.id.rv_courses);

        db.collection("DanhSachHocPhan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Course tempCourse = document.toObject(Course.class);
                                arrayListHocPhan.add(tempCourse);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        // LayoutManager: vertical list
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCourses.setLayoutManager(layoutManager);

        // Divider giữa các item
        DividerItemDecoration divider = new DividerItemDecoration(rvCourses.getContext(), DividerItemDecoration.VERTICAL);
        rvCourses.addItemDecoration(divider);

        // Adapter
        courseAdapter = new CourseAdapter(arrayListHocPhan, course -> {
            // Xử lý click: ví dụ hiển thị Toast
            Toast.makeText(CRUDActivity.this, "Chọn: " + course.getMaHP(), Toast.LENGTH_SHORT).show();
            editTMaHP.setText(course.getMaHP());
            editTTenHP.setText(course.getTenHP());
            editTTongTinChi.setText(course.getTongTinChi());
        });
        rvCourses.setAdapter(courseAdapter);
        courseAdapter.notifyDataSetChanged();
    }

    private void MakeToast(String message) {
        Toast t =Toast.makeText(CRUDActivity.this, message, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER,0,0);
        t.show();
    }

}