package android.zh.xmappad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.zh.libxmap.command.XMapCommand;
import android.zh.libxmap.lan.EzhNetEvent;
import android.zh.libxmap.lan.XMapLan;
import android.zh.libxmap.pack.XMapReadPacket;
import android.zh.libxmap.protocol.EControLoginResult;
import android.zh.libxmap.protocol.EControlProtocolBase;
import android.zh.xmappad.lan.FGM_Cover;
import android.zh.xmappad.lan.FGM_Weather;

import java.util.ArrayList;
import java.util.List;


class GlobalMenuItem {
    public String title;

    GlobalMenuItem() {
        title = "";
    }
};

class GlobalMenuAdapter extends BaseAdapter {
    private List<GlobalMenuItem> items;
    private LayoutInflater mInflater;
    private Context mContext = null;


    public GlobalMenuAdapter(Context context, List<GlobalMenuItem> lstItem) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        if (null != items) {
            items.clear();
        }
        items = lstItem;
    }
    public void clear() {
        items.clear();
    }
    public Object getItem(int arg0) {
        return items.get(arg0);
    }
    public long getItemId(int position) {
        return position;
    }
    public int getCount() {
        return items.size();
    }

    public View getView(int position, View convertView,
                        android.view.ViewGroup parent) {
        ImageView img;
        TextView indexTitle;

        if (convertView == null) {
            // 和item_custom.xml脚本关联
            convertView = mInflater.inflate(R.layout.list_gmenu, null);
        }

        img = (ImageView) convertView.findViewById(R.id.img);
        indexTitle = (TextView) convertView.findViewById(R.id.title);

        GlobalMenuItem it = items.get(position);
        indexTitle.setText(it.title);

        return convertView;
    }

};

public class InfoActivity extends AppCompatActivity
{
    final String FLAG="InfoActivity";

    //--------------------------------------
    Context _cxt;
    //--------------------------------------
    XMapCommand sysCmd;
    XMapLan sysXl;
    //是否连接
    boolean isConnected;
    //保活包
    long dwLastKeepTime;
    //XMAP服务器信息
    String host="127.0.0.1";
    String port="5990";

    //--------------------------------------
    private List<GlobalMenuItem> lstMenuItem = new ArrayList<>();
    private GlobalMenuAdapter adapter;
    //菜单
    LinearLayout llt;
    //子界面
    private ViewPager viewPager;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        _cxt=this;
        //
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //viewPager.setCurrentItem(0);
        //全局按键
        ImageButton imageButton=(ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(menuBtn_click);
        //菜单
        llt=(LinearLayout)findViewById(R.id.menuList);
        llt.setVisibility(View.GONE);
        //菜单列表
        ListView listView = (ListView) findViewById(R.id.lv);
        adapter = new GlobalMenuAdapter(_cxt, lstMenuItem);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(lvListern);
        initMenuList();
        //
        viewPager.addOnPageChangeListener(vpler);
        //初始化子界面
        setupViewPager(viewPager);

        //--------------------------------------------
        //初始化网络
        sysCmd=new XMapCommand(); //指命处理层
        sysXl=new XMapLan();
        new Thread(task).start();
        //连接到服务器
        String host="127.0.0.1";
        String port="5990";
        //连接服务器
        sysXl.Init(0);
        sysXl.Connect(host,Integer.parseInt(port));
        isConnected=false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    private void setupViewPager(final ViewPager viewPager) {
        //加载界面
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(FGM_Cover.newInstance("@My1"));
        adapter.addFragment(FGM_Weather.newInstance("@My2"));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());
    }

    void initMenuList()
    {
        lstMenuItem.clear();

        GlobalMenuItem gmi=new GlobalMenuItem();
        gmi.title="ABC";
        lstMenuItem.add(gmi);
        gmi=new GlobalMenuItem();
        gmi.title="DDD";
        lstMenuItem.add(gmi);
        gmi=new GlobalMenuItem();
        gmi.title="CCC";
        lstMenuItem.add(gmi);

        adapter.notifyDataSetChanged();
    }

