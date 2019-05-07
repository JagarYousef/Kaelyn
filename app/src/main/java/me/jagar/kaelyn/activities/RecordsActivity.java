package me.jagar.kaelyn.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import me.jagar.kaelyn.R;
import me.jagar.kaelyn.adapters.RecordsAdapter;
import me.jagar.kaelyn.models.Record;
import me.jagar.kaelyn.storage.SQLDatabaseHelper;

import static android.widget.LinearLayout.VERTICAL;

public class RecordsActivity extends AppCompatActivity {

    private FloatingActionButton fabAdd, fabStats;
    private RecyclerView recyclerView;
    private SQLDatabaseHelper sqlDatabaseHelper;
    private ImageView imgNoRecords;

    private RecordsAdapter adapter;
    private List<Record> recordsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        fabAdd = findViewById(R.id.fabAdd);
        fabStats = findViewById(R.id.fabStats);
        recyclerView = findViewById(R.id.recyclerview);
        imgNoRecords = findViewById(R.id.imgNoRecord);
        fabAdd.setOnClickListener(fabAddClickListener);

        recordsList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(RecordsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            sqlDatabaseHelper = new SQLDatabaseHelper(RecordsActivity.this);
            recordsList = sqlDatabaseHelper.getAllRecords();


        }
        fabStats.setOnClickListener(fabStatsClickListener);



