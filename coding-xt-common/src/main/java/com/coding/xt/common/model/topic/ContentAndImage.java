package com.coding.xt.common.model.topic;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//内容和图像
@Data
public class ContentAndImage {
    private String content;
    private List<String> imageList;

    public static ContentAndImage deal(String str){
        if (StringUtils.isEmpty(str)){
            return null;
        }
        List<String> imageList = new ArrayList<>();
        String content = str;
        if (str.contains("http")){
            //多个图片的模板规则是 [http://xxx.png,http://nnn.png]
            if (str.contains("[http")){
                //多个图片
                Pattern pt = Pattern.compile("http?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
                Matcher mt = pt.matcher(str);
                if (mt.find()) {
                    String imagesStr = mt.group();
                    String[] images = imagesStr.split(",");
                    imageList.addAll(Arrays.asList(images));
                }
            }else {
                Pattern pt = Pattern.compile("http?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
                Matcher mt = pt.matcher(str);
                if (mt.find()) {
                    imageList.add(mt.group());
                }
                content = str.split("http")[0];
            }
        }
        ContentAndImage contentAndImageModel = new ContentAndImage();
        contentAndImageModel.setContent(content);
        contentAndImageModel.setImageList(imageList);
        return contentAndImageModel;
    }

    public static void main(String[] args) {
        Pattern pt = Pattern.compile("http?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
        Matcher mt = pt.matcher("河姆渡黑陶钵[http://static.lzxtedu.com/7a1/11a.png,http://static.lzxtedu.com/7a1/11b.png]");
        if (mt.find()) {
            System.out.println(mt.group());
        }
    }
}
