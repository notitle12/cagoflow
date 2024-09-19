package com.spring_cloud.eureka.client.hub.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hub_route")
public class HubRoute extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "start_hub_id", nullable = false)
    private Hub startHub;

    @ManyToOne
    @JoinColumn(name = "end_hub_id", nullable = false)
    private Hub endHub;

    @Column(name = "estimated_time", nullable = false)
    private Double estimatedTime;

    @Column(name = "route_details", nullable = false)
    private String routeDetails;

    public void setStartHub(Hub startHub) {
        if (this.startHub != null) {
            this.startHub.removeRoute(this);
        }
        this.startHub = startHub;
        if (startHub != null) {
            startHub.addRoute(this);
        }
    }

    public void setEndHub(Hub endHub) {
        if (this.endHub != null) {
            this.endHub.removeRoute(this);
        }
        this.endHub = endHub;
        if (endHub != null) {
            endHub.addRoute(this);
        }
    }
}
