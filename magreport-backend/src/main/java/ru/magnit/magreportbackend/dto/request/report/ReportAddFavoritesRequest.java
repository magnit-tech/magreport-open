package ru.magnit.magreportbackend.dto.request.report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class ReportAddFavoritesRequest {

    private Long folderId;
    private Long reportId;
}
