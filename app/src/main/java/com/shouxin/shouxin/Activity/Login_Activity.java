package com.shouxin.shouxin.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.shouxin.shouxin.API.Client;
import com.shouxin.shouxin.API.Service;
import com.shouxin.shouxin.DataModel.User;
import com.shouxin.shouxin.Utils.SPHelper;
import com.shouxin.shouxin.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class Login_Activity extends AppCompatActivity {

    private static final int SERVER_ERROR = 1;
    private static final int NO_SUCH_USER = 2;
    private static final int PASSWORD_INCORRECTNESS = 3;
    private static final int LOGIN_SUCCESS = 6;

    private final String serverUrl = "http://39.108.60.40:9050/user";
    private ActivityLoginBinding loginBinding;
    private MyHandler loginHandler ;
    private User currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(LayoutInflater.from(this));
        setContentView(loginBinding.getRoot());

        loginBinding.password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        loginBinding.register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        loginBinding.register.setOnClickListener(v -> {
            Intent intent = new Intent(Login_Activity.this,RegisterActivity.class);
            startActivity(intent);
        });

        loginBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLogin(view);
//                startActivity(new Intent(Login_Activity.this, BottomNavigationActivity.class));
            }
        });
        loginHandler = new MyHandler(this);
        currUser = null;
    }

    public void onClickLogin(View view) {
        String account = loginBinding.uid.getText().toString();
        String password = loginBinding.password.getText().toString();

        login(account,password);
    }

//    public void Login(String url,final String userName,String passWord){
//
//        OkHttpClient client = new OkHttpClient();
//        FormBody.Builder formBuilder = new FormBody.Builder();
//        formBuilder.add("username", userName);
//        formBuilder.add("password", passWord);
//        Request request = new Request.Builder().url(url).post(formBuilder.build()).build();
//        Call call = client.newCall(request);
//        call.enqueue(new Callback()
//        {
//            @Override
//            public void onFailure(Call call, IOException e)
//            {
//                runOnUiThread(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        //showWarnSweetDialog("服务器错误");
//                        Message message =  Message.obtain();
//                        message.what = SERVER_ERROR;
//                        message.obj = "服务器请求出错，请重试";
//                        loginHandler.sendMessage(message);
//                    }
//                });
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException
//            {
//                final String res = response.body().string();
//                runOnUiThread(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        if (res.equals("0"))
//                        {
//                            //showWarnSweetDialog("无此账号,请先注册");
//                            Message message =  Message.obtain();
//                            message.what = NO_SUCH_USER;
//                            message.obj = "此用户不存在";
//                            loginHandler.sendMessage(message);
//                        }
//                        else if(res.equals("1"))
//                        {
//                            //println("密码不正确");
//                            Message message =  Message.obtain();
//                            message.what = PASSWORD_INCORRECTNESS;
//                            message.obj = "密码错误";
//                            loginHandler.sendMessage(message);
//                        }
//                        else//登陆成功
//                        {
//                            //进入UI
//                            Message message =  Message.obtain();
//                            message.what = LOGIN_SUCCESS;
//                            message.obj = "登陆成功";
//                            loginHandler.sendMessage(message);
//                        }
//                    }
//                });
//            }
//        });
//    }

    private void login(String username, String password) {
        Service service = Client.retrofit.create(Service.class);
        Call<ResponseBody> call = service.login(username, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    JSONObject object = new JSONObject(result);
                    int code = object.getInt("code");
                    Message message =  Message.obtain();
                    if (code == 200) {
                        message.what = LOGIN_SUCCESS;
                        message.obj = object.get("msg");
                        currUser = new User();
                        currUser.setName(username);
                        saveUserInfo(currUser);
                        loginHandler.sendMessage(message);
                    } else {
                        message.what = PASSWORD_INCORRECTNESS;
                        message.obj = object.get("msg");
                        loginHandler.sendMessage(message);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("1", "1");
            }

        });
    }

    public void saveUserInfo(User user) {
        SPHelper.saveUserInfo(this, user);
    }

    static class MyHandler extends Handler {
        private WeakReference<Activity> activity;

        public MyHandler(Activity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case SERVER_ERROR:
                    Toast.makeText(activity.get(),msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;
                case NO_SUCH_USER:
                    Toast.makeText(activity.get(),msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;
                case PASSWORD_INCORRECTNESS:
                    Toast.makeText(activity.get(),msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;
                case LOGIN_SUCCESS:
                    Toast.makeText(activity.get(),msg.obj.toString(),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(activity.get(), BottomNavigationActivity.class);
                    activity.get().finish();
                    activity.get().startActivity(intent);
                    break;
                default:
                    break;
            }
        }

    }

}
