package pl.bodzioch.damian.service;

import pl.bodzioch.damian.dto.GetServiceFromBurResponse;

interface IServiceService {
    GetServiceFromBurResponse getServiceFromBur(String serviceNumber);
}
