package com.maple.maplepicturebackend.manager;

import com.maple.maplepicturebackend.config.CosClientConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

@Component
public class CosManager {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;

    /**
     * 上传对象
     * @param key
     * @param file
     * @return
     * @throws CosClientException
     * @throws CosServiceException
     */
    public PutObjectResult putObject(String key, File file)
            throws CosClientException, CosServiceException{
        return cosClient.putObject(new PutObjectRequest(cosClientConfig.getBucket(), key, file));
    }

    /**
     * 下载对象
     * @param key
     * @return
     * @throws CosClientException
     * @throws CosServiceException
     */
    public COSObject getObject(String key)
            throws CosClientException, CosServiceException{
    return cosClient.getObject(new GetObjectRequest(cosClientConfig.getBucket(), key));
    }

    /**
     * 上传对象（附带图片信息）
     * @param key
     * @param file
     * @return
     * @throws CosClientException
     * @throws CosServiceException
     */
//    public PutObjectResult putObject(String key, File file)
//            throws CosClientException, CosServiceException{
//        // 对图片处理
//
//        return cosClient.putObject(new PutObjectRequest(cosClientConfig.getBucket(), key, file));
//    }
}
