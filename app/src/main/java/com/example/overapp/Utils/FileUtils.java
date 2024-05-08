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




//将byte数组转化为文件，保存到特定路径，以及文件名下
public static void getFileByBytes(byte[] bytes, String filePath, String fileName) {
//    带缓冲的输出流，高效地将字节数组写入文件
    BufferedOutputStream bos = null;
//    输出流，将字节写入文件
    FileOutputStream fos = null;
//    创建文件
    File file = null;
    try {
//        创建 File 对象，指定文件路径
        File dir = new File(filePath);
        if (!dir.exists()) {// 判断文件目录是否存在，没有直接创建
            Log.d("FileUtils", "没有这个目录");
            dir.mkdirs();
        }
//        创建文件路径，直接写入文件，路径//文件名
        file = new File(filePath + "//" + fileName);
//        FileOutputStream 对象,将数据写入文件
        fos = new FileOutputStream(file);
        bos = new BufferedOutputStream(fos);
//        字节数组写入文件
        bos.write(bytes);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
//        将BufferedOutputStream 和 FileOutputStream关闭
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

//解压压缩包
    /**
     * @param archive       解压文件得路径
     * @param decompressDir 解压文件目标路径
     * @param isDeleteZip   解压完毕是否删除解压文件
     * @throws IOException
     */
    public static void unZipFile(String archive, String decompressDir, boolean isDeleteZip) throws IOException {
        BufferedInputStream bi;
//        创建zipfile对象,解压文件
        ZipFile zf = new ZipFile(archive);
//        调用entries获取zip中的所有条目,枚举
        Enumeration e = zf.entries();
//        遍历zip中的文件的每一条目
        while (e.hasMoreElements()) {
//            获取下一个对象文件
            ZipEntry ze2 = (ZipEntry) e.nextElement();
//            获取条目名称后,构建完整的路径
            String entryName = ze2.getName();
            String path = decompressDir + "/" + entryName;
//            条目为文件夹,检查是否再解压目录中存在,不存在创建见文件夹
            if (ze2.isDirectory()) {
                File decompressDirFile = new File(path);
                if (!decompressDirFile.exists()) {
                    decompressDirFile.mkdirs();
                }
            } else {
//                文件,zip结尾,先将zip移除在截取空0到.zip的所有字符
                /*例:ecompressDir的值为/path/to/archive.zip，pressDir的值会变为/path/to/archive  */
                if (decompressDir.endsWith(".zip")) {
                    decompressDir = decompressDir.substring(0, decompressDir.lastIndexOf(".zip"));
                }
//                创建文件/路径
                File fileDirFile = new File(decompressDir);
//                不存在同其他要创建
                if (!fileDirFile.exists()) {
                    fileDirFile.mkdirs();
                }
//                1.先提取文件名：通过找到条目名称中最后一个斜杠的位置，并截取它之后的部分来实现。
//                2.再创建BufferedOutputStream，将文件内容写入到解压目录中的相应位置。
//                3.后创建BufferedInputStream，从ZIP文件中读取该条目的内容。
                String substring = entryName.substring(entryName.lastIndexOf("/") + 1, entryName.length());
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(decompressDir + "/" + substring));
                bi = new BufferedInputStream(zf.getInputStream(ze2));
//                读取和写入文件内容1024字节缓冲区读取内容
                byte[] readContent = new byte[1024];
                int readCount = bi.read(readContent);
//                循环将读取到的内容写入到输出流中
                while (readCount != -1) {
                    bos.write(readContent, 0, readCount);
                    readCount = bi.read(readContent);
                }
//                关闭输出流释放资源
                bos.close();
            }
        }
//        zf.close()来实现关闭zipfile对象
        zf.close();
//       zip存在, 删除指定文件
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
//        初始化BufferedReader 对象为 null。字符输入流读取文本
        BufferedReader bufferedReader = null;
//        创建读取的文件，
//        MyApplication.getContext().getFilesDir() 获取应用的内部存储目录，然后和文件名 fileName 拼接，得到完整的文件路径
        File file = new File(MyApplication.getContext().getFilesDir(), fileName);
        // 文件不存在
        if (!file.exists()) {
            return null;
        }
        try {
//            创建 BufferedReader 对象，并传入一个以 File 对象为参数的 FileReader，读取文件
            bufferedReader = new BufferedReader(new FileReader(file));
            String line = "";
//            readline逐行读取文件,知道空,读取后加入string builder中
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
//            字符输入流置为空
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
