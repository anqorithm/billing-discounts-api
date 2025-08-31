package sa.billing.discounts.presentation.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ApiResponse<T> {
    private String message;
    private String status;
    private T data;
    private Map<String, Object> meta;
    
    public ApiResponse() {
        this.meta = new HashMap<>();
    }
    
    public ApiResponse(String message, String status, T data, Map<String, Object> meta) {
        this.message = message;
        this.status = status;
        this.data = data;
        this.meta = meta != null ? meta : new HashMap<>();
    }
    
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("", "success", data, null);
    }
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, "success", data, null);
    }
    
    public static <T> ApiResponse<T> error(String message) {
        Map<String, Object> meta = new HashMap<>();
        meta.put("error", message);
        return new ApiResponse<>(message, "fail", null, meta);
    }
    
    public static <T> ApiResponse<T> error(String message, String errorCode) {
        Map<String, Object> meta = new HashMap<>();
        meta.put("error", message);
        meta.put("errorCode", errorCode);
        return new ApiResponse<>(message, "fail", null, meta);
    }
    
    public static <T> ApiResponse<T> pending(String message, T data) {
        return new ApiResponse<>(message, "pending", data, null);
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public Map<String, Object> getMeta() {
        return meta;
    }
    
    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiResponse<?> that = (ApiResponse<?>) o;
        return Objects.equals(message, that.message) && 
               Objects.equals(status, that.status) && 
               Objects.equals(data, that.data) && 
               Objects.equals(meta, that.meta);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(message, status, data, meta);
    }
    
    @Override
    public String toString() {
        return "ApiResponse{" +
                "message='" + message + '\'' +
                ", status='" + status + '\'' +
                ", data=" + data +
                ", meta=" + meta +
                '}';
    }
}