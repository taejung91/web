package project.web.repository.members;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter @Setter
public class Member {

    private String id;
    private String pw;
    private String name;
    private String birth;
    private String phone;
    private String addr;
    private Timestamp indate;


    public String setPhoneNum(String phone) throws Exception{

        Pattern tellPattern = Pattern.compile( "^(01\\d{1}|02|0505|0502|0506|0\\d{1,2})-?(\\d{3,4})-?(\\d{4})");

        Matcher matcher = tellPattern.matcher(phone);
        if(matcher.matches()) {
            //정규식에 적합하면 matcher.group으로 리턴

            phone = matcher.group(1) + "-" + matcher.group(2) + "-" + matcher.group(3);

            return phone;
        }else{
            //정규식에 적합하지 않으면 substring으로 휴대폰 번호 나누기

            String str1 = phone.substring(0, 3);
            String str2 = phone.substring(3, 7);
            String str3 = phone.substring(7, 11);

            phone = str1 + "-" + str2 + "-" + str3;

            return phone;
        }
    }
}
