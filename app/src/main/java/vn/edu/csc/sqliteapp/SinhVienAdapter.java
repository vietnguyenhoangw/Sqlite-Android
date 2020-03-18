package vn.edu.csc.sqliteapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SinhVienAdapter extends RecyclerView.Adapter<SinhVienAdapter.VH> {

    Context context;
    ArrayList<SinhVien> arrayList;

    public SinhVienAdapter(Context context, ArrayList<SinhVien> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.layout_item,null);

        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {

        SinhVien sinhVien = arrayList.get(i);

        vh.textView.setText(sinhVien.getName());
        vh.textView2.setText(sinhVien.getGender() == 0 ? "Male":"Female");

        if(sinhVien.getImage() == null){
            vh.imageView.setImageResource(R.drawable.ic_launcher_background);
        }else {

            Bitmap bitmap =
                    BitmapFactory.decodeByteArray(sinhVien.getImage(),
                            0,
                            sinhVien.getImage().length);

            vh.imageView.setImageBitmap(bitmap);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    /*
    * using for clear and add
    * like clear(), addAll in List-view.
    * */
    public void swap(ArrayList<SinhVien> datas) {
        arrayList.clear();
        arrayList.addAll(datas);
        notifyDataSetChanged();
    }



    public class VH extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        TextView textView2;

        public VH(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
        }
    }
}
