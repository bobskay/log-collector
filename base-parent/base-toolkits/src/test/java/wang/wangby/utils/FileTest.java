package wang.wangby.utils;


import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;

@Slf4j
public class FileTest {

    public static void main(String[] args) {
        System.out.println(compute("E:\\linux\\workspace",file -> !file.getAbsolutePath().endsWith(".java")));
        System.out.println(compute("E:\\blog\\blog\\_posts",file -> !file.getAbsolutePath().endsWith(".md")));
    }


    public static String compute(String path, Function<File,Boolean> ignroe){
        File dir=new File(path);
        LongAdder size=new LongAdder();
        LongAdder line=new LongAdder();
        LongAdder word=new LongAdder();
        AtomicInteger count=new AtomicInteger(0);
        FileUtil.iterator(dir,(dd,file)->{
            if(dd.getName().startsWith(".")){
                return false;
            }
            if(ignroe.apply(file)){
                return true;
            }

            size.add(file.length());
            count.addAndGet(1);
            String s=FileUtil.getText(file.getAbsolutePath());
            String[] lines=s.split("\n");
            for(String ss:lines){
                if(ss.trim().length()>1){
                    line.add(1);
                }
            }
            word.add(s.length());
            //System.out.println(file.getAbsoluteFile());
            return true;
        });

        String sizeS=StringUtil.showSize(size.longValue());
        return MessageFormat.format("文件数={0},字节={1},总字数{2},总行数={3}",count,sizeS,word,line);
    }
}
