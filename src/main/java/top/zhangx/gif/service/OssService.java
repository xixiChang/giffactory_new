package top.zhangx.gif.service;

import com.aliyun.oss.OSSClient;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class OssService {
    public final static String gifBucketName = "gif-factory";
    public final static String cacheBucketName = "temp-cache";

    private final static String endPoint = "https://oss-cn-shanghai.aliyuncs.com";
    public static String OSSAlias = "https://giffile.zhangx.top";
    private final static String ACCESS_KEY_ID = "HUPuIA97jIUFGP5t";
    private final static String ACCESS_KEY_SECRET = "bnWcQew3mxJt0MXm5UZIpcQ0bTtkIA";


    private OSSClient client = new OSSClient(endPoint, ACCESS_KEY_ID, ACCESS_KEY_SECRET);

    public OSSClient getClient() {
        return client;
    }

    @PreDestroy
    public void shutDownOSSClient(){
        if (client != null)
            client.shutdown();
    }
}
