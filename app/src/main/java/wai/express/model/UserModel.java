package wai.express.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by Finder丶畅畅 on 2017/3/8 19:32
 * QQ群481606175
 */

public class UserModel extends BmobUser {
    String realName;
    String userType;//用户分类

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
