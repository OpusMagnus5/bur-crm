package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.service_provider.ServiceProviderDto;
import pl.bodzioch.damian.user.InnerUserDto;
import pl.bodzioch.damian.utils.CipherComponent;

import java.io.Serializable;
import java.time.LocalDateTime;

public record GetServiceProviderDetailsResponse(

        String id,
        String nip,
        String name,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        String creatorFirstName,
        String creatorLastName,
        String modifierFirstName,
        String modifierLastName

) implements Serializable {

    public GetServiceProviderDetailsResponse(ServiceProviderDto serviceProviderDto, CipherComponent cipher) {
        this(
                cipher.encryptMessage(serviceProviderDto.id().toString()),
                serviceProviderDto.nip().toString(),
                serviceProviderDto.name(),
                serviceProviderDto.createdAt(),
                serviceProviderDto.modifiedAt(),
                serviceProviderDto.getCreator().map(InnerUserDto::firstName).orElse(null),
                serviceProviderDto.getCreator().map(InnerUserDto::lastName).orElse(null),
                serviceProviderDto.getModifier().map(InnerUserDto::firstName).orElse(null),
                serviceProviderDto.getModifier().map(InnerUserDto::lastName).orElse(null)
        );
    }
}
