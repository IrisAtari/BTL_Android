package an4.com.example.btl_android;

public class Course {
    private String MaHP;
    private String TenHP;
    private String TongTinChi;
    private String LoaiHP;
    private String HocKy;
    public Course() {};
    public Course(String maHP, String tenHP, String tongTinChi, String loaiHP, String hocKy) {
        MaHP = maHP;
        TenHP = tenHP;
        TongTinChi = tongTinChi;
        LoaiHP = loaiHP;
        HocKy = hocKy;
    }
    public Course(String MaHP, String TenHP) {
        this.MaHP = MaHP;
        this.TenHP = TenHP;
    }
    public String getMaHP() { return MaHP; }
    public String getTenHP() { return TenHP; }
    public String getTongTinChi() { return TongTinChi; }
    public String getLoaiHP() { return LoaiHP; }
    public String getHocKy() { return  HocKy; }
    public void setMaHP(String maHP) { this.MaHP = maHP; }
    public void setTenHP(String tenHP) { this.TenHP = tenHP; }
    public void setTongTinChi(String tongTinChi) { this.TongTinChi = tongTinChi;  }
    public void setLoaiHP(String loaiHP) { this.LoaiHP = loaiHP; }
    public void setHocKy(String hocKy) { this.HocKy = hocKy; }
}
