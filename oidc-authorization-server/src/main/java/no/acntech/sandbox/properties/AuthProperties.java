package no.acntech.sandbox.properties;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.util.*;

/**
 * @description：
 * @author: chengxingjun
 * @version: 1.0.0
 * @date: 2021-08-04 5:25 下午
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "auth")
public class AuthProperties implements InitializingBean {

    /**
     * 白名单url，符合的，就不判断token、企业实例等
     */
    private Set<String> whitelists = new HashSet<>();

    private String secretKey = "jwtTokenKey";

    /**
     * token有效秒
     */
    private Long jwtTokenTtl = 5*3600l;

    /**
     * 验证token的服务地址前缀
     * 相对地址参考TokenApi
     */
    private String tokenServerUri;

    /**
     * 用于验证校验resource服务器是否有权限调用AuthorizeServer
     * 目前未使用
     */
    private String clientId;

    /**
     * 有效访问子网掩码
     * 10.201.0.0/24
     */
    private List<String> accessIpList;

    /**
     * 认证用户信息缓存键值
     */
    private String authCacheName = "login:userinfo";

    /**
     * 调试开关，true时只需要设置header[userAccount={username}]即可设置为某用户，在该用户已经登陆存在缓存的情况
     */
    private boolean accessByUsername = true;

    /**
     * 登录失败次数
     * 第3次锁定，第三次仍然不会提示，到第4次提示已经锁定
     */
    private int rate = 3;

    /**
     * 60秒内
     */
    private int rateIntervalSecond = 60;

    private int lockDownSecond = 3600;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("{}", toString());
        if (CollectionUtils.isEmpty(whitelists)) {
            whitelists.add("/uaa/**");
            whitelists.add("/jwk/**");
            whitelists.add("/jwt/**");
            whitelists.add("/error");
            whitelists.add("/actuator");
            whitelists.add("/actuator/**");
            whitelists.add("/favicon.ico");
        }
    }
}
