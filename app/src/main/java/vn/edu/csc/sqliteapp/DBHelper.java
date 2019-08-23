package vn.edu.csc.sqliteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBHelper {
    String DATABASE_NAME = "qlsv.db";
    private static final String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase db = null;

    Context context;

    public DBHelper(Context context) {
        this.context = context;

        processSQLite();
    }

    private void processSQLite() {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        if(!dbFile.exists()){
            try{
                CopyDatabaseFromAsset();
                Toast.makeText(context, "Copy successful !!!", Toast.LENGTH_SHORT).show();

            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private void CopyDatabaseFromAsset() {
        try{
            InputStream databaseInputStream = context.getAssets().open(DATABASE_NAME);

            String outputStream = getPathDatabaseSystem();

            File file = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if(!file.exists()){
                file.mkdir();
            }

            OutputStream databaseOutputStream = new FileOutputStream(outputStream);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = databaseInputStream.read(buffer)) > 0){
                databaseOutputStream.write(buffer,0,length);
            }


            databaseOutputStream.flush();
            databaseOutputStream.close();
            databaseInputStream.close();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private String getPathDatabaseSystem() {
        return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }


    public ArrayList<SinhVien> getSinhVien(){
        db = context.openOrCreateDatabase(DATABASE_NAME,context.MODE_PRIVATE,null);

      Cursor cursor = db.rawQuery("Select * From SinhVien",null);

        ArrayList<SinhVien> arrayList = new ArrayList<>();

        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int gender = cursor.getInt(2);
            byte[] image = cursor.getBlob(3);
            arrayList.add(new SinhVien(id,name,gender,image));

        }

        return arrayList;
    }
//
    public long insertSinhVien(SinhVien sv){
        db = context.openOrCreateDatabase(DATABASE_NAME,
                                          context.MODE_PRIVATE,
                                            null);

        //"INSERT INTO Student(name,address,gender) VALUES(?,?,?)"

        ContentValues contentValues = new ContentValues();
        contentValues.put("Name",sv.getName());
        contentValues.put("Gender",sv.gender);
        contentValues.put("Image", sv.getImage());

        long result = db.insert("SinhVien",
                                null,
                            contentValues);

        return result;

    }
//
    public long updateSinhVein(SinhVien sv){
        db = context.openOrCreateDatabase(DATABASE_NAME,
                context.MODE_PRIVATE,
                null);

        //"UPDATE Student SET name = ? and image=? Where id = ?"

        ContentValues contentValues = new ContentValues();
        contentValues.put("Name",sv.getName());
        contentValues.put("Gender", sv.getGender());
        contentValues.put("Image", sv.getImage());

        long result = db.update("SinhVien",contentValues,"ID=" + sv.getId(),null);

        return result;

    }

//
    public long deleteSinhVien(int id){

        //"Delete From Student where id = ?"
        db = context.openOrCreateDatabase(DATABASE_NAME,
                context.MODE_PRIVATE,
                null);

        long result = db.delete("SinhVien","ID = " + id,null);

        return result;

    }
}
