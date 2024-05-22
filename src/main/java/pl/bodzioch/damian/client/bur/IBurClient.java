package pl.bodzioch.damian.client.bur;

public interface IBurClient {

    BurServiceProviderDto getServiceProvider(Long nip);

    BurServiceDto getService(Long id);
}
