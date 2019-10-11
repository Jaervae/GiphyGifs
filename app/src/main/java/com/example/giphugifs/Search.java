package com.example.giphugifs;

public class Search {
    private String type;
    private String limit;
    private String offset;
    private int idType;

    public Search(String type, String limit, String offset,int idType) {
        this.type = type;
        this.limit = limit;
        this.offset = offset;
        this.idType = idType;
    }

    public String getType() {
        return type;
    }

    public String getLimit() {
        return limit;
    }

    public String getOffset() {
        return offset;
    }

    public int getIdType() {
        return idType;
    }

}
