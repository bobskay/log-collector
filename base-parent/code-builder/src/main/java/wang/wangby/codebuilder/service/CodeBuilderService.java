package wang.wangby.codebuilder.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.wangby.codebuilder.controller.vo.CodeConfig;
import wang.wangby.utils.FileUtil;
import wang.wangby.utils.template.TemplateUtil;
import wang.wangby.web.tools.controller.utils.CodeCreator;
import wang.wangby.web.tools.controller.vo.CodeCreateResult;
import wang.wangby.web.tools.controller.vo.JavaClass;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CodeBuilderService {

    @Autowired
    TemplateUtil templateUtil;

    public String createCode(CodeConfig codeConfig, String codeTemplate, String output){
        Map context = createContent(codeConfig);
        File src=new File(codeTemplate);
        String rootF=src.getAbsolutePath().replaceAll("\\\\","/");
        FileUtil.iteratorFile(src,(dir,file)->{
            String dirF=dir.getAbsolutePath().replaceAll("\\\\","/");
            String targetDir=output+"/"+dirF.substring(rootF.length());
            targetDir=templateUtil.parseText(targetDir,context);
            File createDir=new File(targetDir);
            if(!createDir.exists()){
                createDir.mkdirs();
                log.debug("生成目录:"+createDir.getAbsolutePath());
            }
            if(file.isDirectory()){
                return true;
            }
            String fileF=file.getAbsolutePath().replaceAll("\\\\","/");
            String targetFile=output+"/"+fileF.substring(rootF.length());
            targetFile=templateUtil.parseText(targetFile,context);
            targetFile=targetFile.substring(0,targetFile.length()-3);
            String content=FileUtil.getText(file.getAbsolutePath());
            content=templateUtil.parseText(content,context);
            try{
                FileUtil.createFile(targetFile,content);
                log.debug("生成文件:"+new File(targetFile).getAbsolutePath());
            }catch (Exception ex){
                log.error("生成文件出错:"+targetFile);
            }
            return true;
        });
        return "";
    }

    private Map createContent(CodeConfig codeConfig) {
        Map context=new HashMap<>();
        CodeCreateResult result = new CodeCreateResult();
        CodeCreator codeCreator = new CodeCreator(codeConfig.getSql(), codeConfig.getPackageName());
        List<JavaClass> classes=new ArrayList<>();
        classes.add(codeCreator.model());
        classes.add(codeCreator.controller());
        classes.add(codeCreator.service());
        classes.add(codeCreator.dao());
        result.setJavaClasses(classes);
        result.setPkName(codeCreator.getPkField().getName());
        result.setModelName(codeCreator.getModelName());
        result.setTableInfo(codeCreator.getTableInfo());

        codeConfig.setModelName(codeCreator.getModelName());
        context.put("codeCreateResult",result);
        context.put("dollar","$");
        context.put("codeConfig",codeConfig);
        context.put("codeCreator",codeCreator);
        return context;
    }
}
