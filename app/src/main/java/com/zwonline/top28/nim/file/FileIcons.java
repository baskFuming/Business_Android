package com.zwonline.top28.nim.file;

import com.netease.nim.uikit.common.util.file.FileUtil;
import com.zwonline.top28.R;

import java.util.HashMap;
import java.util.Map;


public class FileIcons {

    private static final Map<String, Integer> smallIconMap = new HashMap<String, Integer>();

    static {
        smallIconMap.put("xls", R.drawable.gt3logo);
        smallIconMap.put("ppt", R.drawable.gt3logo);
        smallIconMap.put("doc", R.drawable.gt3logo);
        smallIconMap.put("xlsx", R.drawable.gt3logo);
        smallIconMap.put("pptx", R.drawable.gt3logo);
        smallIconMap.put("docx", R.drawable.gt3logo);
        smallIconMap.put("pdf", R.drawable.gt3logo);
        smallIconMap.put("html", R.drawable.gt3logo);
        smallIconMap.put("htm", R.drawable.gt3logo);
        smallIconMap.put("txt", R.drawable.gt3logo);
        smallIconMap.put("rar", R.drawable.gt3logo);
        smallIconMap.put("zip", R.drawable.gt3logo);
        smallIconMap.put("7z", R.drawable.gt3logo);
        smallIconMap.put("mp4", R.drawable.gt3logo);
        smallIconMap.put("mp3", R.drawable.gt3logo);
        smallIconMap.put("png", R.drawable.gt3logo);
        smallIconMap.put("gif", R.drawable.gt3logo);
        smallIconMap.put("jpg", R.drawable.gt3logo);
        smallIconMap.put("jpeg", R.drawable.gt3logo);
    }

    private static final Map<String, Integer> bigIconMap = new HashMap<String, Integer>();

    static {
        bigIconMap.put("xls", R.drawable.gt3logo);
        bigIconMap.put("ppt", R.drawable.gt3logo);
        bigIconMap.put("doc", R.drawable.gt3logo);
        bigIconMap.put("xlsx", R.drawable.gt3logo);
        bigIconMap.put("pptx", R.drawable.gt3logo);
        bigIconMap.put("docx", R.drawable.gt3logo);
        bigIconMap.put("pdf", R.drawable.gt3logo);
        bigIconMap.put("html", R.drawable.gt3logo);
        bigIconMap.put("htm", R.drawable.gt3logo);
        bigIconMap.put("txt", R.drawable.gt3logo);
        bigIconMap.put("rar", R.drawable.gt3logo);
        bigIconMap.put("zip", R.drawable.gt3logo);
        bigIconMap.put("7z", R.drawable.gt3logo);
        bigIconMap.put("mp4", R.drawable.gt3logo);
        bigIconMap.put("mp3", R.drawable.gt3logo);
        bigIconMap.put("png", R.drawable.gt3logo);
        bigIconMap.put("gif", R.drawable.gt3logo);
        bigIconMap.put("jpg", R.drawable.gt3logo);
        bigIconMap.put("jpeg", R.drawable.gt3logo);
    }

    public static int smallIcon(String fileName) {
        String ext = FileUtil.getExtensionName(fileName).toLowerCase();
        Integer resId = smallIconMap.get(ext);
        if (resId == null) {
            return R.drawable.gt3logo;
        }

        return resId.intValue();
    }

    public static int bigIcon(String fileName) {
        String ext = FileUtil.getExtensionName(fileName).toLowerCase();
        Integer resId = bigIconMap.get(ext);
        if (resId == null) {
            return R.drawable.gt3logo;
        }

        return resId.intValue();
    }
}
