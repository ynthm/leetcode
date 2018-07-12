package utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class TestHttpClientUtil {
    @Test
    @DisplayName("身份证二要素")
    void myFirstTest() {
        String url = "http://yaosu.market.alicloudapi.com/api/zmxy/zmxy_two/ali_two";
        Map<String, String> bodys = new HashMap<>();
        bodys.put("certNo", "420116198608046615");
        bodys.put("name", "王寅生");

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + "83359fd73fe94948385f570e3c139105");
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        String respose =  HttpClientUtil.instance().httpPostForm(url,bodys,headers,"UTF-8",1000);
        System.out.println(respose);
    }


    @Test
    @DisplayName("证实名认证-身份证二要素一致性验证")
    void myssTest() {
        String url = "https://idcardpro.market.alicloudapi.com/idcard";
        Map<String, String> bodys = new HashMap<>();
        bodys.put("cardNo", "420116198608046615");
        bodys.put("realName", "王寅生");

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + "c4d9656ac0c44570b8b43e1c0e80e81f");

        String respose =  HttpClientUtil.instance().httpPostForm(url,bodys,headers,"UTF-8",1000);
        System.out.println(respose);
    }
}
