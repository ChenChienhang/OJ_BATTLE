package com.team10.ojbattle.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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


    /**
     * 生成Json数据和头信息
     *
     * @param params
     * @return
     */
    private HttpEntity<String> generatePostJson(Map<String, Object> params) {
        //都可以在这里追加头信息
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Judge-Server-Token", getSHA256Str(token));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(JSON.toJSONString(params), httpHeaders);
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

    /**
     * 判题
     * @param src
     * @param languageConfigEnum
     * @param maxCpuTime
     * @param maxMemory
     * @param testCaseId
     * @param output
     * @param testCase
     * @param spjVersion
     * @param spjConfig
     * @param spjCompileConfig
     * @param spjSrc
     * @return
     */
    public JSONObject judge(@NotNull String src, @NotNull Map<String, Object> languageConfigEnum,
                            int maxCpuTime, int maxMemory,
                            @NotNull String testCaseId, Boolean output, String testCase,
                            String spjVersion, String spjConfig,
                            String spjCompileConfig, String spjSrc) {
        Map<String, Object> data = new HashMap<>(10);
        data.put("language_config", languageConfigEnum);
        data.put("src", src);
        data.put("max_cpu_time", maxCpuTime);
        data.put("max_memory", maxMemory);
        data.put("test_case_id", testCaseId);
        Optional.ofNullable(testCase).ifPresent(e -> data.put("test_case", e));
        Optional.ofNullable(spjVersion).ifPresent(e -> data.put("spj_version", e));
        Optional.ofNullable(spjConfig).ifPresent(e -> data.put("spj_config", e));
        Optional.ofNullable(spjCompileConfig).ifPresent(e -> data.put("spj_compile_config", e));
        Optional.ofNullable(spjSrc).ifPresent(e -> data.put("spj_src", e));
        Optional.ofNullable(output).ifPresent(e -> data.put("output", e));
        System.out.println(JSON.toJSONString(data));

        ResponseEntity<JSONObject> apiResponse = restTemplate.postForEntity
                (
                        serverBaseUrl + "/judge",
                        generatePostJson(data),
                        JSONObject.class
                );
        return apiResponse.getBody();
    }
}