        adapter = new RecordsAdapter(this, recordsList, imgNoRecords);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        if (recordsList.size() <= 0)
            imgNoRecords.setVisibility(View.VISIBLE);


    }

    View.OnClickListener fabStatsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (ContextCompat.checkSelfPermission(RecordsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                sqlDatabaseHelper = new SQLDatabaseHelper(RecordsActivity.this);

                final AlertDialog.Builder builder = new AlertDialog.Builder(RecordsActivity.this);
                View content = LayoutInflater.from(RecordsActivity.this).inflate(R.layout.totals_view, null);
                builder.setView(content);
                TextView txtTotalAmount = content.findViewById(R.id.txtTotalAmount);
                TextView txtTotalTime = content.findViewById(R.id.txtTotalTime);
                String totalAmount = "", totalTime = "";
                if (String.valueOf(sqlDatabaseHelper.getTotalAmount()).length() <= 7)
                    totalAmount = sqlDatabaseHelper.getTotalAmount() + " mls";
                else
                    totalAmount = String.valueOf(sqlDatabaseHelper.getTotalAmount()).substring(0, 7) + " mls";
                if (String.valueOf(sqlDatabaseHelper.getTotalTime()).length() <= 7)
                    totalTime = sqlDatabaseHelper.getTotalTime() + " hours";
                else
                    totalTime = String.valueOf(sqlDatabaseHelper.getTotalTime()).substring(0, 7) + " hours";

                txtTotalAmount.setText(totalAmount);
                txtTotalTime.setText(totalTime);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog alertDialog = builder.create();
                        alertDialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationStyle;
                final MediaPlayer mp = MediaPlayer.create(RecordsActivity.this, R.raw.click);
                mp.setVolume(0.05f, 0.05f);
                mp.start();
                alertDialog.show();


            }else{
                Toast.makeText(RecordsActivity.this, "No records yet!", Toast.LENGTH_LONG)
                        .show();
            }
        }
    };
    View.OnClickListener fabAddClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            checkPermissions();

        }
    };

    private void checkPermissions() {
        Dexter.withActivity(RecordsActivity.this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()){
                            showAddRecordDialog();
                        }else {
                            Toast.makeText(RecordsActivity.this, "All permissions should be granted!", Toast.LENGTH_LONG)
                                    .show();
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();
    }

    private void showAddRecordDialog() {

        sqlDatabaseHelper = new SQLDatabaseHelper(RecordsActivity.this);

        AlertDialog.Builder builder = new AlertDialog.Builder(RecordsActivity.this);
        View content = LayoutInflater.from(RecordsActivity.this).inflate(R.layout.record_popup, null);
        builder.setView(content);

        final EditText edtAmount = content.findViewById(R.id.edtAmount);
        final EditText edtTime = content.findViewById(R.id.edtTime);
        final EditText edtRate = content.findViewById(R.id.edtRate);
        final EditText edtNotes = content.findViewById(R.id.edtNotes);
        Button btnCalcualte = content.findViewById(R.id.btnCalculate);
        Button btnSave = content.findViewById(R.id.btnSave);
        final ImageView imgSmile = content.findViewById(R.id.imgSmile);
        final ImageView imgNormal = content.findViewById(R.id.imgNormal);
        final ImageView imgSad = content.findViewById(R.id.imgSad);
        final ImageButton imgBtnCancel = content.findViewById(R.id.imgBtnCancel);
        final AlertDialog alertDialog = builder.create();

        final int[] selected_emo = {-1};

        btnCalcualte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double amount = 0.0;
                double time = 0.0;
                double rate = 0.0;
                if (edtAmount.getText().toString().trim().length() > 0)
                    amount = Double.valueOf(edtAmount.getText().toString().trim());
                if (edtTime.getText().toString().trim().length() > 0)
                    time = Double.valueOf(edtTime.getText().toString().trim());
                if (edtRate.getText().toString().trim().length() > 0)
                    rate = Double.valueOf(edtRate.getText().toString().trim());
                if (amount == 0.0 && (time != 0.0 && rate != 0.0))
                    edtAmount.setText(String.valueOf(time * rate));
                else if (rate == 0.0 && (amount != 0.0 && time != 0.0))
                    edtRate.setText(String.valueOf(amount / time));
                else if (time == 0.0 && (amount != 0.0 && rate != 0.0))
                    edtTime.setText(String.valueOf(amount / rate));
                else if (amount != 0.0 && rate != 0.0 && time != 0.0)
                    Toast.makeText(RecordsActivity.this, "Nothing to calculate!", Toast.LENGTH_SHORT)
                            .show();
                else
                    Toast.makeText(RecordsActivity.this, "At least 2 valuse need to be defined!", Toast.LENGTH_LONG)
                            .show();
            }
        });

        imgBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Toast.makeText(RecordsActivity.this, "The record has been cancelled", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        imgSmile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GradientDrawable selected_shape = new GradientDrawable();
                selected_shape.setColor(getResources().getColor(R.color.gray));
                selected_shape.setCornerRadius(8);
                GradientDrawable unselected_shape = new GradientDrawable();
                unselected_shape.setColor(getResources().getColor(R.color.white));
                unselected_shape.setCornerRadius(0);

                imgSmile.setBackground(selected_shape);
                imgSad.setBackground(unselected_shape);
                imgNormal.setBackground(unselected_shape);

                selected_emo[0] = 0;

            }
        });
        imgSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GradientDrawable selected_shape = new GradientDrawable();
                selected_shape.setColor(getResources().getColor(R.color.gray));
                selected_shape.setCornerRadius(8);
                GradientDrawable unselected_shape = new GradientDrawable();
                unselected_shape.setColor(getResources().getColor(R.color.white));
                unselected_shape.setCornerRadius(0);

                imgSad.setBackground(selected_shape);
                imgSmile.setBackground(unselected_shape);
                imgNormal.setBackground(unselected_shape);

                selected_emo[0] = 2;

            }
        });
        imgNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GradientDrawable selected_shape = new GradientDrawable();
                selected_shape.setColor(getResources().getColor(R.color.gray));
                selected_shape.setCornerRadius(8);
                GradientDrawable unselected_shape = new GradientDrawable();
                unselected_shape.setColor(getResources().getColor(R.color.white));
                unselected_shape.setCornerRadius(0);

                imgNormal.setBackground(selected_shape);
                imgSad.setBackground(unselected_shape);
                imgSmile.setBackground(unselected_shape);
                selected_emo[0] = 1;

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtAmount.getText().toString().trim().length() <= 0){
                    edtAmount.setError("This value is requested!");
                    edtAmount.requestFocus();
                }else if (edtRate.getText().toString().trim().length() <= 0){
                    edtRate.setError("This value is requested!");
                    edtRate.requestFocus();
                }else if (edtTime.getText().toString().trim().length() <= 0){
                    edtTime.setError("This value is requested!");
                    edtTime.requestFocus();
                }else{
                    Record record = new Record();
                    record.setAmount(Double.valueOf(edtAmount.getText().toString().trim()));
                    record.setRate(Double.valueOf(edtRate.getText().toString().trim()));
                    record.setTime(Double.valueOf(edtTime.getText().toString().trim()));
                    if (selected_emo[0] != -1)
                        record.setEmo(selected_emo[0]);
                    else
                        record.setEmo(1);
                    if (edtNotes.getText().toString().trim().length() > 0)
                        record.setNote(edtNotes.getText().toString());
                    else
                        record.setNote("N/A");
                    record.setDate(System.currentTimeMillis());
                    sqlDatabaseHelper.insertRecord(record);
                    alertDialog.dismiss();
                    Toast.makeText(RecordsActivity.this, "The record has been saved successfully"
                    ,Toast.LENGTH_SHORT).show();

                    //Update recyclerView
                    final MediaPlayer mp = MediaPlayer.create(RecordsActivity.this, R.raw.success);
                    mp.setVolume(0.05f, 0.05f);
                    mp.start();
                    List<Record> updatedList = new ArrayList<>();
                    updatedList =sqlDatabaseHelper.getAllRecords();
                    adapter = new RecordsAdapter(RecordsActivity.this, updatedList, imgNoRecords);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                    if (updatedList.size() > 0)
                        imgNoRecords.setVisibility(View.GONE);
                }
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationStyle;
        final MediaPlayer mp = MediaPlayer.create(RecordsActivity.this, R.raw.click);
        mp.setVolume(0.05f, 0.05f);
        mp.start();
        alertDialog.show();

    }
}
