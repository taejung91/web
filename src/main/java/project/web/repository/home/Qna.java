package project.web.repository.home;

import lombok.Getter;
import lombok.Setter;
import project.web.paging.CommonDTO;

import java.sql.Date;

@Getter @Setter
public class Qna extends CommonDTO {

    private int num;
    private String pw;
    private String title;
    private String content;
    private int inquiry;
    private String writer;
    private String image;
    private Date indate;
    private String reply;
    private int reply_inquiry;
    private Date reply_indate;

    //private String search;
   // private String searchKind;



}
