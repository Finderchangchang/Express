package wai.express.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by Finder丶畅畅 on 2017/2/27 20:16
 * QQ群481606175
 */

public class service extends BmobObject implements Serializable {
    BmobUser user;//购买用户
    BmobUser courier;//抢单骑士信息
    String user_tel;//用户电话
    String state;//订单状态
    String remark;//取件信息
    String express_name;//快递员电话
    String express_address;//送货地址
    Double good_price;
    String priceType;

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getExpress_address() {
        return express_address;
    }

    public void setExpress_address(String express_address) {
        this.express_address = express_address;
    }

    public Double getGood_price() {
        return good_price;
    }

    public void setGood_price(Double good_price) {
        this.good_price = good_price;
    }

    public String getExpress_name() {
        return express_name;
    }

    public void setExpress_name(String express_name) {
        this.express_name = express_name;
    }

    public BmobUser getUser() {
        return user;
    }

    public void setUser(BmobUser user) {
        this.user = user;
    }

    public BmobUser getCourier() {
        return courier;
    }

    public void setCourier(BmobUser courier) {
        this.courier = courier;
    }

    public String getUser_tel() {
        return user_tel;
    }

    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
