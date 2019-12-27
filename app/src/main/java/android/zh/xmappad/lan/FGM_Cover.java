package android.zh.xmappad.lan;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.zh.xmappad.BaseFragment;
import android.zh.xmappad.R;


public class FGM_Cover extends BaseFragment {
    Context context;

    public static FGM_Cover newInstance(String param1) {
        FGM_Cover fragment = new FGM_Cover();
        Bundle args = new Bundle();
        args.putString("info", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fgm_cover, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        context = view.getContext();

    }

    void jumpFormMyInfo(final boolean ani) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {

              /*  FragmentTransaction transaction = getFragmentManager().beginTransaction();
                if(true==ani){
                    transaction.setCustomAnimations(R.anim.in_0, R.anim.in_1); //自定义动画
                }
                transaction.addToBackStack(null)  //将当前fragment加入到返回栈中
                        .replace(R.id.container_main), new MyInfo()).commit();*/
            }
        });

    }
}
