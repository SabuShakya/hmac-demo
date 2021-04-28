package com.sabu.hmacdemo.restpath;

import org.springframework.security.web.util.matcher.*;

import static com.sabu.hmacdemo.restpath.PublicRestPath.NORMAL_URLS;
import static com.sabu.hmacdemo.restpath.PublicRestPath.PUBLIC_UI_URLS;


/**
 * @author Sabu Shakya
 * @email <sabu.shakya@f1soft.com>
 * @createdDate 2021/04/21
 */
public class RestPath {

    public static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
            PUBLIC_UI_URLS,
            NORMAL_URLS
    );

    public static final RequestMatcher NORMAL_AUTH_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/test")
    );

    public static final RequestMatcher HMAC_URLS = new AndRequestMatcher(
            new NegatedRequestMatcher(PUBLIC_URLS),
            new NegatedRequestMatcher(NORMAL_AUTH_URLS),
            new AntPathRequestMatcher("/hmac")
    );

    public static final RequestMatcher BASIC_AUTH_URLS = new AndRequestMatcher(
            new NegatedRequestMatcher(PUBLIC_URLS),
            new NegatedRequestMatcher(NORMAL_AUTH_URLS),
            new AntPathRequestMatcher("/barahi/basicTest")
    );
}
