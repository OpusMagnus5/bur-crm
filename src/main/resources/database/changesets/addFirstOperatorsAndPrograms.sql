DO $$
DECLARE

    _opr_id BIGINT;

BEGIN

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'WROCŁAWSKA AGENCJA ROZWOJU REGIONALNEGO SPÓŁKA AKCYJNA', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Dotacje na usługi rozwojowe dla dolnośląskich firm.', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'AGENCJA ROZWOJU REGIONALNEGO AGROREG SPÓŁKA AKCYJNA W NOWEJ RUDZIE', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'NOWOCZESNE KADRY DOLNEGO ŚLĄSKA – SYSTEM FINANSOWANIA USŁUG ROZWOJOWYCH DLA MIESZKAŃCÓW SUBREG. WAŁB', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'DOLNOŚLĄSKA AGENCJA WSPÓŁPRACY GOSPODARCZEJ SPÓŁKA Z OGRANICZONĄ ODPOWIEDZIALNOŚCIĄ', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Dotacje na usługi rozwojowe dla dolnośląskich firm.', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Agencja Rozwoju Regionalnego ARLEG S.A.', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Dotacje na usługi rozwojowe dla dolnośląskich firm.', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'DOLNOŚLĄSKI PARK INNOWACJI I NAUKI SPÓŁKA AKCYJNA', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Dotacje na usługi rozwojowe dla dolnośląskich firm.', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Sudecki Instytut Rozwoju Regionalnego', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Nowe kompetencje', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'DOLNOŚLĄSKA AGENCJA ROZWOJU REGIONALNEGO SPÓŁKA AKCYJNA', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Dotacje na usługi rozwojowe dla dolnośląskich firm.', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'KARKONOSKA AGENCJA ROZWOJU REGIONALNEGO SPÓŁKA AKCYJNA', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Dotacje na usługi rozwojowe dla dolnośląskich firm.', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Wojewódzki Urząd Pracy w Toruniu', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Kierunek - Rozwój', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Toruńska Agencja Rozwoju Regionalnego Spółka Akcyjna', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Regionalny Fundusz Szkoleniowy II', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Lubelski Park Naukowo-Technologiczny Spółka Akcyjna', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Park Nowych Kwalifikacji dla MŚP', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'FUNDACJA ROZWOJU LUBELSZCZYZNY', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Finansowanie usług rozwojowych dla mikro-, małych, średnich i dużych przedsiębiorstw z podregionu', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'STOWARZYSZENIE ROZWOJU AKTYWNOŚCI SPOŁECZNEJ TRIADA', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Usługi rozwojowe dla pracodawców, przedsiębiorców i ich pracowników w podregionie', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'LUBELSKA FUNDACJA ROZWOJU', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Kompetencje przyszłości. Usługi rozwojowe dla osób dorosłych w podregionie lubelskim', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'FUNDACJA INTEGRON PLUS', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'NASTAW SIĘ NA ROZWÓJ - oferta wsparcia kwalifikacji i kompetencji mieszkańców podregionu', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'PUŁAWSKIE CENTRUM PRZEDSIĘBIORCZOŚCI', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Transformacje- dofinansowania i wsparcie dla osób dorosłych w ramach PSF -podregion puławski', _opr_id, -1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'UPSKILLING HR', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Łódzka Agencja Rozwoju Regionalnego S.A.', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'BON- Apetyt na rozwój 2', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'MAŁOPOLSKA AGENCJA ROZWOJU REGIONALNEGO S.A.', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Małopolskie Bony Rozwojowe - Nowa Perspektywa', _opr_id, -1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'AKADEMIA HR – dostosuj firmę do wymagań jutra', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Fundacja Rozwoju Regionu Rabka', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Tarnowskie Bony Rozwojowe', _opr_id, -1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Bon dla Podhalańskiego Przedsiębiorcy 2', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Wojewódzki Urząd Pracy w Krakowie', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Małopolski Pociąg do kariery sezon I', _opr_id, -1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Nowy start w Małopolsce z EURESEM', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Stowarzyszenie na Rzecz Szkoły Zarządzania i Handlu w Oświęcimiu', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'NetBon 2', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Miasto Nowy Sącz- Centrum Pozyskiwania Funduszy i Przedsiębiorczości', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Sądeckie Bony Szkoleniowe II', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'KDK Info spółka z ograniczoną odpowiedzialnością', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'AKADEMIA HR. Profesjonalne kadry HR kluczem do rozwoju przedsiębiorstw.', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Agencja Rozwoju Regionalnego MARR S.A.', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Akademia HR - czas na zmiany', _opr_id, -1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Nowoczesne kompetencje w subregionie tarnobrzeskim', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Regionalna Izba Gospodarcza w Katowicach', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Przepis na rozwój - Akademia HR', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'CERTES SPÓŁKA Z OGRANICZONĄ ODPOWIEDZIALNOŚCIĄ', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'NA WYŻSZYM POZIOMIE - KWALIFIKACJE DLA HR', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'PÓŁNOCNA IZBA GOSPODARCZA W SZCZECINIE', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Przepis na rozwój - Akademia HR', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'ŁÓDZKA IZBA PRZEMYSŁOWO- HANDLOWA', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Akademia HR - doskonalenie kompetencji kadry zarządzającej zasobami ludzkimi', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Instytut ADN spółka z ograniczoną odpowiedzialnością sp.k.', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Akademia HR. Szkolenia i doradztwo z zakresu zarządzania zasobami ludzkimi (...)', _opr_id, -1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Usługi rozwojowe dla pracowników i pracodawców podregionu leszczyńskiego', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'HRP GRANTS Sp. z o. o.', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Przepis na rozwój - Akademia HR', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Stowarzyszenie Centrum Rozwoju Ekonomicznego Pasłęka', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Menadżer HR', _opr_id, -1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Kariera przyszłości', _opr_id, -1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'FUR 3 - Fundusz Usług Rozwojowych', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Techpal Sp. z o.o.', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'AKADEMIA HR. Profesjonalne kadry HR kluczem do rozwoju przedsiębiorstw.', _opr_id, -1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Klucz do usług rozwojowych dla MŚP z województwa warmińsko- mazurskiego', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Opolskie Centrum Rozwoju Gospodarki w Opolu', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Kształcenie ustawiczne', _opr_id, -1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Adaptacyjność pracodawców i pracowników', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Caritas Diecezji Rzeszowskiej', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Nowe kwalifikacje i kompetencje drogą do kariery', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Rzeszowska Agencja Rozwoju Regionalnego S.A.', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Generator Kompetencji 3.0 - Rozwój kwalif. i kompetencji pracowników MŚP oraz innych podmiotów…', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Stawil Sp. z o.o.', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'SK – pr. wsparcia pod. pracodawców i pracowników w podnoszeniu kwalif. i kompetencji w ramach PSF', _opr_id, -1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'SJ – prog. wsp. osób dorosłych w zdobywaniu i uzupełnianiu kwalifikacji i kompetencji dla powiatów…', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Przemyska Agencja Rozwoju Regionalnego S.A.', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Fundusz Usług Rozwojowych II – wsp. przedsiębiorców i pracodawców oraz ich pracowników z sub. przem.', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Fundacja Rozwoju Społeczno- Gospodarczego INWENCJA', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Postaw na siebie!', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Horeca24 Spółka z ograniczoną odpowiedzialnoscia', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Operacja Edukacja!', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'SAGITUM Spółka Akcyjna', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Szkolenia Plus', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Centrum Szkoleniowo Konsultingowe dla Biznesu Jerzy Gałuszka', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Kuźnia Kwalifikacji Zawodowych', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'LOOTUS Joanna Jędrzejowska', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Akademia kwalifikacji zawodowych', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Fundacja Amico', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Rozkwit kwalifikacji zawodowych osób dorosłych', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Stowarzyszenie Pomocy Dzieciom i Młodzieży', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Z nami podniesiesz swoje kwalifikacje', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Wojewódzki Urząd Pracy w Białymstoku', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Podmiotowy System Finansowania - realizacja usług rozwojowych w województwie podlaskim', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Wojewódzki Urząd Pracy w Kielcach', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Pracownik kapitałem firmy. Postaw na jego rozwój z bazą usług rozwojowych', _opr_id, -1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'BUduj swój Rozwój – Baza', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Usług Rozwojowych Instytut Badawczo-Szkoleniowy Spółka z ograniczoną odpowiedzialnością', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Adaptacja - Usługi rozwojowe dla MŚP', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'PROCESSTEAM SPÓŁKA Z OGRANICZONĄ ODPOWIEDZIALNOŚCIĄ', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Kompetentny region - Warmia i Mazury', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'OŚRODEK DORADZTWA I TRENINGU KIEROWNICZEGO', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Kompetentny region - Warmia i Mazury', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Krajowa Agencja Informacyjna INFO Spółka z ograniczoną odpowiedzialnością', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'EDUKACJA dla przyszłości. Usługi rozwojowe dla osób dorosłych z woj. warmińsko-mazurskiego', _opr_id, -1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Usługi rozwojowe dla przyszłości Warmii i Mazur', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Warmińsko-Mazurski Związek Pracodawców Prywatnych', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'BUR w działaniu. Edukacja przez całe życie', _opr_id, -1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Adaptacja - Usługi rozwojowe dla MŚP', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Inspire Consulting sp. z o.o.', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Kompetentny region - Warmia i Mazury', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'ProcessTeam Sp. z o.o.', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Kompetentny region - Warmia i Mazury', _opr_id, -1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Program wsparcia mikro, małych i średnich firm w okresowych trudnościach', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Wielkopolska Agencja Rozwoju Przedsiębiorczości Sp. z o.o.', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Usługi rozwojowe dla Twojego biznesu', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Stowarzyszenie Na Rzecz Spółdzielni Socjalnych', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Usługi rozwojowe dla Twojego biznesu', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'KRAJOWA IZBA GOSPODARCZA', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Usługi rozwojowe dla pracowników i pracodawców podregionu leszczyńskiego', _opr_id, -1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'KOMPLEKSOWE WSPARCIE FIRM W OKRESOWYCH TRUDNOŚCIACH', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Fundusz Rozwoju i Promocji Województwa Wielkopolskiego S.A', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Usługi rozwojowe w subregionie pilskim - szansą na zmianę', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Agencja Rozwoju Regionalnego S.A. w Koninie', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Transformacja w kształceniu osób dorosłych z Wielkopolski Wschodniej.', _opr_id, -1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Transformacja kapitału ludzkiego w Wielkopolsce Wschodniej.', _opr_id, -1);
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Usługi rozwojowe - inwestycja w kapitał ludzki w podregionie konińskim.', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Fundacja Kaliski Inkubator Przedsiębiorczości', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Subregion kaliski inwestuje w kadry!', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'STOWARZYSZENIE OSTROWSKIE CENTRUM WSPIERANIA PRZEDSIĘBIORCZOŚCI', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Subregion kaliski inwestuje w kadry!', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Wielkopolska Izba Przemysłowo- Handlowa Izba Gospodarcza', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Usługi rozwojowe dla pracowników i pracodawców podregionu leszczyńskiego', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Koszalińska Agencja Rozwoju Regionalnego S.A.', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Usługi BUR kluczem do sukcesu przedsiębiorstwa', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Polska Fundacja Przedsiębiorczości', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Fundusz Usług Rozwojowych w województwie zachodniopomorskim - FUR3', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'Certes Sp. z o.o.', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'POSTAW SWÓJ BIZNES NA NOGI', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'QS Zurich Sp. z o.o.', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'STACJA: NOWE OTWARCIE', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'KDK INFO Sp. z o.o.', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Kompleksowy program wsparcia firm w okresowych trudnościach', _opr_id, -1);

    INSERT INTO operator(opr_uuid, opr_name, opr_created_by) VALUES (gen_random_uuid(), 'CTS Customized Training Solutions Sp. z o.o.', -1) RETURNING opr_id INTO _opr_id;
    INSERT INTO program(prg_uuid, prg_name, prg_operator_id, prg_created_by) VALUES(gen_random_uuid(), 'Centrum wsparcia MŚP w okresowych trudnościach', _opr_id, -1);


END $$;