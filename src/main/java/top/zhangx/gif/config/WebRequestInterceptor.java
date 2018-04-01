package top.zhangx.gif.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.zhangx.gif.entity.AccessInfo;
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
        return true;
    }
}
