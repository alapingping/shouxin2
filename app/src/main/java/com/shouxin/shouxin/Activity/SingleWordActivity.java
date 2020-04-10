package com.shouxin.shouxin.Activity;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import  androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.shouxin.shouxin.DataModel.Word;
import com.shouxin.shouxin.R;
import com.shouxin.shouxin.databinding.ActivitySignleWordBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

public class SingleWordActivity extends AppCompatActivity {

    private ActivitySignleWordBinding binding;

    ImageView imageView;
    TextView wordTitle;
    TextView wordDescription;
    Drawable drawable;
    String wordDescriptionCont;

    static{
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

//    Handler handler = new Handler(){
//
//        @Override
//        public void handleMessage(Message msg){
//            imageView.setBackground(drawable);
//            wordDescription.setText(wordDescriptionCont);
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivitySignleWordBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        Word word = (Word) bundle.getSerializable("word");

        binding.wordTitle.setText(word.getName());
        binding.wordDescription.setText(word.getDescription());

        binding.toolbar.setTitle(R.string.menu_back);
        binding.toolbar.setBackgroundColor(getResources().getColor(R.color.colorMintDark));
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.toolbar.setNavigationOnClickListener(view -> SingleWordActivity.this.finish());

        Glide.with(this)
                .load(word.pictureUrl)
                .into(binding.wordImage);
//        wordTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            getNetworkData("A");
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//            }
//        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            this.finish();
        }
        return true;
    }

//    public void getNetworkData(String WordName) throws IOException {
//
//        final String BASE_URL = "https://shouyu.51240.com/";
//        final String POSTFIX = "__shouyus/";
//        String url = BASE_URL + WordName + POSTFIX;
//
//        Document doc = Jsoup.connect(url).timeout(5000).get();
//        Elements pngs = doc.select("img[src$=.png]");
//        png2Drawable(pngs.get(0).attr("src").substring(2));
//
//        Elements titles = doc.select("td[bgcolor=#FFFFFF]");
//        wordDescriptionCont = titles.get(0).text();
//
//        handler.sendEmptyMessage(0);
//
//    }

//    public void png2Drawable(String url){
//
//        url = "http://" + url;
//        try {
//            drawable = (Drawable) Drawable.createFromStream((new URL(url)).openStream(), null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
