package com.shouxin.shouxin.Views;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.shouxin.shouxin.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login_Activity extends AppCompatActivity {

    private static final int SERVER_ERROR = 1;
    private static final int NO_SUCH_USER = 2;
    private static final int PASSWORD_INCORRECTNESS = 3;
    private static final int LOGIN_SUCCESS = 6;

    private String serverUrl = "http://39.108.60.40:9050/user";

    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        EditText password_input_view = (EditText)findViewById(R.id.password);
        password_input_view.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        TextView signup_text = (TextView)findViewById(R.id.register);
        signup_text.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        signup_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginBtn = findViewById(R.id.login_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login_Activity.this, CenterActivity.class));
            }
        });
    }


    private Handler loginHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case SERVER_ERROR:
                    Toast.makeText(Login_Activity.this,msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;
                case NO_SUCH_USER:
                    Toast.makeText(Login_Activity.this,msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;
                case PASSWORD_INCORRECTNESS:
                    Toast.makeText(Login_Activity.this,msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;
                case LOGIN_SUCCESS:
                    Toast.makeText(Login_Activity.this,msg.obj.toString(),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Login_Activity.this,ModeChoiceActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    public void onClickLogin(View view) {
        EditText account_text = findViewById(R.id.uid);
        EditText password_text = findViewById(R.id.password);
        String account = account_text.getText().toString();
        String password = password_text.getText().toString();

        Login(serverUrl,account,password);
    }

    public void Login(String url,final String userName,String passWord){

        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username", userName);
        formBuilder.add("password", passWord);
        Request request = new Request.Builder().url(url).post(formBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //showWarnSweetDialog("服务器错误");
                        Message message =  Message.obtain();
                        message.what = SERVER_ERROR;
                        message.obj = "服务器请求出错，请重试";
                        loginHandler.sendMessage(message);
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                final String res = response.body().string();
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (res.equals("0"))
                        {
                            //showWarnSweetDialog("无此账号,请先注册");
                            Message message =  Message.obtain();
                            message.what = NO_SUCH_USER;
                            message.obj = "此用户不存在";
                            loginHandler.sendMessage(message);
                        }
                        else if(res.equals("1"))
                        {
                            //println("密码不正确");
                            Message message =  Message.obtain();
                            message.what = PASSWORD_INCORRECTNESS;
                            message.obj = "密码错误";
                            loginHandler.sendMessage(message);
                        }
                        else//登陆成功
                        {
                            //进入UI
                            Message message =  Message.obtain();
                            message.what = LOGIN_SUCCESS;
                            message.obj = "登陆成功";
                            loginHandler.sendMessage(message);
                        }
                    }
                });
            }
        });
    }


}
