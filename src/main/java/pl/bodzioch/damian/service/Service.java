package pl.bodzioch.damian.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

//TODO general error handling on front which logging on db
//TO NIE JEST KARTA TYLKO USŁUGA, KILKA FIRM MOZE SIĘ ZAPISAC
record Service(

        Long srv_id,
        UUID srv_uuid,
        Long srv_service_card_bur_id,
        String srv_name,
        String srv_number,
        ServiceType srv_service_type,
        LocalDateTime srv_start_date,
        LocalDateTime srv_end_date,
        Long srv_service_provider_id,
        Integer srv_number_of_participants,
        Long srv_program_id,
        Long srv_customer_id,
        List<Long> srv_cst_ids,
        Long srv_intermediary
        /*ServiceProvider serviceProvider, //burId, Nazwa, Nip
        Program program, // pod nim opeartor a w nim kontakt do operatora
        Customer customer, //możeby być kilka firm które zapiszą się na tą samą kartę
        List<Coach> coach, // moze byc kilku trenerów
        Intermediary intermediary*/
) {


}
