package an4.com.example.btl_android;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

public class SharedCourseViewModel extends ViewModel {
    // MutableLiveData to hold the list of courses
    private final MutableLiveData<ArrayList<Course>> selectedCourses = new MutableLiveData<>(new ArrayList<>());

    // Function to update the list (used by Sender)
    public void setCourses(ArrayList<Course> courses) {
        selectedCourses.setValue(courses);
    }

    // Function to add a single course (optional helper)
    public void addCourse(Course course) {
        ArrayList<Course> currentList = selectedCourses.getValue();
        if (currentList != null) {
            currentList.add(course);
            selectedCourses.setValue(currentList); // Trigger observers
        }
    }
    public void deleteAllCourses() {
        selectedCourses.setValue(new ArrayList<>());
    }
    // Function to get the list (used by Receiver)
    public LiveData<ArrayList<Course>> getCourses() {
        return selectedCourses;
    }
}
