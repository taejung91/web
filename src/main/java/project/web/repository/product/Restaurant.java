package project.web.repository.product;

import lombok.Getter;
import lombok.Setter;
import project.web.paging.CommonDTO;

import java.sql.Blob;
import java.sql.Date;
import java.sql.Time;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter @Setter
public class Restaurant extends CommonDTO {

    private int num;
    private String res_name;
    private String kind;
    private int price1;
    private int price2;
    private int price3;
    private String addr;
    private String tel;
    private String content;
    private String image;
    private String bestyn;
    private Date indate;
    private Date date;
    private Time time;
    private int qtt;

    public String setTelNum(String tel) throws Exception{

        Pattern tellPattern = Pattern.compile( "^(01\\d{1}|02|0505|0502|0506|0\\d{1,2})-?(\\d{3,4})-?(\\d{4})");

        Matcher matcher = tellPattern.matcher(tel);
        if(matcher.matches()) {
            //정규식에 적합하면 matcher.group으로 리턴

            tel = matcher.group(1) + "-" + matcher.group(2) + "-" + matcher.group(3);

            return tel;
        }else{
            //정규식에 적합하지 않으면 substring으로 휴대폰 번호 나누기

            String str1 = tel.substring(0, 3);
            String str2 = tel.substring(3, 7);
            String str3 = tel.substring(7, 11);

            tel = str1 + "-" + str2 + "-" + str3;

            return tel;
        }
    }

}
