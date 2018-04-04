package top.zhangx.gif.controller;

import org.apache.commons.lang.CharSet;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.zhangx.gif.constants.WebStatusInfo;
import top.zhangx.gif.entity.Result;
import top.zhangx.gif.entity.Sentence;
import top.zhangx.gif.service.GifService;
import top.zhangx.gif.service.OssService;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by zhangxi on 2018/3/28.
 */
@RestController
@RequestMapping(path = "/gif", consumes = {MediaType.APPLICATION_JSON_VALUE})
public class GifController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GifService.class);

    @Autowired
    GifService gifService;

    @Autowired
    private OssService ossService;

    @PostMapping(path = "/produce")
    public Result renderGifPath(@RequestBody List<Sentence> sentences, @RequestParam String templateName){
        Result result = new Result();
        if (sentences == null || sentences.size() == 0){
            result.setStatus(WebStatusInfo.STATUS_FAIL);
            result.setMsg("无字幕参数");
            return result;
        }
        if (StringUtils.isEmpty(templateName)){
            result.setStatus(WebStatusInfo.STATUS_FAIL);
            result.setMsg("无模板名");
            return result;
        }
        try{
            String gifPath = gifService.renderGif(sentences, templateName);
            if (StringUtils.isEmpty(gifPath)){
                result.setStatus(WebStatusInfo.STATUS_FAIL);
                result.setMsg("生成Gif失败:#01");
            }
            String gifName = Paths.get(gifPath).getFileName().toString();
            ossService.getClient().putObject(OssService.gifBucketName, gifName, new File(gifPath));
            result.setResult(OssService.OSSAlias + "/" + gifName);
        } catch (Exception e) {
            result.setStatus(WebStatusInfo.STATUS_FAIL);
            result.setMsg("生成Gif失败:#02");
            logger.error("生成Gif失败:02", e.getMessage());
            return result;
        }
        return result;
    }

    @GetMapping(path = "/download", produces = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Resource> renderGif(@RequestParam(name = "gifUri") String gifUri) throws Exception {
        Resource resource = new UrlResource(gifUri);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=xx.gif").body(resource);
    }
}
