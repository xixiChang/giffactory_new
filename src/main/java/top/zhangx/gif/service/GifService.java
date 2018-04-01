package top.zhangx.gif.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import top.zhangx.gif.entity.Sentence;

import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by zhangxi on 2018/3/28.
 */
@Service
@Getter
@Setter
@ConfigurationProperties(prefix = "cache.template")
public class GifService {

    private static final Logger logger = LoggerFactory.getLogger(GifService.class);

    private String tempPath;

    public String renderGif(List<Sentence> subtitles, String templateName) throws Exception {
        String assPath = renderAss(subtitles, templateName);
        String gifPath = Paths.get(tempPath).resolve(UUID.randomUUID() + ".gif").toString();
        String videoPath = Paths.get(tempPath).resolve(templateName + "/template.mp4").toString();
//        String cmd = String.format("ffmpeg -i %s -r 6 -vf ass=%s,scale=300:-1 -y %s", videoPath, assPath, gifPath);
//        if ("simple".equals(subtitles.getMode())) {
//            cmd = String.format("ffmpeg -i %s -r 2 -vf ass=%s,scale=250:-1 -f gif - |gifsicle --optimize=3 --delay=20 > %s ", videoPath, assPath, gifPath);
        String  cmd = String.format("ffmpeg -i %s -r 5 -vf ass=%s,scale=180:-1 -y %s ", videoPath, assPath, gifPath);
//        }
        logger.info("cmd: {}", cmd);
        try {
            Process exec = Runtime.getRuntime().exec(cmd);
            exec.waitFor();

//            logger.info("输出:{}",IOUtils.toString(exec.getErrorStream()));
        } catch (Exception e) {
            logger.error("生成gif报错：{}", e);
            return null;
        }
        return gifPath;
    }

    private String renderAss(List<Sentence> subtitles, String templateName) throws Exception {
        Path path = Paths.get(tempPath).resolve(UUID.randomUUID().toString().replace("-", "") + ".ass");
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setDirectoryForTemplateLoading(Paths.get(tempPath).resolve(templateName).toFile());
        Map<String, Object> root = new HashMap<>();
        Map<String, String> mx = new HashMap<>();

        for (int i = 0; i < subtitles.size(); i++) {
            mx.put("sentences" + i, subtitles.get(i).getDefault_value());
        }
        root.put("mx", mx);
        Template temp = cfg.getTemplate("template.ftl");
        try (FileWriter writer = new FileWriter(path.toFile())) {
            temp.process(root, writer);
        } catch (Exception e) {
            logger.error("生成ass文件报错", e);
        }
        return path.toString();
    }

}
