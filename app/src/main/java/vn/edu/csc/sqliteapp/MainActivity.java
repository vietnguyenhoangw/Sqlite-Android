package vn.edu.csc.sqliteapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    DBHelper dbHelper;
    ListView listView;
    ArrayList<SinhVien> arrayList;
    ArrayList<SinhVien> arrayListtmp;

    SinhVienAdapter sinhVienAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper= new DBHelper(MainActivity.this);


        listView = findViewById(R.id.listView);
        arrayList = dbHelper.getSinhVien();
        arrayListtmp = dbHelper.getSinhVien();

        sinhVienAdapter = new SinhVienAdapter(MainActivity.this,arrayList);
        listView.setAdapter(sinhVienAdapter);

        registerForContextMenu(listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<SinhVien> tmp = new ArrayList<>();

                for(SinhVien sv : arrayListtmp){
                    if(sv.name.toLowerCase().contains(s.toLowerCase())){
                        tmp.add(sv);
                    }
                }

                if(tmp.size() > 0) {
                    sinhVienAdapter.clear();
                    sinhVienAdapter.addAll(tmp);
                    sinhVienAdapter.notifyDataSetChanged();
                }
                if(tmp.size()==0){
                    listView.setVisibility(View.GONE);
                }
                if(s.isEmpty()){
                    listView.setVisibility(View.VISIBLE);
                }

                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.mnuInsert){

            Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.image_2);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageSV = stream.toByteArray();

            SinhVien sinhVien = new SinhVien("Vu DInh Ai",1,imageSV );
            if(dbHelper.insertSinhVien(sinhVien) > 0){
                arrayListtmp = dbHelper.getSinhVien();
                sinhVienAdapter.clear();
                sinhVienAdapter.addAll(dbHelper.getSinhVien());
                sinhVienAdapter.notifyDataSetChanged();
            }else {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            }


        }else {
            Collections.sort(arrayList);
            sinhVienAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int ID = arrayList.get(adapterContextMenuInfo.position).getId();

        if(item.getItemId() == R.id.mnuDelete){

            arrayList.remove(adapterContextMenuInfo.position);

            if(dbHelper.deleteSinhVien(ID) > 0){
                Toast.makeText(this, "OKE", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "NOKE", Toast.LENGTH_SHORT).show();
            }

            sinhVienAdapter.notifyDataSetChanged();
            arrayListtmp = dbHelper.getSinhVien();

        }else {
            SinhVien sinhVien = new SinhVien(ID,"abc",1,null);

            if(dbHelper.updateSinhVein(sinhVien) > 0){
                sinhVienAdapter.clear();
                sinhVienAdapter.addAll(dbHelper.getSinhVien());
                sinhVienAdapter.notifyDataSetChanged();

                arrayListtmp = dbHelper.getSinhVien();
            }else {
                Toast.makeText(this, "NOKE", Toast.LENGTH_SHORT).show();
            }
        }


        return super.onContextItemSelected(item);
    }
}