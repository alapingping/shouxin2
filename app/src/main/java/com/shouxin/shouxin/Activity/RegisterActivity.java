package com.shouxin.shouxin.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class RegisterActivity extends AppCompatActivity {

    private static final int SERVER_ERROR = 1;
    private static final int USER_EXISTED = 4;
    private static final int REGISTER_SUCCESS = 5;

    ImageButton photoChoiceButton;

    //所选头像图片路径
    private String photoPath = "";

    //http请求所需url地址
    private String ServerUrl = "http://39.108.60.40:9050/register";

    //返回登陆界面
    TextView back2login;

    //处理子线程中的信息并及时更新界面
    private Handler register_msg_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case SERVER_ERROR:
                    Toast.makeText(RegisterActivity.this,msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;
                case USER_EXISTED:
                    Toast.makeText(RegisterActivity.this,msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;
                case REGISTER_SUCCESS:
                    Toast.makeText(RegisterActivity.this,msg.obj.toString(),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this,BottomNavigationActivity.class);
                    startActivity(intent);
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        back2login = this.findViewById(R.id.back2login);
        back2login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,Login_Activity.class);
                startActivity(intent);
            }
        });

    }

    public void onClickRegister(View view) {
        EditText username_input = (EditText)findViewById(R.id.uid);
        EditText password_input = (EditText)findViewById(R.id.password);
        EditText ensure_password_input = (EditText)findViewById(R.id.confirm_pass);
        //获取输入内容
        final String username = username_input.getText().toString();
        final String password = password_input.getText().toString();
        String ensurePassword = ensure_password_input.getText().toString();
        //判断两次密码输入一致性
        if(!password.equals(ensurePassword)){
            Toast.makeText(this,"两次密码不一致",Toast.LENGTH_LONG).show();
        }
        else if(password.equals("")){
            Toast.makeText(this,"密码不能为空",Toast.LENGTH_LONG).show();
        }
        else if(password.length() > 16){
            Toast.makeText(this,"密码最长为16位",Toast.LENGTH_LONG).show();
        }
        else if(password.equals(ensurePassword)){
            registerToServer(username,password);
        }

    }

    /**
     *
     * this method is to send user's
     * register information to server
     * and receive the response from server
     * @param username :the user's name
     * @param password :the user's password
     *
     */
    public void registerToServer(String username, String password){
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder =  new FormBody.Builder();
        formBuilder.add("username",username);
        formBuilder.add("password",password);
        Request request = new Request.Builder().url(ServerUrl).post(formBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(
            new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Message message =  Message.obtain();
                            message.what = SERVER_ERROR;
                            message.obj = "服务器请求出错，请重试";
                            register_msg_handler.sendMessage(message);
                        }
                    });

                }
                //处理响应信息方法
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String res = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(res.equals("00"))
                            {
                                //println("已被注册");
                                Message message =  Message.obtain();
                                message.what = USER_EXISTED;
                                message.obj = "用户名已存在";
                                register_msg_handler.sendMessage(message);
                            }
                            else if(res.equals("11"))
                            {
                                //println("注册成功");
                                Message message =  Message.obtain();
                                message.what = REGISTER_SUCCESS;
                                message.obj = "密码错误";
                                register_msg_handler.sendMessage(message);
                            }
                        }
                    });
                }//end onResponse
            }//end CallBack()
        );//end enqueue


    }//end register method

    public void onPhotoChoice(View view) {
        getPictureFromAlbum();
    }

    private void getPictureFromAlbum(){
        Intent choosePhotoIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //创建应用选择器
        String choserTitle = "选择应用";
        Intent chosenIntent = Intent.createChooser(choosePhotoIntent,choserTitle);
        //启动选择应用
        startActivityForResult(chosenIntent,1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1001:
                Uri selectedPicturePath = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedPicturePath, filePathColumns, null, null, null);
                cursor.moveToFirst();
                int ColunmIndex = cursor.getColumnIndex(filePathColumns[0]);
                photoPath = cursor.getString(ColunmIndex);
                cursor.close();

                //加载图片
                Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                photoChoiceButton.setImageBitmap(bitmap);

        }

    }

}
