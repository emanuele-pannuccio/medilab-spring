package com.apps.medilab.response;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class ApiResponse {
    private int status;
    private Object response;
}
