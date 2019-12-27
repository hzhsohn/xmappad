package android.zh.xmap_lib_demo;

public class XMBaseStatus {
    public static long lastSendXTNetSignTime=0;//最后一次发送登录的时间
    public static boolean isDevuuidSubscrOK=false; //是否订阅数据成功
    public static boolean isXTNetSignSuccess=false;//是否签入通讯成功

    //网络通讯质量
    public static long lastSendKeepTime;
    public static long lastRecvKeepTime;
    public static long rtt;

}
