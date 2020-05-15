package com.team10.ojbattle.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: 陈健航
 * @description: 封装文件上传信息
 * @since: 2020/3/21 19:53
 * @version: 1.0
 */
@Data
@AllArgsConstructor
public class FastDFSFile {

    /**
     * 文件名字
     */
    private String name;

    /**
     * 文件内容
     */
    private byte[] content;

    /**
     * 文件扩展名
     */
    private String ext;

    /**
     * 文件md5摘要值
     */
    private String md5;

    /**
     * 文件创建作者
     */
    private String author;
}
