package ram.android.myhabittracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ram.android.myhabittracker.data.HabitContract;
import ram.android.myhabittracker.data.HabitTrackerDBHelper;

import static ram.android.myhabittracker.data.HabitContract.HabitEntry.COLUMN_DURATION;
import static ram.android.myhabittracker.data.HabitContract.HabitEntry.COLUMN_HABIT_NAME;

public class MainActivity extends AppCompatActivity {

    HabitTrackerDBHelper dbHelper;
    SQLiteDatabase db;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new HabitTrackerDBHelper(this);
        Button next_button = (Button) findViewById(R.id.next_button);
        next_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText samplePersonEditTextView = (EditText) findViewById(R.id.edit1);
                String inputHabitName = samplePersonEditTextView.getText().toString();
                EditText sampleNumberSignedEditText = (EditText) findViewById(R.id.edit2);
                String value = sampleNumberSignedEditText.getText().toString();
                i = Integer.parseInt(value);
                insertHabit(inputHabitName, i);
                samplePersonEditTextView.setText("");
                sampleNumberSignedEditText.setText("");
            }
        });
        Button read_button = (Button) findViewById(R.id.req_button);
        read_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText samplePersonEditTextView = (EditText) findViewById(R.id.edit3);
                String inputHabitName = samplePersonEditTextView.getText().toString();
                readHabits(inputHabitName);
            }
        });
        Button delete_button = (Button) findViewById(R.id.del_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteEntries();
            }


        });
    }

    //Insert method for making an entry to the habit tracker database
    public void insertHabit(String name, int duration) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HABIT_NAME, name);
        values.put(COLUMN_DURATION, duration);

        db.insert(HabitContract.HabitEntry.TABLE_NAME, "null", values);
        Toast.makeText(MainActivity.this, "Record Inserted Successfully!",
                Toast.LENGTH_LONG).show();
        Button readButton = (Button) findViewById(R.id.req_button);
        readButton.setVisibility(View.VISIBLE);

    }

    //read method that reads habit_name and duration of entries with habit name as gaming
    public void readHabits(String habitName) {
        db = dbHelper.getReadableDatabase();
        String whereClause = COLUMN_HABIT_NAME + " = ?";
        String[] selectionArgs = {habitName};
        String result = "";
        StringBuilder sb = new StringBuilder();
        String[] projection = {
                COLUMN_HABIT_NAME,
                COLUMN_DURATION};
        Cursor c = db.query(
                HabitContract.HabitEntry.TABLE_NAME,
                projection,
                whereClause,
                selectionArgs,
                null,
                null,
                null);
        c.moveToFirst();
        if (c != null) {
            do {
                for (int i = 0; i < c.getColumnCount(); i++) {
                    result = sb.append(" " + c.getString(i)).toString();
                }
            } while (c.moveToNext());
            Log.v("Result of query ", result);
            TextView textview = (TextView) findViewById(R.id.text_view1);
            textview.setText(result);
        }
        c.close();
    }

    //deletes all the entries from the table
    public void deleteEntries() {
        db.delete(HabitContract.HabitEntry.TABLE_NAME, null, null);
        Toast.makeText(MainActivity.this, "Record Deleted Successfully!",
                Toast.LENGTH_LONG).show();
        TextView textview = (TextView) findViewById(R.id.text_view1);
        textview.setText("");
        Button readButton = (Button) findViewById(R.id.req_button);
        readButton.setVisibility(View.INVISIBLE);
    }
}


