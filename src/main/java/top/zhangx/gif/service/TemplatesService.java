package top.zhangx.gif.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.zhangx.gif.entity.Template;
import top.zhangx.gif.mapper.TemplateMapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Service
@Setter
@Getter
@ConfigurationProperties(prefix = "cache.template")
public class TemplatesService {

    @Value("${custom.config.imageBaseUri}")
    private String imageBaseUri;

    @Autowired
    private TemplateMapper templateMapper;

    private static final String imageSuffix = ".jpg";

    //每半小时刷新一次模板
//    @Scheduled(cron = "0 0/30 * * * *")
    @Scheduled(fixedDelay = 30 * 60 * 1000)
    public void updateTemplates(){
        proTemplates();
    }

    private List<Template> templates = new ArrayList<>();

    private String imagePath;

    private String tempPath;

    private void proTemplates() {
        templates.clear();
        File root = Paths.get(tempPath).toFile();
        if (root != null && root.isDirectory()) {
            String[] fileList = root.list();
            if (fileList == null)
                return;
            for (String subFile : fileList) {
                if (Paths.get(tempPath + File.separator + subFile).toFile().isDirectory()) {
                    BufferedImage srcImage = null;
                    try {
                        srcImage = ImageIO.read(new File(imagePath + subFile + imageSuffix));
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                    Template template = new Template();
                    final int srcImageHeight = srcImage.getHeight();
                    final int srcImageWidth = srcImage.getWidth();
                    template.setWidth(String.valueOf(srcImageWidth));
                    template.setHeight(String.valueOf(srcImageHeight));
                    template.setName(subFile);
                    template.setUri(imageBaseUri + subFile + imageSuffix);
                    template.setDisplay_name(templateMapper.getDisplayNameByName(subFile));
                    templates.add(template);
                }
            }
        }
    }
}
