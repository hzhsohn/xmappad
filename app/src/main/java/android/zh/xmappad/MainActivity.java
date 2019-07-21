package android.zh.xmappad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity
{
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
        adapter = new GlobalMenuAdapter(MainActivity.this, lstMenuItem);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(lvListern);
        initMenuList();
        //
        viewPager.addOnPageChangeListener(vpler);
        //初始化子界面
        setupViewPager(viewPager);
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
        adapter.addFragment(MyMain.newInstance("@My"));
        adapter.addFragment(MyMain2.newInstance("@My2"));
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

}
