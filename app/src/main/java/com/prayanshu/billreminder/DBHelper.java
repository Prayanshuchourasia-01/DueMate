package com.prayanshu.billreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Bills.db";

    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {

        super(
                context,
                DB_NAME,
                null,
                DB_VERSION
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(

                "CREATE TABLE bills(" +

                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +

                        "name TEXT," +

                        "amount REAL," +

                        "dueDate TEXT," +

                        "isPaid INTEGER)"
        );
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        db.execSQL(
                "DROP TABLE IF EXISTS bills"
        );

        onCreate(db);
    }

    // ===== ADD BILL =====

    public void addBill(
            String name,
            double amount,
            String dueDate
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues cv =
                new ContentValues();

        cv.put("name", name);

        cv.put("amount", amount);

        cv.put("dueDate", dueDate);

        cv.put("isPaid", 0);

        db.insert(
                "bills",
                null,
                cv
        );
    }

    // ===== GET ALL BILLS =====

    public Cursor getBills() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(

                "SELECT * FROM bills ORDER BY id DESC",

                null
        );
    }

    // ===== MARK PAID =====

    public void markPaid(int id) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues cv =
                new ContentValues();

        cv.put("isPaid", 1);

        db.update(

                "bills",

                cv,

                "id=?",

                new String[]{
                        String.valueOf(id)
                }
        );
    }

    // ===== DELETE =====

    public void deleteBill(int id) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        db.delete(

                "bills",

                "id=?",

                new String[]{
                        String.valueOf(id)
                }
        );
    }

    // ===== UPDATE BILL =====

    public void updateBill(

            int id,

            String name,

            double amount,

            String dueDate

    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues cv =
                new ContentValues();

        cv.put("name", name);

        cv.put("amount", amount);

        cv.put("dueDate", dueDate);

        db.update(

                "bills",

                cv,

                "id=?",

                new String[]{
                        String.valueOf(id)
                }
        );
    }
}