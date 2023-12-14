package com.zhangjun.quyi.ftp.config;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FtpConfig {

    //ftp服务器ip地址
    @Value("${ftp.host}")
    String FTP_ADDRESS;

    //端口号
    @Value("${ftp.port}")
    Integer FTP_PORT;

    //用户名
    @Value("${ftp.username}")
    String FTP_USERNAME;

    //密码
    @Value("${ftp.password}")
    String FTP_PASSWORD;


    @Bean
    public FTPClient createFtpClient(){
        FTPClient ftp = new FTPClient();
        ftp.setControlEncoding("GBK");
        try {
            int reply;
            ftp.connect(FTP_ADDRESS, FTP_PORT);// 连接FTP服务器
            ftp.login(FTP_USERNAME, FTP_PASSWORD);// 登录
            reply = ftp.getReplyCode();
            ftp.enterLocalPassiveMode();//开启被动模式，否则文件上传不成功，也不报错
        }catch (Exception e){
            e.printStackTrace();
        }
        return ftp;
    }
}
