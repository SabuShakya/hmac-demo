package com.sabu.hmacdemo.filter;

import com.sabu.hmacdemo.constants.HmacConstants;
import com.sabu.hmacdemo.hmac.AuthHeader;
import com.sabu.hmacdemo.hmac.HmacSignatureBuilder;
import com.sabu.hmacdemo.repository.ApplicationConfigRepository;
import com.sabu.hmacdemo.util.CachedRequestWrapper;
import com.sabu.hmacdemo.util.HmacUtil;
import com.sabu.hmacdemo.constants.MsgConstant;
import com.sabu.hmacdemo.exception.NoContentFoundException;
import com.sabu.hmacdemo.exception.UnauthorizedException;
import com.sabu.hmacdemo.entities.ApplicationConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Sabu Shakya
 * @email <sabu.shakya@f1soft.com>
 * @createdDate 2021/04/21
 */
@Slf4j
public class HmacAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private ApplicationConfigRepository applicationConfigRepository;

    private CachedRequestWrapper requestWrapper;

    public HmacAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (!requiresAuthentication(httpServletRequest, httpServletResponse)) {
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        requestWrapper = new CachedRequestWrapper(httpServletRequest);

        Authentication authenticationResult = null;

        try {
            authenticationResult = attemptAuthentication(requestWrapper, httpServletResponse);
            if (Objects.isNull(authenticationResult))
                return;

        } catch (AuthenticationException exception) {
            log.error("Authentication failed Path : {}", requestWrapper.getRequestURI());
            unsuccessfulAuthentication(requestWrapper, httpServletResponse, exception);
        }
        successfulAuthentication(requestWrapper, httpServletResponse, chain, authenticationResult);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse) throws AuthenticationException,
            IOException, ServletException {

        final AuthHeader authHeader = HmacUtil.validateAndGetAuthHeader(httpServletRequest);

        if (Objects.isNull(authHeader)) {
            log.warn("Authorization Header is missing.");
            throw new UnauthorizedException(MsgConstant.TOKEN_NOT_FOUND);
        }

        String apiSecret = getApiSecret();

        byte[] contentAsByteArray = HmacUtil.extractPayload(httpServletRequest.getMethod(), requestWrapper);

        final HmacSignatureBuilder signatureBuilder = new HmacSignatureBuilder()
                .requestMethod(httpServletRequest.getMethod().toUpperCase())
                .path(httpServletRequest.getRequestURI())
                .contentType(httpServletRequest.getContentType())
                .nonce(authHeader.getNonce())
                .timeStamp(Long.parseLong(httpServletRequest.getHeader(HmacConstants.AUTHORIZATION_TIMESTAMP)))
                .payload(contentAsByteArray)
                .algorithm(authHeader.getAlgorithm())
                .apiKey(authHeader.getApiKey())
                .apiSecret(apiSecret);

        compareSignature(signatureBuilder, authHeader.getDigest());

        PreAuthenticatedAuthenticationToken preAuthenticatedAuthenticationToken =
                new PreAuthenticatedAuthenticationToken(authHeader, null);
        preAuthenticatedAuthenticationToken.setAuthenticated(true);
        return preAuthenticatedAuthenticationToken;

    }

    private String getApiSecret() {
        ApplicationConfig apiSecret = applicationConfigRepository.findByConfigKey(HmacConstants.API_SECRET);

        if (Objects.isNull(apiSecret))
            throw new NoContentFoundException("Api secret not found.");

        String secret = apiSecret.getConfigValue().substring(1, apiSecret.getConfigValue().length() - 1);
        return secret;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    public void compareSignature(HmacSignatureBuilder signatureBuilder, byte[] digest) {
        if (!signatureBuilder.isHashEquals(digest)) {
            log.error("INVALID DIGEST!");
            throw new BadCredentialsException(MsgConstant.UNAUTHORIZED_REQUEST);
        }
    }
}
