package dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViewStats {

    private String app;

    private String uri;

    private Long hits;
}
