package com.github.Sigma429.pojo.entity;

import lombok.Data;
/**
 * ClassName:Result
 * Package:com.github.Sigma429.pojo.entity
 * Description:
 * @Author:14亿少女的梦-Sigma429
 * @Create:2023/08/23 - 14:48
 * @Version:v1.0
 */
@Data
public class Result {

    public static final Integer SUCCESS = 200;
    public static final Integer ERROR = 500;
    private Integer code;
    private String msg;
    private Object data;

    public static Result success() {
        Result result = new Result();
        result.setCode(SUCCESS);
        result.setMsg("success");
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(SUCCESS);
        result.setMsg("success");
        result.setData(data);
        return result;
    }

    public static Result error() {
        Result result = new Result();
        result.setCode(ERROR);
        result.setMsg("Internal error");
        return result;
    }

    public static Result error(String msg) {
        Result result = new Result();
        result.setCode(ERROR);
        result.setMsg(msg);
        return result;
    }

    public static Result error(Integer code,String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
