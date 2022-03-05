package com.http.notify.lib.response;

//A builder for GenericResponse
public class ResponseUtil {

    public static final String APP_KEY = "enrollee";

    private ResponseUtil() {
        //disable constructor
    }

    public static class  GenericResponseUtil<T> {

        private final GenericResponse<T> response = new GenericResponse<>();

        public GenericResponseUtil<T> wrapResponse(T data) {
            response.setData(data);
            return this;
        }

        public GenericResponseUtil<T> setMessage(String message) {
            response.setMessage(message);
            return this;
        }

        public GenericResponseUtil<T> setSuccess() {
            response.setStatus(GenericResponse.SUCCESS_KEY);
            return this;
        }

        public GenericResponseUtil<T> setFailure() {
            response.setStatus(GenericResponse.FAILED_KEY);
            return this;
        }

        public GenericResponse<T> build() {
            return response;
        }
    }



}
