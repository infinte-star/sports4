package com.sports.sportclub.UI.UI.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sports.sportclub.DataModel.User;
import com.sports.sportclub.R;
import com.sports.sportclub.api.BmobService;
import com.sports.sportclub.api.Client;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private User current_user = null;
    private String username;
    private String password;
    private JSONObject jsonObject = null;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private List<String> mPermissionList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化bmob服务
        Bmob.initialize(this, "5fad9f2543ffa83e56155a46398d6ede");
        /*
        * 验证本地缓存用户
        * 若用户存在，则免登陆
        * 否则需用户输入登陆信息
        */
//        current_user = BmobUser.getCurrentUser();
//        if(current_user != null){
//            jump2main();
//        }

            //设置下划线
            TextView forget_text = findViewById(R.id.forget_text);
            forget_text.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            //设置监听
            forget_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(LoginActivity.this,"该功能未开放",Toast.LENGTH_LONG).show();
                }
            });

            TextView signup_text = findViewById(R.id.register_text);
            signup_text.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            signup_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                    startActivity(intent);
                }
            });
        }


    //登陆按钮的跳转
    public void onClickSignin(View view) {
        EditText username_input = findViewById(R.id.username_input);
        EditText password_input = findViewById(R.id.password_input);

        username = username_input.getText().toString();
        password = password_input.getText().toString();

        //使用retrofit实现登录请求
        BmobService service = Client.retrofit.create(BmobService.class);
        Call<ResponseBody> call = service.getUser(username,password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200){
                    showmsg("登陆成功");
                    try {
                        String str =  response.body().string();
                        jsonObject = new JSONObject(str);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (Build.VERSION.SDK_INT >= 23) {
                        for (int i = 0; i < PERMISSIONS_STORAGE.length; i++) {
                            int checkCallPhonePermission = ContextCompat.checkSelfPermission(LoginActivity.this, PERMISSIONS_STORAGE[i]);
                            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                                mPermissionList.add(PERMISSIONS_STORAGE[i]);
                            }
                        }
                        if (mPermissionList.size() > 0) {
                            ActivityCompat.requestPermissions(LoginActivity.this,
                                    PERMISSIONS_STORAGE, 222);
                        } else {
                            jump2main(jsonObject);
                        }

                    }

                }
                else if(response.code() == 400) {
                    showmsg("用户名或密码错误");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showmsg(t.getMessage());
            }
        });




        //bmob内部封装的登陆方法
//        current_user = new BmobUser();
//        current_user.setPassword(password);
//        current_user.setUsername(userEmail);
//        current_user.login(new SaveListener<BmobUser>() {
//
//            @Override
//            public void done(BmobUser user, BmobException e) {
//                if(e == null){
//                    showmsg("登陆成功");
//                    jump2main();
//                }
//                else{
//                    showmsg(e.getMessage().toString());
//                }
//            }
//        });

    }

    public boolean Validation(User user){

        return false;
    }

    //显示信息
    public void showmsg(String msg){
        Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_LONG).show();
    }
    //跳转至主界面
    public void jump2main(JSONObject jsonObject){

        try {
            //Object id = jsonObject.get("objectId");
            Object token = jsonObject.get("sessionToken");
            Object user_name = jsonObject.get("username");
            if(jsonObject == null)
                return;
            current_user = new User();
            current_user.setUsername(user_name.toString());
            current_user.setSessionToken(token.toString());
            current_user.setPassword(password);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        Intent intent = new Intent(this, navigationActivity.class);
        //intent.putExtra("username",username);
        intent.putExtra("user",current_user);
        startActivity(intent);
        //verifyStoragePermissions(this);
        finish();

    }

//    public static void verifyStoragePermissions(Activity activity) {
//        int permission = ActivityCompat.checkSelfPermission(activity,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE);
//        }
//    }

    /**
     * 该方法判定获取权限的结果
     * 若失败，则不能开启定位
     * 若成功，则正确开启定位
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            //就像onActivityResult一样这个地方就是判断你是从哪来的。
            case 222:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    jump2main(jsonObject);
                } else {
                    // Permission Denied
                    Toast.makeText(LoginActivity.this, "相机开启失败", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
