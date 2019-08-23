package vn.edu.csc.sqliteapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SinhVienAdapter extends ArrayAdapter<SinhVien> {

    Context context;
    ArrayList<SinhVien> arrayList;

    public SinhVienAdapter(Context context, ArrayList<SinhVien> objects) {
        super(context, 0, objects);
        this.context = context;
        this.arrayList = objects;
    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.layout_item,null);

        SinhVien sinhVien = arrayList.get(position);

        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView textView = convertView.findViewById(R.id.textView);
        TextView textView2 = convertView.findViewById(R.id.textView2);

        textView.setText(sinhVien.getName());
        textView2.setText(sinhVien.getGender() == 1 ? "Male":"Female");

        if(sinhVien.getImage() == null){
            imageView.setImageResource(R.drawable.ic_launcher_background);
        }else {

            Bitmap bitmap =
                    BitmapFactory.decodeByteArray(sinhVien.getImage(),
                            0,
                            sinhVien.getImage().length);

            imageView.setImageBitmap(bitmap);
        }


        return convertView;
    }
}
