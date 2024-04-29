package pl.bodzioch.damian.service_provider;

import java.util.UUID;

//Rejestr
record ServiceProvider(

        Long spr_id,
        UUID spr_uuid,
        Integer spr_version,
        Long spr_bur_id,
        String spr_name,
        Long spr_nip
) {


}
