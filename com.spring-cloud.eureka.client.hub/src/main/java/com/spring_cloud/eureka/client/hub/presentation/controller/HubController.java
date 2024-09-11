package com.spring_cloud.eureka.client.hub.presentation.controller;

import com.spring_cloud.eureka.client.hub.application.dtos.HubResponseDTO;
import com.spring_cloud.eureka.client.hub.application.dtos.HubRouteResponseDTO;
import com.spring_cloud.eureka.client.hub.application.service.HubService;
import com.spring_cloud.eureka.client.hub.presentation.dtos.HubRequestDTO;
import com.spring_cloud.eureka.client.hub.presentation.dtos.HubRouteRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hubs")
@RequiredArgsConstructor
public class HubController {

    private final HubService hubService;

    @PostMapping
    public ResponseEntity<HubResponseDTO> createHub(@RequestBody HubRequestDTO hubRequestDTO) {
        HubResponseDTO hubResponseDTO = hubService.createHub(hubRequestDTO);
        return new ResponseEntity<>(hubResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/routes")
    public ResponseEntity<HubRouteResponseDTO> addRouteToHub(
            @RequestBody HubRouteRequestDTO hubRouteRequestDTO) {
        HubRouteResponseDTO hubRouteResponseDTO = hubService.addRouteToHub(hubRouteRequestDTO);
        return new ResponseEntity<>(hubRouteResponseDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{hubId}/routes/{routeId}")
    public ResponseEntity<Void> removeRouteFromHub(
            @PathVariable UUID hubId,
            @PathVariable UUID routeId) {
        hubService.removeRouteFromHub(hubId, routeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{hubId}")
    public ResponseEntity<HubResponseDTO> getHub(@PathVariable UUID hubId) {
        HubResponseDTO hubResponseDTO = hubService.getHub(hubId);
        return new ResponseEntity<>(hubResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/{hubId}/routes")
    public ResponseEntity<List<HubRouteResponseDTO>> getHubRoutes(@PathVariable UUID hubId) {
        List<HubRouteResponseDTO> hubRouteResponseDTOs = hubService.getHubRoutes(hubId);
        return new ResponseEntity<>(hubRouteResponseDTOs, HttpStatus.OK);
    }

    @PutMapping("/{hubId}")
    public ResponseEntity<HubResponseDTO> updateHub(
            @PathVariable UUID hubId,
            @RequestBody HubRequestDTO hubRequestDTO) {
        HubResponseDTO hubResponseDTO = hubService.updateHub(hubId, hubRequestDTO);
        return new ResponseEntity<>(hubResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{hubId}")
    public ResponseEntity<Void> deleteHub(@PathVariable UUID hubId) {
        hubService.deleteHub(hubId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{hubId}/restore")
    public ResponseEntity<Void> restoreHub(@PathVariable UUID hubId) {
        hubService.restoreHub(hubId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{hubId}/permanent")
    public ResponseEntity<Void> deleteHubPermanently(@PathVariable UUID hubId) {
        hubService.deleteHubPermanently(hubId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
