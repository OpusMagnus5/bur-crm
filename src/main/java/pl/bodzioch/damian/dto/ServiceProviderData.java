package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.service_provider.ServiceProviderDto;
import pl.bodzioch.damian.utils.CipherComponent;

public record ServiceProviderData(
        String id,
        String nip,
        String name
) {

    public ServiceProviderData(ServiceProviderDto providerDto, CipherComponent cipher) {
        this(
                cipher.encryptMessage(providerDto.id().toString()),
                providerDto.nip().toString(),
                providerDto.name()
        );
    }
}
