package com.zhangjun.quyi.ftp.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FtpService {

    //默认文件路径
    @Value("${ftp.rootPath}")
    String FTP_ROOT_PATH;

    @Autowired
    private FTPClient ftpClient;

    //根据当前文件生成 文件夹
    private static String getTimePath() {
        Date now = new Date();
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd/");
        return format.format(now);
    }

    /**
     * 文件上传
     * @param inputStream
     * @param originName
     * @return
     */
    public String upload(InputStream inputStream, String originName) throws IOException {
        StringBuilder url = new StringBuilder();
        String timePath = getTimePath();
        String saveDir = FTP_ROOT_PATH + timePath;
        createDir(ftpClient, saveDir);
        originName= System.currentTimeMillis()+originName.substring(originName.lastIndexOf('.'));
        url.append(originName);
        ftpClient.storeFile(originName, inputStream);
        inputStream.close();
        ftpClient.logout();
        ftpClient.disconnect();
        return url.toString();
    }

    // 创建文件夹，并切换到该文件夹
    // 比如： hello/test
    //最终会切换到test 文件夹返回
    private void createDir(FTPClient client, String path) throws IOException {
        String[] dirs = path.split("/");
        for (String dir : dirs) {
            if (StringUtils.isEmpty(dir)) {
                continue;
            }
            if (!client.changeWorkingDirectory(dir)) {
                client.makeDirectory(dir);
            }
            client.changeWorkingDirectory(dir);
        }
    }

}
