package com.shouxin.shouxin.Views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import  android.support.v7.widget.Toolbar;

import com.shouxin.shouxin.DataModel.ItemEntry;
import com.shouxin.shouxin.R;

public class SingleWordActivity extends AppCompatActivity {


    static{
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signle_word);

        Bundle bundle = getIntent().getExtras();
        ItemEntry word = (ItemEntry) bundle.getSerializable("word");

        ImageView imageView = findViewById(R.id.wordImage);
        imageView.setImageResource(R.drawable.p6);

        TextView wordTitle = findViewById(R.id.wordTitle);
        wordTitle.setText(word.getName());

        TextView wordDescription = findViewById(R.id.wordDescription);
        wordDescription.setText(word.getDescription());

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_back);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorMintDark));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingleWordActivity.this.finish();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            this.finish();
        }
        return true;
    }

}
