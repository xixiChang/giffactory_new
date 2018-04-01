package top.zhangx.gif.controller;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.zhangx.gif.constants.WebStatusInfo;
import top.zhangx.gif.entity.Result;
import top.zhangx.gif.mapper.SentenceMapper;
import top.zhangx.gif.mapper.TemplateMapper;
import top.zhangx.gif.service.TemplatesService;
import top.zhangx.gif.service.OssService;

import java.io.File;
import java.io.IOException;

@RestController
public class TemplateController {
    private static final String DES_FILE_SUFFIX = ".txt";
    private static final Logger logger = LoggerFactory.getLogger(TemplateController.class);

    @Autowired
    private TemplateMapper templateMapper;
//    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
//    private TemplateMapper templateMapper = context.getBean(TemplateMapper.class);

    @Autowired
    private SentenceMapper sentenceMapper;

    @Autowired
    private TemplatesService templatesService;

    @Autowired
    private OssService ossService;

    @GetMapping(path = "/getTemplates")
    public Result getTemplates(){
        Result result = new Result();
        result.setResult(templatesService.getTemplates());
        return result;
    }

    @GetMapping(path = "/getTemplate")
    public Result getTemplate(@RequestParam(name = "templateName") String templateName){
        Result result = new Result();
        if (StringUtils.isEmpty(templateName)){
            result.setStatus(WebStatusInfo.STATUS_FAIL);
            result.setMsg("无请求参数：templateName");
            return result;
        }
        final int tempID = templateMapper.getIdByName(templateName);
        result.setResult(sentenceMapper.getSentencesByTID(tempID));
        return result;
    }


    @PostMapping(path = "/postTemplates")
    public Result postTemplates(MultipartFile file, String description){
        Result result = new Result();
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isEmpty(originalFilename)){
            result.setStatus(WebStatusInfo.STATUS_FAIL);
            result.setMsg("上传失败,无法获取文件名或格式");
            return result;
        }
        String fileName = templatesService.getTempPath() + file.getOriginalFilename();
        try {
            FileUtils.writeByteArrayToFile(
                    new File(fileName),
                    file.getBytes());
        }catch (IOException ioe){
            result.setStatus(WebStatusInfo.STATUS_FAIL);
            result.setMsg(ioe.getMessage());
            return result;
        }
        ossService.getClient().putObject(OssService.cacheBucketName, originalFilename,
                new File(fileName));
        final String desFilePath = writeDesFile(description, fileName);
        if (desFilePath != null){
            logger.debug("已写入模板描述文件－>" + desFilePath);
        }

        return result;
    }

    private String writeDesFile(String des, String fileName){
        if (StringUtils.isEmpty(des))
            return null;
        int index = fileName.indexOf(".");
        fileName = fileName.substring(0, index);
        try {
            FileUtils.writeByteArrayToFile(new File(fileName + DES_FILE_SUFFIX), des.getBytes());
        } catch (IOException e) {
            logger.error("写入模板描述文件错误", e.getMessage());
            return null;
        }
        return fileName + DES_FILE_SUFFIX;
    }

}
