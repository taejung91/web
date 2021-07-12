package project.web.paging;

import lombok.Getter;
import lombok.Setter;
import project.web.paging.PaginationInfo;
import project.web.paging.Criteria;

@Getter @Setter
public class CommonDTO extends Criteria {

    /** 페이징 정보 */
    private PaginationInfo paginationInfo;

}
