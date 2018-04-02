package top.zhangx.gif.service;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class OssService {
    public final static String gifBucketName = "gif-factory";
    public final static String cacheBucketName = "temp-cache";

    @Value("${custom.config.endPoint}")
    private String endPoint;

    public static String OSSAlias = "https://giffile.zhangx.top";
    private final static String ACCESS_KEY_ID = "HUPuIA97jIUFGP5t";
    private final static String ACCESS_KEY_SECRET = "bnWcQew3mxJt0MXm5UZIpcQ0bTtkIA";

    private OSSClient client;

    public OssService(){
    }


    public OSSClient getClient() {
        if (client == null){
            ClientConfiguration conf = new ClientConfiguration();
            conf.setConnectionTimeout(5000);
            conf.setMaxErrorRetry(3);
            client= new OSSClient(endPoint, ACCESS_KEY_ID, ACCESS_KEY_SECRET, conf);
        }
        return client;
    }

    @PreDestroy
    public void shutDownOSSClient(){
        if (client != null)
            client.shutdown();
    }
}
