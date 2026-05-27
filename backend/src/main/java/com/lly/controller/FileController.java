package com.lly.controller;

import com.lly.utils.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${file.upload-path:./uploads/}")
    private String uploadPath;

    @PostMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return R.error("文件为空");
        String original = file.getOriginalFilename();
        String ext = "";
        if (original != null && original.contains(".")) ext = original.substring(original.lastIndexOf("."));
        String dateDir = new SimpleDateFormat("yyyyMM").format(new Date());
        File dir = new File(uploadPath + dateDir);
        if (!dir.exists()) dir.mkdirs();
        String fileName = UUID.randomUUID().toString().replace("-", "") + ext;
        File target = new File(dir, fileName);
        file.transferTo(target);
        String url = "/api/uploads/" + dateDir + "/" + fileName;
        return R.ok().put("url", url).put("fileName", original);
    }
}
