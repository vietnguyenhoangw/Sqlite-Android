package vn.edu.csc.sqliteapp;

public class SinhVien implements Comparable<SinhVien>{
    int id;
    String name;
    int gender;
    byte[] image;

    public SinhVien(String name, int gender) {
        this.id = id;
        this.name = name;
        this.gender = gender;
    }

    public SinhVien(String name, int gender, byte[] image) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.image = image;
    }

    public SinhVien(int id, String name, int gender, byte[] image) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public int compareTo(SinhVien o) {
        return name.compareToIgnoreCase(o.name);
    }
}
