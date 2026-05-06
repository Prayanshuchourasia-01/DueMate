package com.prayanshu.billreminder;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class AddBillActivity extends AppCompatActivity {

    TextInputEditText etName, etAmount;

    TextView tvSelectedDate,
            tvSelectedTime;

    Calendar calendar;

    DBHelper dbHelper;

    int selectedYear,
            selectedMonth,
            selectedDay,
            selectedHour,
            selectedMinute;

    int billId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_bill);

        etName =
                findViewById(R.id.etName);

        etAmount =
                findViewById(R.id.etAmount);

        tvSelectedDate =
                findViewById(R.id.tvSelectedDate);

        tvSelectedTime =
                findViewById(R.id.tvSelectedTime);

        Button btnDate =
                findViewById(R.id.btnDate);

        Button btnTime =
                findViewById(R.id.btnTime);

        Button btnSave =
                findViewById(R.id.btnSave);

        dbHelper =
                new DBHelper(this);

        calendar =
                Calendar.getInstance();

        // ===== EDIT MODE =====

        Intent intent = getIntent();

        if (intent.hasExtra("id")) {

            billId = intent.getIntExtra(
                    "id",
                    -1
            );

            String name =
                    intent.getStringExtra(
                            "name"
                    );

            double amount =
                    intent.getDoubleExtra(
                            "amount",
                            0
                    );

            String date =
                    intent.getStringExtra(
                            "date"
                    );

            etName.setText(name);

            etAmount.setText(
                    String.valueOf(amount)
            );

            tvSelectedDate.setText(date);

            btnSave.setText("Update Bill");
        }

        // ===== DATE PICKER =====

        btnDate.setOnClickListener(v -> {

            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(

                            this,

                            (view, year, month, dayOfMonth) -> {

                                selectedYear = year;

                                selectedMonth = month;

                                selectedDay = dayOfMonth;

                                tvSelectedDate.setText(

                                        dayOfMonth + "/"
                                                + (month + 1)
                                                + "/"
                                                + year
                                );
                            },

                            calendar.get(Calendar.YEAR),

                            calendar.get(Calendar.MONTH),

                            calendar.get(Calendar.DAY_OF_MONTH)
                    );

            datePickerDialog.show();
        });

        // ===== TIME PICKER =====

        btnTime.setOnClickListener(v -> {

            TimePickerDialog timePickerDialog =
                    new TimePickerDialog(

                            this,

                            (view, hourOfDay, minute) -> {

                                selectedHour = hourOfDay;

                                selectedMinute = minute;

                                tvSelectedTime.setText(

                                        hourOfDay
                                                + ":"
                                                + minute
                                );
                            },

                            calendar.get(Calendar.HOUR_OF_DAY),

                            calendar.get(Calendar.MINUTE),

                            true
                    );

            timePickerDialog.show();
        });

        // ===== SAVE / UPDATE =====

        btnSave.setOnClickListener(v -> {

            String name =
                    etName.getText()
                            .toString()
                            .trim();

            String amountStr =
                    etAmount.getText()
                            .toString()
                            .trim();

            if (name.isEmpty()
                    || amountStr.isEmpty()) {

                Toast.makeText(

                        this,

                        "Fill all fields",

                        Toast.LENGTH_SHORT

                ).show();

                return;
            }

            double amount =
                    Double.parseDouble(
                            amountStr
                    );

            String dueDate =
                    tvSelectedDate.getText()
                            .toString();

            // ===== UPDATE =====

            if (billId != -1) {

                dbHelper.updateBill(

                        billId,

                        name,

                        amount,

                        dueDate
                );

                Toast.makeText(

                        this,

                        "Bill Updated",

                        Toast.LENGTH_SHORT

                ).show();

            }

            // ===== ADD =====

            else {

                dbHelper.addBill(

                        name,

                        amount,

                        dueDate
                );

                scheduleReminder(name);

                Toast.makeText(

                        this,

                        "Bill Saved",

                        Toast.LENGTH_SHORT

                ).show();
            }

            finish();
        });
    }

    // ===== REMINDER =====

    private void scheduleReminder(
            String billName
    ) {

        Calendar reminderCalendar =
                Calendar.getInstance();

        reminderCalendar.set(

                selectedYear,

                selectedMonth,

                selectedDay,

                selectedHour,

                selectedMinute,

                0
        );

        Intent intent =
                new Intent(
                        this,
                        NotificationReceiver.class
                );

        intent.putExtra(
                "billName",
                billName
        );

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(

                        this,

                        (int) System.currentTimeMillis(),

                        intent,

                        PendingIntent.FLAG_UPDATE_CURRENT
                                |
                                PendingIntent.FLAG_IMMUTABLE
                );

        AlarmManager alarmManager =
                (AlarmManager)
                        getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {

            alarmManager.setExact(

                    AlarmManager.RTC_WAKEUP,

                    reminderCalendar.getTimeInMillis(),

                    pendingIntent
            );
        }
    }
}