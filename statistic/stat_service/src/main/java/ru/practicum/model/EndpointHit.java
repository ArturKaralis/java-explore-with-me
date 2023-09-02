package ru.practicum.model;

import lombok.*;
import org.springframework.stereotype.Component;
import dto.ViewStats;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Component
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "endpoint_hits")
@SqlResultSetMapping(name = "mapperFromEndpointHitToViewStats",
        classes = @ConstructorResult(
                targetClass = ViewStats.class,
                columns = {@ColumnResult(name = "app", type = String.class),
                        @ColumnResult(name = "uri", type = String.class),
                        @ColumnResult(name = "hits", type = Long.class)}))
@NamedNativeQueries({
        @NamedNativeQuery(name = "findAll", resultSetMapping = "mapperFromEndpointHitToViewStats",
                query = "select APPS.NAME as app, uri, count(ip) as hits from ENDPOINT_HITS " +
                        "left join APPS on APPS.ID = ENDPOINT_HITS.APP_ID " +
                        "where timestamp between ?1 and ?2 and uri in ?3 group by APPS.NAME, uri order by hits desc"),
        @NamedNativeQuery(name = "findAllUniqueIp", resultSetMapping = "mapperFromEndpointHitToViewStats",
                query = "select APPS.NAME as app, uri, count(distinct ip) as hits from ENDPOINT_HITS " +
                        "left join APPS on APPS.ID = ENDPOINT_HITS.APP_ID " +
                        "where timestamp between ?1 and ?2 and uri in ?3 group by APPS.NAME, uri order by hits desc"),
        @NamedNativeQuery(name = "findAllIfNoUris", resultSetMapping = "mapperFromEndpointHitToViewStats",
                query = "select APPS.NAME as app, uri, count(ip) as hits from ENDPOINT_HITS " +
                        "left join APPS on APPS.ID = ENDPOINT_HITS.APP_ID " +
                        "where timestamp between ?1 and ?2 group by APPS.NAME, uri order by hits desc"),
        @NamedNativeQuery(name = "findAllIfNoUrisUnique", resultSetMapping = "mapperFromEndpointHitToViewStats",
                query = "select APPS.NAME as app, uri, count(distinct ip) as hits from ENDPOINT_HITS " +
                        "left join APPS on APPS.ID = ENDPOINT_HITS.APP_ID " +
                        "where timestamp between ?1 and ?2 group by APPS.NAME, uri order by hits desc")})
public class EndpointHit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "app_id")
    private App app;

    @Column(name = "uri")
    private String uri;

    @Column(name = "ip")
    private String ip;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
