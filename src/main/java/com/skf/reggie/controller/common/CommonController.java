package com.skf.reggie.controller.common;

import com.skf.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * desc:
 *
 * @author: skf
 * @date: 2022/07/26
 */
@RequestMapping("/common")
@RestController
@Slf4j
public class CommonController {
    @Value("${reggie.path}")
    String basePath;

    @PostMapping("/upload")
    public R upload(MultipartFile file) throws IOException {
        log.info("upload() called with parameters => 【file = {}】", file);
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + suffix;
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        file.transferTo(new File(basePath + fileName));
        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse httpServletResponse) {
        try {
            FileInputStream fileInputStream = new FileInputStream(basePath + name);
            ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            IOUtils.copy(fileInputStream, outputStream);
            httpServletResponse.setContentType("image/jpeg");
            //outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
