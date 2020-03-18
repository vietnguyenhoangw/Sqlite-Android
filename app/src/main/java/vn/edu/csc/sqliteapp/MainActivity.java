package vn.edu.csc.sqliteapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    DBHelper dbHelper;
    ListView listView;
    ArrayList<SinhVien> arrayList;
    ArrayList<SinhVien> arrayListtmp;

    SinhVienAdapter sinhVienAdapter;

    /* Dialog */
    EditText edtUserNameInput;
    RadioGroup genderRadioGroup;
    RadioButton maleRadioBtn;
    RadioButton femaleRadioBtn;

    final int INSERT_TYPE = 0;
    final int UPDATE_TYPE = 1;
    static int TYPE;



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

        // Context menu register
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

    /*
    *  top-bar option menu
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.mnuInsert){
            TYPE = 0;
            craeteDialog(0);
//            Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.image_2);
//
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//            byte[] imageSV = stream.toByteArray();
        } else {
            Collections.sort(arrayList);
            sinhVienAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * Dialog configure
     * */
    private void craeteDialog(final int id) {
        LayoutInflater li = LayoutInflater.from(this);
        final View dialogView = li.inflate(R.layout.user_input_dialog, null);

        final AlertDialog alertDialogBuilder = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setPositiveButton("OK", null)
                .setNegativeButton("Canel", null)
                .create();
        alertDialogBuilder.setView(dialogView);

        /* widget mapping */
        edtUserNameInput = dialogView.findViewById(R.id.edtUserNameInput);
        maleRadioBtn = dialogView.findViewById(R.id.maleRadioBtn);
        femaleRadioBtn = dialogView.findViewById(R.id.femaleRadioBtn);
        genderRadioGroup = dialogView.findViewById(R.id.genderRadioGroup);

        alertDialogBuilder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button =  alertDialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (edtUserNameInput.getText().length() == 0) {
                            edtUserNameInput.setError("Empty name!");
                        }
                        else {
                            int gender;

                            switch (genderRadioGroup.getCheckedRadioButtonId()) {
                                case R.id.maleRadioBtn:
                                    gender = 0;
                                    break;
                                case R.id.femaleRadioBtn:
                                    gender = 1;
                                    break;
                                default:
                                    return;
                            }

                            if (TYPE == INSERT_TYPE) {
                                SinhVien sinhVien = new SinhVien(edtUserNameInput.getText() + "", gender);
                                insertAndUpdateUser(sinhVien);
                            } else {
                                SinhVien sinhVien = new SinhVien(id,edtUserNameInput.getText() + "", gender, null);
                                insertAndUpdateUser(sinhVien);
                            }

                            // when everything is ok, using dismiss.
                            alertDialogBuilder.dismiss();
                        }
                    }
                });
            }
        });

        alertDialogBuilder.show();

        WindowManager.LayoutParams lp = alertDialogBuilder.getWindow().getAttributes();
        lp.dimAmount=1f;
        alertDialogBuilder.getWindow().setAttributes(lp);
        alertDialogBuilder.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
    }

    public void insertAndUpdateUser(SinhVien sinhVien) {
        switch (TYPE) {
            case INSERT_TYPE:
                if(dbHelper.insertSinhVien(sinhVien) > 0){
                    arrayListtmp = dbHelper.getSinhVien();
                    sinhVienAdapter.clear();
                    sinhVienAdapter.addAll(dbHelper.getSinhVien());
                    sinhVienAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                } break;
            case UPDATE_TYPE:
                if(dbHelper.updateSinhVein(sinhVien) > 0){
                    sinhVienAdapter.clear();
                    sinhVienAdapter.addAll(dbHelper.getSinhVien());
                    sinhVienAdapter.notifyDataSetChanged();

                    arrayListtmp = dbHelper.getSinhVien();
                }else {
                    Toast.makeText(this, "Have error, try again.", Toast.LENGTH_SHORT).show();
                } break;
            default:
                return;
        }
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
                Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Have error, try again.", Toast.LENGTH_SHORT).show();
            }

            sinhVienAdapter.notifyDataSetChanged();
            arrayListtmp = dbHelper.getSinhVien();

        } else {
            TYPE = 1;
            craeteDialog(ID);
        }

        return super.onContextItemSelected(item);
    }
}