package utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
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
        headers.put("Authorization", "APPCODE " + "ff6306a133ff41c8bdc4b58fac64dd8a");
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        String respose =  HttpClientUtil.instance().httpPostForm(url,bodys,headers,"UTF-8",1000);
        // {"code":"000000","success":"true","message":"\u4e00\u81f4","serialNo":"100018_1531392044"}
        System.out.println(respose);
        String json = "";

    }


    @Test
    @DisplayName("身份证验证（校验）返头像照片")
    void myssTest() {
        String url = "http://jisusfzfzp.market.alicloudapi.com/idcardverify2/verify";
        Map<String, String> bodys = new HashMap<>();
        bodys.put("idcard", "420116198608046615");
        bodys.put("realname", "王寅生");

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + "ff6306a133ff41c8bdc4b58fac64dd8a");

        String respose =  HttpClientUtil.instance().httpGet(url,bodys,headers,1000);
        // {"status":"0","msg":"ok","result":{"idcard":"420116198608046615","realname":"王寅生","province":"湖北","city":"武汉","town":"黄陂区","sex":"男","birth":"1986年08月04日","pic":"\/9j\/4AAQSkZJRgABAgAAAQABAAD==","verifystatus":"0","verifymsg":"恭喜您，身份证校验一致！"}}
        System.out.println(respose);

    }


    @Test
    @DisplayName("身份证识别")
    void testIdCardPic() {
        String url = "http://idcardocr.market.alicloudapi.com/id_card_ocr";
        Map<String, String> bodys = new HashMap<>();
        Path path= Paths.get("src/test/resources/id.jpg");
        ImgBase64Util imgUtil = new ImgBase64Util();


        bodys.put("imgData", imgUtil.base64Encoding(imgUtil.readFile(path)));
        bodys.put("type", "1");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + "c4d9656ac0c44570b8b43e1c0e80e81f");

        String respose =  HttpClientUtil.instance().httpPostForm(url,bodys,headers,"UTF-8",1000);
        System.out.println(respose);
    }
}
