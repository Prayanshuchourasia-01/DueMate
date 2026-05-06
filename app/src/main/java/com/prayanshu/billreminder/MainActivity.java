package com.prayanshu.billreminder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    FloatingActionButton btnAdd;

    TextView tvEmpty;

    DBHelper dbHelper;

    ArrayList<Bill> billList;

    BillAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // ===== NOTIFICATION PERMISSION =====

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(

                        this,

                        new String[]{
                                Manifest.permission.POST_NOTIFICATIONS
                        },

                        101
                );
            }
        }

        // ===== INITIALIZE =====

        recyclerView =
                findViewById(R.id.recyclerView);

        btnAdd =
                findViewById(R.id.btnAdd);

        tvEmpty =
                findViewById(R.id.tvEmpty);

        dbHelper =
                new DBHelper(this);

        recyclerView.setLayoutManager(

                new LinearLayoutManager(this)
        );

        // ===== ADD BUTTON =====

        btnAdd.setOnClickListener(v -> {

            Intent intent =
                    new Intent(

                            MainActivity.this,

                            AddBillActivity.class
                    );

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {

        super.onResume();

        loadBills();
    }

    private void loadBills() {

        billList = new ArrayList<>();

        Cursor cursor =
                dbHelper.getBills();

        while (cursor.moveToNext()) {

            Bill bill = new Bill(

                    cursor.getInt(0),

                    cursor.getString(1),

                    cursor.getDouble(2),

                    cursor.getString(3),

                    cursor.getInt(4)
            );

            billList.add(bill);
        }

        // ===== EMPTY STATE =====

        if (billList.isEmpty()) {

            tvEmpty.setVisibility(View.VISIBLE);

            recyclerView.setVisibility(View.GONE);

        } else {

            tvEmpty.setVisibility(View.GONE);

            recyclerView.setVisibility(View.VISIBLE);
        }

        // ===== ADAPTER =====
        adapter = new BillAdapter(

                this,

                billList,

                dbHelper,

                recyclerView,

                tvEmpty
        );

        recyclerView.setAdapter(adapter);
    }
}