# projekt1_po

https://github.com/apohllo/obiektowe-lab/tree/master/proj1

class Animal

    energia (zjedzenie rosliny -> +energia) - startowo defaultowo chyba że dziecko to obliczana

    genon o długości N
    
    ilosc dzieci (przy wywołaniu  multiplitacion zwiększa ilość dzieci)
    
    wiek - ilosc dni (przy wywołaniu move wzrasta ilość dni --> age) 

    isAlive ??
    
    isHealthy - stała do robienia dzieciaka (przy 
    rozmnażaniu sprawdza czy HP jest wystarczające)

    funkcja move(gen) - jeden ruch(gen z genomu) na dzien
        pelna predestynacja - geny pokolei
        nieco szalenstwa - 80% geny pokolei, 20% losowy inny gen


directions

    zakres ruchów
    
    0 - bez zmian
    
    1 - 45stopni
    
    2 - 90stopni
    
    ... 7



class Plants


rectangular map (wysokosc, szerokosc, startowa liczba roslin, startowa liczba zwierzakow,)

ogarnac dwa tryby bounduary

    kula ziemska
    
    piekielny portal
    
    jungla i stepy - uwzglendienia kategorii pol 80/20 %:

        zalesione rowniki
    
        toksyczne trupy

    place animal
    
    dead animal
    
    add plants(n - liczba roslin wyrastajaca kazdego dnia) - zajmuja maks 20% mapy

    jedzenie roslin przez zwierze
    
    rozmazanie - czesc energii przechodzi na dziecko procentowo od kazdego rodzica(x 75% , y 25% sumarycznej energii
        tyle procentowo ze swoich genomow rodzice oddaja dziecku) + wylosowanie strony pobierania genomu od silniejszego
        rodzica, slabszy - przeciwna, tak samo procentowo
        + energia procentowo odbierana od rodzicow ustalona w parametrach
        + mutacje - losowanie n i n genów do zmutowania w zakresie z parametrow
            lekka korekta - o 1 w dol lub w gore
            pelna losowosc - na dowolny iny


parametry:

    wysokość i szerokość mapy,
    
    wariant mapy (wyjaśnione w sekcji poniżej),
    
    startowa liczba roślin,
    
    energia zapewniana przez zjedzenie jednej rośliny,
    
    liczba roślin wyrastająca każdego dnia,
    
    wariant wzrostu roślin (wyjaśnione w sekcji poniżej),
    
    startowa liczba zwierzaków,
    
    startowa energia zwierzaków,
    
    energia konieczna, by uznać zwierzaka za najedzonego (i gotowego do rozmnażania),
    
    energia rodziców zużywana by stworzyć potomka,
    
    minimalna i maksymalna liczba mutacji u potomków (może być równa 0),
    
    wariant mutacji (wyjaśnione w sekcji poniżej),
    
    długość genomu zwierzaków,
    
    wariant zachowania zwierzaków (wyjaśnione w sekcji poniżej)


do przemyslenia:
    nieskonczona petla w world dopoki zwierzeta zyją
        funkcja dzien() ktora robi wszystko co sie w danym dniu dzieje

    animals trzymane w slowniku ale chyba <Vector2d, Animal[]>
        dodawanie(usuwanie z) do listy zwierzat na danej pozycji
        szukanie zwierzat na danej pozycji zwraca liste tych zwierzat

Ania Ś:

    animal - dodać warianty
    
    grass ok
    
    movedirections ok
    
    optionparser ok
    
    mapdirection ok

    simulationengine
    
    simulationenginegui

Ania G:
    
    vector2d - ok
    
    abstractworldmap
    
    grassfield -
        zalesione rowniki - ok
        toksyczne trupy - ??

    
    mapboundary
    
    mapvisualizer
    
    app
    
    guielementbox
    
    resources
