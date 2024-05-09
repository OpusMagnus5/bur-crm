DO $$
DECLARE

    _opr_id BIGINT;

BEGIN

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'DOLNOŚLĄSKA AGENCJA ROZWOJU REGIONALNEGO SPÓŁKA AKCYJNA', 1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Dotacje na usługi rozwojowe dla dolnośląskich firm.', _opr_id, 1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Łódzka Agencja Rozwoju Regionalnego S.A.', 1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'BON - Apetyt na rozwój 2', _opr_id, 1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'MAŁOPOLSKA AGENCJA ROZWOJU REGIONALNEGO S.A.', 1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Małopolskie Bony Rozwojowe - Nowa Perspektywa', _opr_id, 1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'AKADEMIA HR – dostosuj firmę do wymagań jutra', _opr_id, 1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Fundacja Rozwoju Regionu Rabka', 1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Tarnowskie Bony Rozwojowe', _opr_id, 1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Bon dla Podhalańskiego Przedsiębiorcy 2', _opr_id, 1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Stowarzyszenie na Rzecz Szkoły Zarządzania i Handlu w Oświęcimiu', 1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'NetBon 2', _opr_id, 1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Miasto Nowy Sącz-Centrum Pozyskiwania Funduszy i Przedsiębiorczości', 1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Sądeckie Bony Szkoleniowe II', _opr_id, 1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'KDK Info spółka z ograniczoną odpowiedzialnością', 1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'AKADEMIA HR. Profesjonalne kadry HR kluczem do rozwoju przedsiębiorstw.', _opr_id, 1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Agencja Rozwoju Regionalnego MARR S.A.', 1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Akademia HR - czas na zmiany', _opr_id, 1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'ŁÓDZKA IZBA PRZEMYSŁOWO-HANDLOWA', 1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Akademia HR - doskonalenie kompetencji kadry zarządzającej zasobami ludzkimi', _opr_id, 1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Instytut ADN spółka z ograniczoną odpowiedzialnością sp.k.', 1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Akademia HR. Szkolenia i doradztwo z zakresu zarządzania zasobami ludzkimi', _opr_id, 1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'HRP GRANTS Sp. z o. o.', 1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Przepis na rozwój - Akademia HR', _opr_id, 1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Rzeszowska Agencja Rozwoju Regionalnego S.A.', 1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Generator Kompetencji 3.0 - Rozwój kwalif. i kompetencji pracowników MŚP oraz innych podmiotów', _opr_id, 1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Wojewódzki Urząd Pracy w Kielcach', 1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Pracownik kapitałem firmy. Postaw na jego rozwój z bazą usług rozwojowych', _opr_id, 1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Buduj swój Rozwój – Baza Usług Rozwojowych', _opr_id, 1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Krajowa Agencja Informacyjna INFO Spółka z ograniczoną odpowiedzialnością', 1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'EDUKACJA dla przyszłości. Usługi rozwojowe dla osób dorosłych z woj. warmińsko-mazurskiego', _opr_id, 1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Techpal Sp. z o.o.', 1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'AKADEMIA HR. Profesjonalne kadry HR kluczem do rozwoju przedsiębiorstw.', _opr_id, 1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Klucz do usług rozwojowych dla MŚP z województwa warmińsko-mazurskiego', _opr_id, 1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Wielkopolska Agencja Rozwoju Przedsiębiorczoś ci Sp. z o.o.', 1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Usługi rozwojowe dla Twojego biznesu', _opr_id, 1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'KRAJOWA IZBA GOSPODARCZA', 1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Usługi rozwojowe dla pracowników i pracodawców podregionu leszczyńskiego', _opr_id, 1);

END $$;