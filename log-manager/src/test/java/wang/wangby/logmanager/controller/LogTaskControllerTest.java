package wang.wangby.logmanager.controller;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.junit.Test;
import wang.wangby.utils.shell.Shell;

import java.io.IOException;

import static org.junit.Assert.*;

public class LogTaskControllerTest {

    @Test
    public void runTask() throws JSchException, SftpException, IOException {
        Shell shell=new Shell("myos","root","root");
        shell.upload("E:\\Git\\wangbywang\\app\\zk-manager\\target\\zk-manager-0.1.jar","/home/temp");
        shell.run("java -jar /home/temp/zk-manager-0.1.jar",str->{
            System.out.println(str);
        });
    }
}