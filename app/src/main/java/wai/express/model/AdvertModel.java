package wai.express.model;

import cn.bmob.v3.BmobObject;

/**
 * 广告内容
 * Created by Administrator on 2017/3/10.
 */

public class AdvertModel extends BmobObject {
    String content;
    String type;//1.广告。2.图片链接

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
