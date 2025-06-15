package com.maple.maplepicturebackend.manager.upload;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.maple.maplepicture.infrastructure.exception.BusinessException;
import com.maple.maplepicture.infrastructure.exception.ErrorCode;
import com.maple.maplepicture.infrastructure.exception.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * 通过url上传文件
 */
@Slf4j
@Service
public class UrlPictureUpload extends PictureUploadTemplate {
    @Override
    protected void processFile(Object inputSource, File file) throws IOException {
        String fileUrl = (String) inputSource;
        // 下载文件到临时目录
        HttpUtil.downloadFile(fileUrl, file);
    }

    @Override
    protected String getOriginalFilename(Object inputSource) {
        String fileUrl = (String) inputSource;
        return FileUtil.getName(fileUrl);
    }

    @Override
    protected String validPicture(Object inputSource) {
        String fileUrl = (String) inputSource;
        // 1. 校验非空
        ThrowUtils.throwIf(StrUtil.isBlank(fileUrl), ErrorCode.PARAMS_ERROR, "文件地址为空");
        // 2. 校验URL格式
        try {
            new URL(fileUrl);
        } catch (MalformedURLException e) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 3. 校验URL的协议
        ThrowUtils.throwIf(!fileUrl.startsWith("http://") && !fileUrl.startsWith("https://"),
                ErrorCode.PARAMS_ERROR, "仅支持 HTTP 或 HTTPS 协议的文件地址");

        // 4. 发送HEAD请求验证文件是否存在
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpUtil.createRequest(Method.HEAD, fileUrl)
                    .execute();
            // 未正常返回， 无需执行其他判断（有的不支持HEAD请求）
            if (httpResponse.getStatus() != HttpStatus.HTTP_OK) {
                return null;
            }
            // 5. 文件存在，文件类型校验
            String contentType = httpResponse.header("Content-Type");
            // 文件类型不为空，校验是否合法
            if (StrUtil.isNotBlank(contentType)) {
                // 允许的图片类型
                final List<String> ALLOW_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/jpg", "image/png", "image/webp");
                ThrowUtils.throwIf(!ALLOW_CONTENT_TYPES.contains(contentType.toLowerCase()),
                        ErrorCode.PARAMS_ERROR, "文件类型错误");
            }
            // 6. 文件存在，文件大小校验
            String contentLengthStr = httpResponse.header("Content-Length");
            if (StrUtil.isNotBlank(contentLengthStr)) {
                try {
                    long contentLength = Long.parseLong(contentLengthStr);
                    final long ONE_M = 1024 * 1024; // 1M大小
                    ThrowUtils.throwIf(contentLength > 2 * ONE_M, ErrorCode.PARAMS_ERROR, "");
                } catch (NumberFormatException e) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小格式异常");
                }

            }
            return getSuffixFromContentType(contentType);

        } finally {
            // 释放资源
            if (httpResponse != null) {
                httpResponse.close();
            }
        }
    }
}
