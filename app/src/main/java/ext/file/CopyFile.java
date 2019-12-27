package ext.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by hzh on 2017/9/3.
 * <p>
 * <p>
 * 权限
 * <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 */

public class CopyFile {

    static public int copy(String fromFile, String toFile) {
        //要复制的文件目录
        File[] currentFiles;
        File root = new File(fromFile);
        //如同判断SD卡是否存在或者文件是否存在
        //如果不存在则 return出去
        if (!root.exists()) {
            return -1;
        }
        if(root.isDirectory()) {
            //如果存在则获取当前目录下的全部文件 填充数组
            currentFiles = root.listFiles();

            //目标目录
            File targetDir = new File(toFile);
            //创建目录
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            //遍历要复制该目录下的全部文件
            for (int i = 0; i < currentFiles.length; i++) {
                if (currentFiles[i].isDirectory())//如果当前项为子目录 进行递归
                {
                    copy(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");

                } else//如果当前项为文件则进行文件拷贝
                {
                    CopySdcardFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
                }
            }
        }
        else if(root.isFile())
        {
            CopySdcardFile(fromFile, toFile);
        }
        return 0;
    }


    //文件拷贝
    //要复制的目录下的所有非子目录(文件夹)文件拷贝
    static public int CopySdcardFile(String fromFile, String toFile) {

        try {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;

        } catch (Exception ex) {
            return -1;
        }
    }

}
