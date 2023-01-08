package ru.sevn.mytutor.views.course;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class OperationResult {
    @NonNull
    private OperationResultType resultType;
    private String message;
    
    public boolean isError() {
        return resultType == OperationResultType.ERROR;
    }
    
    public static OperationResult error(String msg) {
        return OperationResult.builder()
                .resultType(OperationResultType.ERROR)
                .message(msg)
                .build();
    }
    public static OperationResult added() {
        return OperationResult.builder()
                .resultType(OperationResultType.ADDED)
                .build();
    }
    public static OperationResult modified() {
        return OperationResult.builder()
                .resultType(OperationResultType.MODIFIED)
                .build();
    }
}
