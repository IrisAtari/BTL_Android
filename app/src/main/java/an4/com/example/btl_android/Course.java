package an4.com.example.btl_android;

public class Course {
    private String title;
    private String sub;

    public Course(String title, String sub) {
        this.title = title;
        this.sub = sub;
    }

    public String getTitle() { return title; }
    public String getSub() { return sub; }

    public void setTitle(String title) { this.title = title; }
    public void setSub(String sub) { this.sub = sub; }
}
