package com.olegro.cristianpostcards.data.retrofit.json_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {
    @Expose
    @SerializedName("isError")
    public boolean isError;

    @Expose
    @SerializedName("errorData")
    public String errorData;
}