    ViewPager.OnPageChangeListener vpler=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
        @Override
        public void onPageSelected(int position) {
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    View.OnClickListener menuBtn_click= new View.OnClickListener()
    {
        @SuppressLint("ResourceType")
        @Override
        public void onClick(View v)
        {
            if(llt.getVisibility()==View.GONE)
            {
                llt.setVisibility(View.VISIBLE);
            }
            else
            {
                llt.setVisibility(View.GONE);
            }
        }
    };

    private AdapterView.OnItemClickListener lvListern = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final GlobalMenuItem gmi = (GlobalMenuItem) adapter.getItem(position);


        }
    };


    //
    //--联网-----------------
    //
    Runnable task = new Runnable() {
        @Override
        public void run() {

            while(true)
            {
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        byte[] rd=sysXl.ReadData();
                        if(rd!=null)
                        {
                            if(rd.length>0) {
                                Log.v("zhnet", "rd=" + rd[0]);
                                procNet(rd);
                            }
                        }

                        EzhNetEvent ev=sysXl.StateThread();
                        switch (ev)
                        {
                            //连接成功
                            case ezhNetEventConnected: {
                                Log.v("zhnet", "ezhNetEventConnected");
                                Toast.makeText(_cxt, "连接成功", Toast.LENGTH_SHORT).show();
                                isConnected = true;
                                dwLastKeepTime = System.currentTimeMillis();


                                //System.out.println("----------------------btn1_click");
                                //登录
                                String uname ="admin";
                                String paswd ="admin";
                                //
                                String ANDROID_ID = Settings.System.getString(_cxt.getContentResolver(), Settings.System.ANDROID_ID);
                                try {
                                    sysXl.sendPack(sysCmd.login(uname, paswd, ANDROID_ID));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                            //连接超时
                            case ezhNetEventConnectTimeout:
                                Log.v("zhnet","ezhNetEventConnectTimeout");
                                Toast.makeText(_cxt, "连接超时", Toast.LENGTH_SHORT).show();
                                isConnected=false;
                                break;
                            //断开连接
                            case ezhNetEventDisconnect:
                                Log.v("zhnet","ezhNetEventDisconnect");
                                Toast.makeText(_cxt, "断开连接", Toast.LENGTH_SHORT).show();
                                isConnected=false;
                                break;
                            //
                            case ezhNetNoEvent:
                                //Log.v("zhnet","ezhNetNoEvent");
                                break;
                        }

                        //-----------------------
                        //连接成功
                        if(isConnected)
                        {
                            //持续发送保活包,20秒一次
                            if(System.currentTimeMillis() - dwLastKeepTime > 20000) {
                                try {
                                    dwLastKeepTime = System.currentTimeMillis();
                                    sysXl.sendPack(sysCmd.getKeep());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });


                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    //处理回来的数据信息
    void procNet(byte buf[])
    {
        try {
            XMapReadPacket pack = new XMapReadPacket();
            pack.readInit(buf);
            short rcmd = pack.readShort();
            Log.v("recvProc", "rcmd="+rcmd);
            switch (rcmd) {
                case EControlProtocolBase.ecpnSToCKeep:
                {
                    android.zh.xmap_lib_demo.XMBaseStatus.lastRecvKeepTime=System.currentTimeMillis();
                    android.zh.xmap_lib_demo.XMBaseStatus.rtt= android.zh.xmap_lib_demo.XMBaseStatus.lastRecvKeepTime - android.zh.xmap_lib_demo.XMBaseStatus.lastSendKeepTime;
                    sysXl.sendPack(sysCmd.getKeep());
                }
                break;
                case EControlProtocolBase.ecpuSToCLogin: {
                    int ret=pack.readChar();
                    switch(ret)
                    {
                        case EControLoginResult.ecpRetLoginNone://*****************未登录不能操作*****************
                            Log.v(FLAG,"ZH_GROUP_LOGIN_RESULT_NO_LOGIN");
                            Toast.makeText(_cxt, "未登录不能操作", Toast.LENGTH_SHORT).show();
                            break;
                        case EControLoginResult.ecpRetLoginSuccess://*****************登录成功*****************
                            Log.v(FLAG,"ZH_GROUP_LOGIN_RESULT_SUCCESS");
                            Toast.makeText(_cxt, "登录成功...!!!", Toast.LENGTH_SHORT).show();
                            break;
                        case EControLoginResult.ecpRetLoginPasswordFail://*****************密码错误*****************
                            Log.v(FLAG,"ZH_GROUP_LOGIN_RESULT_PASS_FAIL");
                            Toast.makeText(_cxt, "登录失败!!", Toast.LENGTH_SHORT).show();
                            break;
                        case EControLoginResult.ecpRetLoginOverload://*****************服务器超载*****************
                            Log.v(FLAG,"ZH_GROUP_LOGIN_RESULT_OVERLOAD");
                            Toast.makeText(_cxt, "服务器超载", Toast.LENGTH_SHORT).show();
                            break;
                        case EControLoginResult.ecpRetLoginExistDevice://*****************账户异地登录,当前设备被断开*****************
                            Log.v(FLAG,"ZH_GROUP_LOGIN_RESULT_EXIST_DEVICEID");
                            Toast.makeText(_cxt, "账户异地登录,当前设备被断开", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
