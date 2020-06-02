package com.team10.ojbattle.component;

import com.alibaba.fastjson.JSONObject;
import com.team10.ojbattle.common.enums.LanguageConfigEnum;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author: 陈健航
 * @description:
 * @since: 2020/5/30 22:59
 * @version: 1.0
 */
@Component
public class JudgeServerClient {

    @Value("${token}")
    private String token;

    @Value("${server_base_url}")
    private String serverBaseUrl;

    @Autowired
    private RestTemplate restTemplate;


    private HttpEntity<JSONObject> generatePostJson(JSONObject jsonMap) {
        //都可以在这里追加头信息
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Judge-Server-Token", getSHA256Str(token));
        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
        httpHeaders.setContentType(type);
        return new HttpEntity<>(jsonMap, httpHeaders);
    }

    private String getSHA256Str(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
            encodeStr = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    public String judge(@NotNull String src, @NotNull LanguageConfigEnum languageConfigEnum,
                        @NotNull String maxCpuTime, @NotNull String maxMemory,
                        @NotNull String testCaseId, @NotNull boolean output, String testCase,
                        String spjVersion, String spjConfig,
                        String spjCompileConfig, String spjSrc) {
        JSONObject data = new JSONObject();
        data.put("language_config", languageConfigEnum.getValue());
        data.put("src", src);
        data.put("maxCpuTime", maxCpuTime);
        data.put("maxMemory", maxMemory);
        data.put("test_case_id", testCaseId);
        data.put("testCase", testCase);
        data.put("spjVersion", spjVersion);
        data.put("spjConfig", spjConfig);
        data.put("spjCompileConfig", spjCompileConfig);
        data.put("spjSrc", spjSrc);
        data.put("output", output);
        String uri = "http://127.0.0.1:80";
        ResponseEntity<String> apiResponse = restTemplate.postForEntity
                (
                        uri,
                        generatePostJson(data),
                        String.class
                );
        return apiResponse.getBody();
    }
}
