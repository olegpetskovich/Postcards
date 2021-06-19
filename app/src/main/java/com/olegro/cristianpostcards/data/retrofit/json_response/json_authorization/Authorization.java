package com.olegro.cristianpostcards.data.retrofit.json_response.json_authorization;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.olegro.cristianpostcards.data.retrofit.json_response.Response;

public class Authorization  extends Response {
    @Expose
    @SerializedName("successData")
    public String successData;
}
