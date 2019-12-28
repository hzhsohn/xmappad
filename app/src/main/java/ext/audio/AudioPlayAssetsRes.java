package ext.audio;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/*

//例子
AudioPlayAssetsRes play;
play = new AudioPlayAssetsRes(this);
play.prepareAndPlay("mbwarning.mp3");

*/

public class AudioPlayAssetsRes {

    //内容
    Context cnt;
    //
    private MediaPlayer mediaPlayer = null;// 播放器
    private AudioManager audioMgr = null; // Audio管理器，用了控制音量
    private AssetManager assetMgr = null; // 资源管理器
    //
    private int maxVolume = 50; // 最大音量值
    private int curVolume = 20; // 当前音量值
    private int stepVolume = 0; // 每次调整的音量幅度

    public AudioPlayAssetsRes(Context ct)
    {
        cnt=ct;
        initPlayWork();
    }

    /**
     * 准备播放音乐
     *
     */
    public void prepareAndPlay(String assetsMusicName) {

        try {
            // 打开指定音乐文件
            AssetFileDescriptor afd = assetMgr.openFd(assetsMusicName);
            mediaPlayer.reset();
            // 使用MediaPlayer加载指定的声音文件。
            mediaPlayer.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
            // 准备声音
            mediaPlayer.prepare();
            // 播放
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化播放器、音量数据等相关工作
     */
    private void initPlayWork() {
        audioMgr = (AudioManager) cnt.getSystemService(Context.AUDIO_SERVICE);
        // 获取最大音乐音量
        maxVolume = audioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 获取初始化音量
        curVolume = audioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        // 每次调整的音量大概为最大音量的1/6
        stepVolume = maxVolume / 6;

        mediaPlayer = new MediaPlayer();
        assetMgr = cnt.getAssets();
    }

    public void addVol()
    {
        curVolume += stepVolume;
        if (curVolume >= maxVolume) {
            curVolume = maxVolume;
        }
        adjustVolume();
    }

    public void subtractVol()
    {
        curVolume -= stepVolume;
        if (curVolume <= 0) {
            curVolume = 0;
        }
        adjustVolume();
    }

    /**
     * 调整音量
     */
    private void adjustVolume() {
        audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, curVolume,
                AudioManager.FLAG_PLAY_SOUND);
    }

}
