package com.shouxin.shouxin.Views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import  android.support.v7.widget.Toolbar;

import com.shouxin.shouxin.DataModel.ItemEntry;
import com.shouxin.shouxin.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

public class SingleWordActivity extends AppCompatActivity {

    ImageView imageView;
    TextView wordTitle;
    TextView wordDescription;
    Drawable drawable;
    String wordDescriptionCont;

    static{
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg){
            imageView.setBackground(drawable);
            wordDescription.setText(wordDescriptionCont);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signle_word);

        Bundle bundle = getIntent().getExtras();
        ItemEntry word = (ItemEntry) bundle.getSerializable("word");

        imageView = findViewById(R.id.wordImage);

        wordTitle = findViewById(R.id.wordTitle);
        wordTitle.setText(word.getName());
        wordTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getNetworkData("A");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        wordDescription = findViewById(R.id.wordDescription);
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

    public void getNetworkData(String WordName) throws IOException {

        final String BASE_URL = "https://shouyu.51240.com/";
        final String POSTFIX = "__shouyus/";
        String url = BASE_URL + WordName + POSTFIX;

        Document doc = Jsoup.connect(url).timeout(5000).get();
        Elements pngs = doc.select("img[src$=.png]");
        png2Drawable(pngs.get(0).attr("src").substring(2));

        Elements titles = doc.select("td[bgcolor=#FFFFFF]");
        wordDescriptionCont = titles.get(0).text();

        handler.sendEmptyMessage(0);

    }

    public void png2Drawable(String url){

        url = "http://" + url;
        try {
            drawable = (Drawable) Drawable.createFromStream((new URL(url)).openStream(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
