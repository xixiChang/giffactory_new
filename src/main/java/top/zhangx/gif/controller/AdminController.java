package top.zhangx.gif.controller;

import com.aliyun.oss.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.zhangx.gif.constants.WebStatusInfo;
import top.zhangx.gif.entity.Result;
import top.zhangx.gif.entity.Sentence;
import top.zhangx.gif.entity.Template;
import top.zhangx.gif.mapper.AccessInfoMapper;
import top.zhangx.gif.mapper.SentenceMapper;
import top.zhangx.gif.mapper.TemplateMapper;
import top.zhangx.gif.service.OssService;

import java.io.File;
import java.util.List;

@Controller
public class AdminController {
    private static final String TOKEN = "akljwfqwpoeu2983-'asfluq87r";

    @Autowired
    private TemplateMapper templateMapper;

    @Autowired
    private SentenceMapper sentenceMapper;

    @Autowired
    private AccessInfoMapper accessInfoMapper;

    @Autowired
    private OssService ossService;

    @GetMapping("/admin")
    public String admin(/*@RequestParam (name = "token") String token*/){
//        if (StringUtils.isEmpty(token) || !TOKEN.equals(token)){
//            return "/error";
//        }
//        PutObjectResult putObjectResult = ossService.getClient().putObject(OssService.gifBucketName, "1", new File("/local/sda/temp/build.prop"));
//        putObjectResult.getETag();

        return "/admin";
    }

    @PostMapping("/putInfo")
    @ResponseBody
    public Result putInfo(@RequestParam String tempName,
                          @RequestBody List<Sentence> sentences){
        Result result = new Result();
        Template template = new Template();
        template.setName(tempName);
        templateMapper.addTemplate(template);
        for (Sentence sentence:sentences)
            sentence.setTemplate_id(template.getId());
        if (sentenceMapper.addSentences(sentences) <= 0 ){
            result.setMsg("无法添加sentence for TID = " + template.getId());
            result.setStatus(WebStatusInfo.STATUS_FAIL);
        }
        return result;
    }

    @GetMapping("/getAccessInfo/all")
    @ResponseBody
    public Result getAllAccessInfo(@RequestParam (name = "token") String token){
        Result result = new Result();
        if (StringUtils.isEmpty(token) || !TOKEN.equals(token)){
            result.setStatus(WebStatusInfo.STATUS_FAIL);
            result.setMsg("error token");
            return result;
        }
        result.setResult(accessInfoMapper.getAllAccessInfo());
        return result;
    }

    @GetMapping("/getAccessInfo/day")
    @ResponseBody
    public Result getDayAccessInfo(@RequestParam (name = "token") String token){
        Result result = new Result();
        if (StringUtils.isEmpty(token) || !TOKEN.equals(token)){
            result.setStatus(WebStatusInfo.STATUS_FAIL);
            result.setMsg("error token");
            return result;
        }
        result.setResult(accessInfoMapper.getDayAccessInfo());
        return result;
    }
}
