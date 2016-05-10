package pers.zr.magic.dao.page;

import java.util.List;

/**
 * Created by zhurong on 2016-5-10.
 */
public class PageModel {

    private int pageNumber;

    private int pageSize;

    public PageModel(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
