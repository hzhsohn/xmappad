package android.zh.xmappad;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.zh.xmappad.update.VersionInfo;
import android.zh.xmappad.update.VersionListener;

import java.io.File;

import ext.func.AssertAlert;
import service.ProcessMonitorService;

public class MainActivity extends AppCompatActivity {

    private final int PERMISSION_ID=667;
    final String FLAG="MainActivity";
    Context _cxt;
    VersionInfo ver;
    //
    EditText txthost;
    EditText txtport;
    EditText txtuser;
    EditText txtpasswd;
    EditText txtcompany;
    Button btnImg;
    Button btnSave;
    ImageView imgIcon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _cxt=this;

        //默认不能打键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //按键
        btnImg=(Button)findViewById(R.id.btnImg);
        btnImg.setOnClickListener(btnImg_click);
        btnSave=(Button)findViewById(R.id.btn1);
        btnSave.setOnClickListener(btn1_click);
        imgIcon=(ImageView)findViewById(R.id.imageView);
        //输入框
        txthost=(EditText)findViewById(R.id.editText4);
        txtport=(EditText)findViewById(R.id.editText3);
        txtuser=(EditText)findViewById(R.id.editText);
        txtpasswd=(EditText)findViewById(R.id.editText2);
        txtcompany=(EditText)findViewById(R.id.editComp);

        //---------------------------------
        //存储器权限
        if(0==checkPermission(PERMISSION_ID , Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
            //--------------------------------
            //检测更新
            ver=new VersionInfo(MainActivity.this, new VersionListener() {

                @Override
                public void web_fail_cb() {

                }

                @Override
                public void is_need_update_cb(boolean b) {

                }

                @Override
                public void download_ok() {
                    installProcess();
                }

            },"http://127.0.0.1/android_ver.txt");
        }


        //-----------------------------
        //
        //启用后台服务
        //
        //APK图标是隐藏的
        Intent intent = new Intent(_cxt, ProcessMonitorService.class);
        startService(intent) ;

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadInfo();
    }

    void loadInfo()
    {
        //获取图像
        String stricon = ModifyIcon.getUserIconLocalPath(this,ModifyIcon.uid);
        File fp = new File(stricon);
        if (fp.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(stricon);
            //将图片显示到ImageView中
            if (null != bm) {
                imgIcon.setImageBitmap(bm);
            } else {
                imgIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.def_user));
            }
        } else {
            imgIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.def_user));
        }
    }

    //更改图片
    View.OnClickListener btnImg_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //跳到下一个窗体
            Intent intent = new Intent(_cxt, ModifyIcon.class);
            Bundle bundle = new Bundle();//该类用作携带数据
            intent.putExtras(bundle);//附带上额外的数据
            //带返回结果
            startActivityForResult(intent, 1000);
            overridePendingTransition(R.anim.in_0, R.anim.in_1);

            //关闭键盘
            InputMethodManager imm = (InputMethodManager) getSystemService(_cxt.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            }
        }
    };

    //保存信息
    View.OnClickListener btn1_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(txthost.getText().equals(""))
            {

            }
            if(txtport.getText().equals(""))
            {

            }
            if(txtuser.getText().equals(""))
            {

            }
            if(txtpasswd.getText().equals(""))
            {

            }
            if(txtcompany.getText().equals(""))
            {

            }

            /*
            //跳到下一个窗体
            Intent intent = new Intent(_cxt, AccountSafe.class);
            Bundle bundle = new Bundle();//该类用作携带数据
            intent.putExtras(bundle);//附带上额外的数据
            //带返回结果
            startActivityForResult(intent, 1000);
            overridePendingTransition(R.anim.in_0, R.anim.in_1);
            */

            //关闭键盘
            InputMethodManager imm = (InputMethodManager) getSystemService(_cxt.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            }
        }
    };


    /*
     * return 1权限重新请求通过
     *        2权限被永久拒绝,要到设置里面手动设置
     *        0已经拥有该权限
     * */
    int  checkPermission(int deniedRebackID,String Manifest_permission_xxx) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest_permission_xxx) != PackageManager.PERMISSION_GRANTED) {

            //请求权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest_permission_xxx},
                    deniedRebackID);

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest_permission_xxx)) {
                return 1;
            } else {
                Log.v("checkPermission",Manifest_permission_xxx+" don't ask again");
                return 2;
            }
        }
        return 0;
    }

    //回调
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //摄像头
                    checkPermission(PERMISSION_ID+1 ,Manifest.permission.CAMERA);
                } else {
                    //请求权限被拒绝
                    AssertAlert.show(this, R.string.alert, R.string.no_strong_permission_do_myself, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
                            startActivity(intent);
                            //退出APP
                            System.exit(0);
                        }
                    });
                }
                break;
            }
            case PERMISSION_ID+1:
                break;
        }
    }

    //安装应用的流程
    private void installProcess() {
        boolean haveInstallPermission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先获取是否有安装未知来源应用的权限
            haveInstallPermission = getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {//没有权限
                //弹框提示用户手动打开
                AssertAlert.show(this, "安装权限", "需要打开允许来自此来源，请去设置中开启此权限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            //此方法需要API>=26才能使用
                            startInstallPermissionSettingActivity();
                        }
                    }
                });
                return;
            }
        }
        //有权限，开始安装应用程序
        ver.installApk();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        Uri packageURI = Uri.parse("package:" + getPackageName());
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        startActivityForResult(intent, 16667);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 16667) {
            Toast.makeText(this,"安装应用",Toast.LENGTH_SHORT).show();
            //有权限，开始安装应用程序
            ver.installApk();
        }
    }


}
