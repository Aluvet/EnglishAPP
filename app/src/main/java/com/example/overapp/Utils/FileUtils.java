package com.example.overapp.Utils;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtils {

//BufferedReader是Java I/O中的一个类，它是一个带缓冲区的字符输入流，用于从字符输入流中读取字符。
// 它提供了一种逐行读取文本文件的方法，可以轻松地读取大量文本数据，并且可以通过使用缓冲区来提高读取效率。
// 它的主要作用是读取文本文件中的字符数据，可以读取文件中的每一行数据，是Java I/O中常用的数据读取类之一。
// BufferedReader类只能读取字符类型的数据，如果需要读取其他类型的数据需要进行类型转换。




//将byte数组转化为文件
public static void getFileByBytes(byte[] bytes, String filePath, String fileName) {
    BufferedOutputStream bos = null;
    FileOutputStream fos = null;
    File file = null;
    try {
        File dir = new File(filePath);
        if (!dir.exists()) {// 判断文件目录是否存在
            Log.d("FileUtils", "没有这个目录");
            dir.mkdirs();
        }
        file = new File(filePath + "//" + fileName);
        fos = new FileOutputStream(file);
        bos = new BufferedOutputStream(fos);
        bos.write(bytes);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (bos != null) {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}


    /**
     * @param archive       解压文件得路径
     * @param decompressDir 解压文件目标路径
     * @param isDeleteZip   解压完毕是否删除解压文件
     * @throws IOException
     */
    public static void unZipFile(String archive, String decompressDir, boolean isDeleteZip) throws IOException {
        BufferedInputStream bi;
        ZipFile zf = new ZipFile(archive);
        Enumeration e = zf.entries();
        while (e.hasMoreElements()) {
            ZipEntry ze2 = (ZipEntry) e.nextElement();
            String entryName = ze2.getName();
            String path = decompressDir + "/" + entryName;
            if (ze2.isDirectory()) {
                File decompressDirFile = new File(path);
                if (!decompressDirFile.exists()) {
                    decompressDirFile.mkdirs();
                }
            } else {
                if (decompressDir.endsWith(".zip")) {
                    decompressDir = decompressDir.substring(0, decompressDir.lastIndexOf(".zip"));
                }
                File fileDirFile = new File(decompressDir);
                if (!fileDirFile.exists()) {
                    fileDirFile.mkdirs();
                }
                String substring = entryName.substring(entryName.lastIndexOf("/") + 1, entryName.length());
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(decompressDir + "/" + substring));
                bi = new BufferedInputStream(zf.getInputStream(ze2));
                byte[] readContent = new byte[1024];
                int readCount = bi.read(readContent);
                while (readCount != -1) {
                    bos.write(readContent, 0, readCount);
                    readCount = bi.read(readContent);
                }
                bos.close();
            }
        }
        zf.close();
        if (isDeleteZip) {
            File zipFile = new File(archive);
            if (zipFile.exists() && zipFile.getName().endsWith(".zip")) {
                zipFile.delete();
            }
        }
    }
    // 读取本地文件，参数要读取的文件名
    public static String readLocalData(String fileName) {
//        创建 StringBuilder 对象，用于构建读取到的文件内容。
        StringBuilder stringBuilder = new StringBuilder();
//        初始化BufferedReader 对象为 null。这个对象将用于逐行读取文件内容
        BufferedReader bufferedReader = null;
//        创建读取的文件，
//        MyApplication.getContext().getFilesDir() 获取应用的内部存储目录，然后和文件名 fileName 拼接，得到完整的文件路径
        File file = new File(MyApplication.getContext().getFilesDir(), fileName);
        // 文件不存在
        if (!file.exists()) {
            return null;
        }
        try {
//            创建 BufferedReader 对象，并传入一个以 File 对象为参数的 FileReader，用于读取文件
            bufferedReader = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        在读取文件是将格式转化
/*   String resStr = response.body().string();
String s = resStr.replace("{\"wordRank\"", ",{\"wordRank\"");
String ss = "[" + s.substring() + "]";*/
        String s = stringBuilder.toString().replace("{\"wordRank\"", ",{\"wordRank\"");
        return "[" + s.substring(1) + "]";
    }
//    在调用百度云ocr时使用用于图片压缩
    public  static byte[] bitmapCompress(Bitmap bitmap, int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int quality = 100;
        //循环判断如果压缩后图片是否大于100kb,大于继续压缩,这里的要求数值可根据需求设置
        while (baos.toByteArray().length / 1024 > size) {
            //重置baos即清空baos
            baos.reset();
            //这里压缩quality%，把压缩后的数据存放到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            if (quality - 10 <= 0)
                break;
            else
                quality -= 10;//每次都减少10
        }
        //转为字节数组返回
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

}
