package com.ducetech.hadmin.common.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author lisx
 * @create 2017-09-06 20:23
 **/
@Component
@Data
public class DucetechProperties {
    @Value("${ducetech.upload}")
    private String upload;
    @Value("${ducetech.upload}")
    private String uploadChunk;
    @Value("${ducetech.http}")
    private String http;
    @Value("${ducetech.icon}")
    private String icon;
}
