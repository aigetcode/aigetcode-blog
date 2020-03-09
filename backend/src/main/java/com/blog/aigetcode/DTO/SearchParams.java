package com.blog.aigetcode.DTO;

public class SearchParams {
    private String searchParam;

    public SearchParams() {
    }

    public SearchParams(String searchParam) {
        this.searchParam = searchParam;
    }

    public String getSearchParam() {
        return searchParam;
    }

    public void setSearchParam(String searchParam) {
        this.searchParam = searchParam;
    }
}
