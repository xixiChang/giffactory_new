package top.zhangx.gif.config;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.zhangx.gif.GifAppContext;
import top.zhangx.gif.constants.WebStatusInfo;
import top.zhangx.gif.entity.AccessInfo;
import top.zhangx.gif.entity.Result;
import top.zhangx.gif.mapper.AccessInfoMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


public class WebRequestInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = Logger.getLogger(getClass().toString());
    private static final String CLIENT_TYPE = "client_type";
    private static final String CLIENT_PC = "pc";
    private static final String CLIENT_MOBILE = "mobile";

    @Autowired
    private AccessInfoMapper accessInfoMapper;

    @Value("${custom.config.ifLimit}")
    private boolean ifLimit;

    @Value("${custom.config.limitTime}")
    private int limitTime;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userAgent = request.getHeader("User-Agent");
        final String clientIP = request.getRemoteAddr();
        final String uri = request.getRequestURI();
        final AccessInfo accessInfo = new AccessInfo();
        if (!StringUtils.isEmpty(userAgent) && userAgent.length() > 255)
            userAgent = userAgent.substring(0, 255);
        accessInfo.setUa(userAgent);
        accessInfo.setIp(clientIP);
        accessInfo.setUri(uri);
        accessInfoMapper.addAccessInfo(accessInfo);

        if ("/gif/produce".equals(uri)
                && (StringUtils.isEmpty(clientIP)
                || clientIP.startsWith("127.0")
                || clientIP.startsWith("192.168"))){
            Result result = new Result();
            result.setStatus(WebStatusInfo.STATUS_FAIL);
            result.setMsg("Server get a null IP");
            result.setResult("Don't use proxy to access the API");
            PrintWriter printWriter = response.getWriter();
            printWriter.append(JSON.toJSONString(result));
            printWriter.flush();
            printWriter.close();
        }

        if("/gif/produce".equals(uri) && ifLimit){//接口请求频繁度限制
            final long currentTime = System.currentTimeMillis();
            if (GifAppContext.ipTime.get(clientIP) == null){
                GifAppContext.ipTime.put(clientIP, currentTime);
                return true;
            }
            if (currentTime - GifAppContext.ipTime.get(clientIP) > limitTime * 1000){
                GifAppContext.ipTime.put(clientIP, currentTime);
                return true;
            }
            Result result = new Result();
            result.setStatus(WebStatusInfo.STATUS_FAIL);
            result.setMsg("Too much request");
            result.setResult("try in " + limitTime + "seconds");
            PrintWriter printWriter = response.getWriter();
            printWriter.append(JSON.toJSONString(result));
            printWriter.flush();
            printWriter.close();
            return false;
        }

        return true;
    }
}
