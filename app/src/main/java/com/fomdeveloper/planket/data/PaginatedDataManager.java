package com.fomdeveloper.planket.data;


/**
 * Created by Fernando on 02/07/16.
 */
public class PaginatedDataManager {

    public static final int FIRST_PAGE_DEFAULT = 1;
    private int currentPage;
    private int totalPages = -1;


    public PaginatedDataManager() {
    }

    public void resetPage() {
        this.currentPage = 0;
    }

    public int getNextPageToLoad() {
        return currentPage + 1;
    }

    public int addPageLoaded() {
        int pageLoaded = getNextPageToLoad();
        currentPage = pageLoaded;
        return pageLoaded;
    }


    public boolean areTotalPagesLoaded(){
        if (totalPages > 0 && getNextPageToLoad() > totalPages){
            return true;
        }
        return false;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean isLoadingFirstPage(){
        return getNextPageToLoad() == PaginatedDataManager.FIRST_PAGE_DEFAULT;
    }
}
