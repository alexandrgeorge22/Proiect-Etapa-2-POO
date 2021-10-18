# Energy-Management-System Phase II

# Description
This project represents a more complex, second part of the previous project that implements a simulation of an energy management system.

# Implementation RO:

## Entitati

- Clasa Distributor: 
	- retine informatii utile pentru un distribuitor
	- are urmatoarele metode: setContractPrice ce calculeaza pretul
contractului fiecarui distribuitor sau il seteaza la (INF/999999999) in cazul 
in care distribuitorul a dat faliment
				  setProductionCost ce calculeaza costul de 
productie si il seteaza
				  payCosts ce calculeaza costul total de platit
la finalul lunii. Daca distribuitorul devine bankrupt acesta o sa fie scos
din listele producatorilor si din lista consumatorului abonat la acesta


- Clasa Producer:
	- retine informatii utile pentru un distribuitor
	- cand i se face update la energia distribuita anunta toti distribuitorii
	- la final de luna retine in "baza lui de date" informatii despre 
distribuitorii catre care a furnizat energie


- Clasa Consumer:
	- retine informatii utile pentru un consumator
	- are urmatoarele metode: checkContract ce verifica daca acesta are in
prezent un contract activ. In cazul in care acesta devine liber de contract,
isi plateste datoriile daca are si cauta un distribuitor nou
				  payContract ce plateste contractul actual. In
cazul in care acesta nu poate sa isi plateasca contractul, fie o sa aiba 
restanta, fie o sa devine bankrupt


- Clasa Input:
	- citeste informatiile de la fisierul de intrare


- Clasa FileWriter:
	- scrie rezultatul simularii in fisierul de iesire


- Clasele GreenStrategy, PriceStrategy, QuantityStrategy implementeaza toate
interfata Strategy. In aceste clase se sorteaza producatorii in functie de
tipul strategiei fiecarui distribuitor si apoi distribuitorul pentru care
se aplica strategia isi alege producatorii.


- Clasa Main are 2 metode: 
	- metoda main in care se citesc datele de intrare, se simuleaza fiecare 
runda si la final se scrie rezultatul
	- metoda initialround ce simuleaza runda initiala(am preferat folosirea 
acestei metode pentru ca initial pretul contractelor se genereaza pentru
fiecare distribuitor la fel urmand ca mai apoi sa se calculeze in functie de
numarul de clienti al fiecarui distribuitor).


## Flow

La inceput se citesc datele de intrare si se "joaca" runda intiala unde
pentru fiecare distribuitor se aplica strategia si isi alege producatorii,
calculeaza costul de productie si costul contractului. Consumatorii isi
primesc salariul, aleg distribuitorul si isi platesc rata iar la final 
distribuitorii isi platesc cheltuielile. 

Pentru rundele simularii, initial se citesc update-urile pentru distribuitori,
acestia isi calculeaza noul pret al contractelor, consumatorii isi primesc
salariile si verifica daca trebuie sa isi caute un nou distribuitor sau sa
plateasca rata actuala. Distribuitorii isi platesc cheltuielile. Se citesc
update-urile si pentru producatori, iar de fiecare data cand se face un
update la un producator acesta anunta toti distribuitorii iar daca acestia 
sunt afectati de aceasta schimbare isi reaplica strategia.


## Design patterns

In implementare am introdus si pattern-urile Factory si Singleton, am creat
cate un factory pentru consumatori, distribuitori, producatori si strategii
iar aceste factory-uri le-am facut de tipul Singleton pentru ca m-am gandit
ca o singura instanta din fiecare tip de factory este suficienta pentru a 
genera unul sau mai multe tipuri de consumatori/distribuitori/producatori/
strategii.

De asemenea, am introdus si pattern-ul Strategy pentru aplicarea strategiilor
distribuitorilor, fiecare tip de strategie implementand aceeasi interfata
comuna Strategy

Pattern-ul Observer este prezent si el, producatorii fiind de tipul 
Observabil iar distribuitorii de tipul Observer. Astfel, de fiecare data cand
un producator isi schimba cantitatea de energie oferita catre distribuitori,
acestia sunt anuntati si verifica daca acest lucru ii afecteaza. Daca ii 
afecteaza, distribuitorii o sa isi reaplice strategia.
