package top.zhangx.gif.service;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Getter
@Setter
@ConfigurationProperties(prefix = "cache.template")
public class AppService {
    private static final Logger logger = LoggerFactory.getLogger(AppService.class);

    private String tempPath;

    @Value("${custom.config.fileKeepTime}")
    private float fileKeepTime;//小时

    //每日凌晨3：40清理老文件
    @Scheduled(cron = "0 40 3 * * *")
    public void cleanOldFile(){
        cleanAssGifFile();
    }

    private void cleanAssGifFile() {
        File root = Paths.get(tempPath).toFile();
        if (root != null && root.isDirectory()) {
            String[] fileList = root.list();
            if (fileList == null)
                return;
            long currentTime = System.currentTimeMillis();
            long keepTime = (long) (fileKeepTime * 60 * 60 * 1000);
            int deleteFilesCountS = 0;
            int deleteFilesCountF = 0;
            for (String subFile : fileList) {
                if (!StringUtils.isEmpty(subFile) &&
                        (subFile.endsWith(".gif")
                         || subFile.endsWith(".ass"))){
                    File oFile = Paths.get(tempPath + subFile).toFile();
                    if (oFile != null){
                        final long lastModifiedTime = oFile.lastModified();
                        if (currentTime - lastModifiedTime > keepTime){
                            if (oFile.delete()) {
                                deleteFilesCountS++;
                            } else {
                                deleteFilesCountF++;
                            }
                        }
                    }
                }
            }
            final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            logger.info("文件清理工作,于" + dateFormat.format(new Date()) + "处理文件：" + fileList.length + "个");
            logger.info( "删除成功:" + deleteFilesCountS);
            logger.info( "删除失败:" + deleteFilesCountF);
        }
    }
}
