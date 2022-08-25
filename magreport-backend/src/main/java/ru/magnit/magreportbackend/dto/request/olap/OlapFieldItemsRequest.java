package ru.magnit.magreportbackend.dto.request.olap;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class OlapFieldItemsRequest {

    private Long jobId;
    private Long fieldId;
    private FilterGroup filterGroup;
    private Long from;
    private Long count;


    public int getEndPoint(){
        return (int) (from + count);
    }
}

