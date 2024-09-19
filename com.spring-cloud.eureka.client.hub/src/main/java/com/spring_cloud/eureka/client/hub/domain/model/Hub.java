package com.spring_cloud.eureka.client.hub.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hub")
public class Hub extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "zipcode", nullable = false)
    private String zipcode;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @OneToMany(mappedBy = "startHub", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HubRoute> startRoutes = new ArrayList<>();

    @OneToMany(mappedBy = "endHub", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HubRoute> endRoutes = new ArrayList<>();

    public void addRoute(HubRoute route) {
        if (route.getStartHub() != null && route.getStartHub().equals(this)) {
            startRoutes.add(route);
        }
        if (route.getEndHub() != null && route.getEndHub().equals(this)) {
            endRoutes.add(route);
        }
    }

    public void removeRoute(HubRoute route) {
        if (route.getStartHub() != null && route.getStartHub().equals(this)) {
            this.startRoutes.remove(route);
        }
        if (route.getEndHub() != null && route.getEndHub().equals(this)) {
            this.endRoutes.remove(route);
        }
    }

}
