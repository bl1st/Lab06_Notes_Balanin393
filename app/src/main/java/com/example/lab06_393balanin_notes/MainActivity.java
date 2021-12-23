package com.example.lab06_393balanin_notes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter <Note> adp;
    int selected_pos;//Selected note position

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adp = new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1);
        ListView lst = findViewById(R.id.lst_notes);
        lst.setAdapter(adp);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_pos = position;
            }
        });
    }

    @Override
    protected void onActivityResult(int RequestCode, int ResultCode, @Nullable Intent data)
    {
        if (data != null){//user pressed save button
            int pos = data.getIntExtra("my-note-index",-1);
            String title = data.getStringExtra("my-note-title");
            String content = data.getStringExtra("my-note-content");

            Note n = adp.getItem(pos);
            n.title = title;
            n.content = content;

            adp.notifyDataSetChanged();//updata list box
        }


        super.onActivityResult(RequestCode, ResultCode, data);
    }

        //Balanin 393 Lab 06 Notes
    public void onButtonCreate_Click(View v)
    {
        Note n = new Note();
        n.title = "New note";
        n.content = "Some content";

        adp.add(n);
        int pos = adp.getPosition(n);

        Intent i = new Intent(this, SecondActivity.class);
        i.putExtra("my-note-index", pos);
        i.putExtra("my-note-title", n.title);
        i.putExtra("my-note-content",n.content );

        startActivityForResult(i, 12345);
    }

    public void onButtonEdit_Click(View v)
    {
        Note n = adp.getItem(selected_pos);
        Intent i = new Intent(this, SecondActivity.class);
        i.putExtra("my-note-index", selected_pos);
        i.putExtra("my-note-title", n.title);
        i.putExtra("my-note-content",n.content);

        startActivityForResult(i, 12345);
    }

    public void onButtonDelete_Click(View v)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Note deleting");
        dialog.setCancelable(true);
        dialog.setMessage("You sure you want to delete this note?");
        dialog.setIcon(R.drawable.picturus);
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                return;
            }
        });
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Note n =  n = adp.getItem(selected_pos);
                adp.remove(n);
                adp.notifyDataSetChanged();//Updating ListView
                dialog.cancel();
            }
        });
        dialog.create().show();
    }
}