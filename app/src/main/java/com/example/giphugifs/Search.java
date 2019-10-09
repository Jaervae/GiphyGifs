package com.example.giphugifs;

public class Search {
    private String type;
    private String limit;
    private String offset;

    public Search(String type, String limit, String offset) {
        this.type = type;
        this.limit = limit;
        this.offset = offset;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }
}
