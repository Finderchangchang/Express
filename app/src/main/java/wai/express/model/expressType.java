package wai.express.model;

import cn.bmob.v3.BmobObject;

/**
 * 快递字典
 * Created by Finder丶畅畅 on 2017/2/27 20:17
 * QQ群481606175
 */

public class expressType extends BmobObject {
    int id;
    String typeName;//快递名称
    String typeId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
