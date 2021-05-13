package com.example.miwokapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find the view that shows numbers category
        TextView numbers = (TextView) findViewById(R.id.numbers);

        //Set clickListener on view
        numbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create new intent to open the NumbersActivity
                Intent numbersIntent = new Intent(MainActivity.this, NumbersActivity.class);
                //Start newActivity
                startActivity(numbersIntent);
            }
        });

        //Find the view that shows family category
        TextView family = (TextView) findViewById(R.id.family);

        //Set clickListener on view
        family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create new intent to open the FamilyActivity
                Intent familyIntent = new Intent(MainActivity.this, FamilyActivity.class);
                //Start newActivity
                startActivity(familyIntent);
            }
        });

        //Find the view that shows colors category
        TextView colors = (TextView) findViewById(R.id.colors);

        //Set clickListener on view
        colors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create new intent to open the ColorsActivity
                Intent colorsIntent = new Intent(MainActivity.this, ColorsActivity.class);
                //Start newActivity
                startActivity(colorsIntent);
            }
        });

        //Find the view that shows phrases category
        TextView phrases = (TextView) findViewById(R.id.phrases);

        //Set clickListener on view
        phrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create new intent to open the PhrasesActivity
                Intent phrasesIntent = new Intent(MainActivity.this, PhrasesActivity.class);
                //Start newActivity
                startActivity(phrasesIntent);
            }
        });
    }

}