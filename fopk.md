# Funktionaalisempi ohjelmointi proseduraalisella kielellä

Funktionaalisessa ohjelmoinnissa rakennetaan ohjelmisto siten että se koostuu tilattomista funktioista joiden tulos riippuu pelkästään syötteestä. Sen pohjana on lambda-kalkyyli ja matematiikasta tuttu funktion käsite. Erona proseduraaliseen ohjelmointiin on se että funktionaalisessa ohjelmoinnissa arvioidaan lausekkeita käskyjen antamisen sijaan. 

# Funktionaalisen ohjelmoinnin edut

Proseduulisessa ohjelmoinnissa mikä tahansa käsky voi muokata ohjelman tilaa, tätä kutsutaan sivuvaikutukseksi (side-effect). Ohjelmiston ymmärtämisen kannalta rajoittamattomat sivuvaikutukset ovat haitallisia. Esimerkiksi säikeisessä (multi-threaded) ohjelmistossa käsiteltävä tieto voi muuttua kesken proseduurien suoritusten, mikä voi johtaa vaikeasti selvitettäviin ongelmiin. Koska funktionaalisessa ohjelmoinnissa funktion paluuarvo riippuu vain syötteestä ja tieto on oletusarvoisesti muuttumatonta tämän tyyppisiä ongelmia ei esiinny.

Soveltamalla muutamia funktionaalisen ohjelmoinnin käsitteitä voidaan proseduraalisten tai olio-pohjaisten ohjelmistojen rakennetta selkiyttää ja vähentää virheitä merkittävästi. Lisäksi ohjelmiston rinnakkaistaminen helpottuu, koska tietoa ei tarvitse lukita säikeiden välillä.

# Funktionaaliset ohjelmointitekniikat

# Tilattomat funktiot (Stateless function)

Mikään proseduraalisessa ohjelmoinnissa ei estä kirjoittamasta metodeita siten että ne eivät muokkaa omaa syötettään tai ohjelman muuta tilaa.

Esimerkki 1. Java

~~~ {.java}
package functional.java;

import java.util.ArrayList;
import java.util.List;

public class Filter {
        public List<String> apply(List<String> values, Predicate<String> predicate) {
                ArrayList<String> output = new ArrayList<String>();
                for (String string : values) {
                        if (predicate.apply(string)) {
                                output.add(string);
                        }
                }
                return output;
        }

        public interface Predicate<T> {
                public boolean apply(T input);
        }
}
~~~

Esimerkki 2. C++

Edelläolevassa esimerkissä työtettä ei suoraan muokata vaan metodissa palautetaan uusi lista predikaatin täyttämällä ehdolla.

# Muuttumaton data (Immutable data)

Yksi helpoimpia tapoja vähentää sivuvaikutuksien muodostumista on estää datan suora muokkaaminen. Olio-kielissä tämä edellyttää sellaista olioiden tekemistä, jotka eivät anna muokata omia muuttujiaan.

Javassa

Tyypillinen Java-bean-rakenne ohjaa väärään suuntaan ja sen sijaan kannattaakin suosia final-avainsanaa. Muuttumattomat oliot vaativat avukseen apuluokkia, jotta niiden muodostaminen onnistuu kivuttomasti. Usein käytetty tapa on  rakentaja-olio (Builder-pattern).

Esimerkki data ja rakentaja

Toinen tapa rakentaa muuttumattomia olioita on edustaja (Proxy). Sen sijaan että oliolla on omia muuttujia, se toimii näkymänä toisten olioiden tietosisältöön.

Esimerkki edustaja

Javassa ei ole sisäänrakennettua tapaa saada muuttumattomia tietorakenteita, kuten listoja (List) tai taulukkoa (Map). Tähän tarkoitukseen kannattaa käyttää esimerkiksi Googlen guava-kirjastoa.

C++:ssa

C++:ssa ei ole muuttumattomia tietorakenteita, mutta const-avainsanan käytöllä voidaan estää esimerkiksi syötteenä välitettävän listan muokkaaminen.

Esimerkki C++:lla

# Koostaminen (Composition)

Koostamisessa funktion palautusarvot sopivat suoraan seuraavan funktion syötteeksi. Tällä tavalla funktiota voidaan helposti ketjuttaa toisiinsa, sekä niistä tulee lyhyitä ja helposti uudelleenkäytettäviä.

Javassa ja C++:ssa koostaminen tehdään funktio-olioilla, jotka alustetaan syötteellä ja tuottavat saman tuloksen. Funktio-olioita voidaan antaa syötteeksi toisille funktio-olioille jolloin saadaan aikaan ns. korkean asteen funktioita.

Muunnokset (Transformation)

Muunnoksessa data muutetaan seuraavan funktion tarvitsemaan muotoon muuttamatta alkuperäistä dataa.

# Yhteenveto

Funktionaalisten ohjelmointitekniikoiden käyttö vaatii ohjelmoijalta kurinalaisuutta ja pieteettiä. Palkintona saadaan kuitenkin helpommin testattava ja paremmin muutoksia sietävä ohjelmisto.
