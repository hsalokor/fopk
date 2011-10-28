# Funktionaalisempi ohjelmointi proseduraalisella kielellä

Funktionaalisessa ohjelmoinnissa rakennetaan ohjelmisto siten että se koostuu tilattomista funktioista joiden tulos riippuu pelkästään syötteestä. Sen pohjana on lambda-kalkyyli ja matematiikasta tuttu funktion käsite. Erona proseduraaliseen ohjelmointiin on se että funktionaalisessa ohjelmoinnissa arvioidaan lausekkeita käskyjen antamisen sijaan. 

## Funktionaalisen ohjelmoinnin edut

Proseduulisessa ohjelmoinnissa mikä tahansa käsky voi muokata ohjelman tilaa, tätä kutsutaan sivuvaikutukseksi (side-effect). Ohjelmiston ymmärtämisen kannalta rajoittamattomat sivuvaikutukset ovat haitallisia. Esimerkiksi säikeisessä (multi-threaded) ohjelmistossa käsiteltävä tieto voi muuttua kesken proseduurien suoritusten, mikä voi johtaa vaikeasti selvitettäviin ongelmiin. Koska funktionaalisessa ohjelmoinnissa funktion paluuarvo riippuu vain syötteestä ja tieto on oletusarvoisesti muuttumatonta tämän tyyppisiä ongelmia ei esiinny.

Soveltamalla muutamia funktionaalisen ohjelmoinnin käsitteitä voidaan proseduraalisten tai olio-pohjaisten ohjelmistojen rakennetta selkiyttää ja vähentää virheitä merkittävästi. Lisäksi ohjelmiston rinnakkaistaminen helpottuu, koska tietoa ei tarvitse lukita säikeiden välillä.

## Funktionaaliset ohjelmointitekniikat

Muutamia funktionaalisia ohjelmointitekniikoita voidaan hyödyntää myös proseduraalisissa ohjelmointikielissä. Seuraavissa kappaleissa esittelemme muutamia niistä, sekä esimerkkejä sekä Javalla että C++:lla.

Koska näitä funktionaalisten ohjelmointikielien ominaisuuksia ei ole suoraan rakennettu näihin kieliin, monet tekniikoista saattavat vaikuttaa oudoilta tai jopa tarkoituksettomilta, mutta niiden hyödyntäminen johtaa moniin samoihin etuihin joista funktionaalisen ohjelmointikielten ohjelmoijat nauttivat.

### Tilattomat funktiot (Stateless function)

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

~~~ {.cpp}
#include <boost/function.hpp>
#include <boost/bind.hpp>
#include <boost/foreach.hpp>

using namespace std;
using namespace boost;

bool exact(string& value, string expected)
{
    return value == expected;
}

const vector<string> filter(const vector<string>& values,
                            function<bool(string)> predicate)
{
    vector<string> output;
    BOOST_FOREACH (string value, values)
    {
        if (predicate(value))
            output.push_back(value);
    }
    return output;
}
~~~

Edelläolevassa esimerkissä syötettä ei suoraan muokata vaan metodissa palautetaan uusi lista predikaatin täyttämällä ehdolla.

### Muuttumaton data (Immutable data)

Yksi helpoimpia tapoja vähentää sivuvaikutuksien muodostumista on estää datan suora muokkaaminen. Olio-kielissä tämä edellyttää sellaista olioiden tekemistä, jotka eivät anna muokata omia muuttujiaan.

#### Javassa

Tyypillinen Java-bean-rakenne ohjaa väärään suuntaan ja sen sijaan kannattaakin suosia final-avainsanaa. Muuttumattomat oliot vaativat avukseen apuluokkia, jotta niiden muodostaminen onnistuu kivuttomasti. Usein käytetty tapa on  rakentaja-olio (Builder-pattern).

Esimerkki data ja rakentaja
~~~{.java}
package functional.java;

public class Address implements ContactInformation {
	private final String streetAddress;
	private final String postCode;
	private final String postOffice;

	public Address(String streetAddress, String postCode, String postOffice) {
		this.streetAddress = streetAddress;
		this.postCode = postCode;
		this.postOffice = postOffice;
	}

	@Override
	public String getStreetAddress() {
		return streetAddress;
	}

	@Override
	public String getPostCode() {
		return postCode;
	}

	@Override
	public String getPostOffice() {
		return postOffice;
	}
}

package functional.java;

public class AddressBuilder {
	private String buildStreetAddress;
	private String buildPostCode;
	private String buildPostOffice;

	public AddressBuilder withAddress(String streetAddress) {
		buildStreetAddress = streetAddress;
		return this;
	}

	public AddressBuilder withPostCode(String postCode) {
		buildPostCode = postCode;
		return this;
	}

	public AddressBuilder withPostOffice(String postOffice) {
		buildPostOffice = postOffice;
		return this;
	}

	public Address build() {
		return new Address(buildStreetAddress, buildPostCode, buildPostOffice);
	}
}
~~~

Toinen tapa rakentaa muuttumattomia olioita on edustaja (Proxy). Sen sijaan että oliolla on omia muuttujia, se toimii näkymänä toisten olioiden tietosisältöön.

Esimerkki edustaja
~~~{.java}
package functional.java;

import java.io.Serializable;

public interface ContactInformation extends Serializable {
	public static final ContactInformation NO_CONTACT_INFORMATION = new NoContactInformation();

	String getStreetAddress();

	String getPostCode();

	String getPostOffice();

	public static final class NoContactInformation implements ContactInformation {
		@Override
		public String getStreetAddress() {
			return "";
		}

		@Override
		public String getPostCode() {
			return "";
		}

		@Override
		public String getPostOffice() {
			return "";
		}
	}
}

package functional.java;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

public class DbAddress implements ContactInformation {
	@Inject
	private QueryRunner database;
	private final Integer id;

	public DbAddress(Integer id) {
		this.id = id;
	}

	@Override
	public String getStreetAddress() {
		return fetchDbContactInfo("streetAddress");
	}

	@Override
	public String getPostCode() {
		return fetchDbContactInfo("postCode");
	}

	@Override
	public String getPostOffice() {
		return fetchDbContactInfo("postOffice");
	}

	private String fetchDbContactInfo(String fieldName) {
		try {
			return database.query("select " + fieldName + " from contactinfo where id = ?", new FirstStringHandler(), id);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private static final class FirstStringHandler implements ResultSetHandler<String> {
		@Override
		public String handle(ResultSet rs) throws SQLException {
			if (rs.next()) {
				return rs.getString(0);
			}
			throw new SQLException("No result");
		}
	}
}

Huomaa, että ContactInformation-rajapinnalla on oma tyhjä vakio NO_CONTACT_INFORMATION, jota on hyvä käyttää sen sijaan että palauttaisi null-arvon. Tällöin null-tarkistuksien sijaan voidaan verrata suoraan NO_CONTACT_INFORMATION-vakioon.

Javassa ei ole sisäänrakennettua tapaa saada muuttumattomia tietorakenteita, kuten listoja (List) tai taulukkoa (Map). Tähän tarkoitukseen kannattaa käyttää esimerkiksi [Googlen guava-kirjastoa](http://code.google.com/p/guava-libraries/).

#### C++:ssa

C++:ssa ei ole muuttumattomia tietorakenteita, mutta const-avainsanan käytöllä voidaan estää esimerkiksi syötteenä välitettävän listan muokkaaminen.

Esimerkki C++:lla

### Koostaminen (Composition)

Koostamisessa funktion palautusarvot sopivat suoraan seuraavan funktion syötteeksi. Tällä tavalla funktiota voidaan helposti ketjuttaa toisiinsa, sekä niistä tulee lyhyitä ja helposti uudelleenkäytettäviä.

Javassa ja C++:ssa koostaminen tehdään funktio-olioilla, jotka alustetaan syötteellä ja tuottavat saman tuloksen. Funktio-olioita voidaan antaa syötteeksi toisille funktio-olioille jolloin saadaan aikaan ns. korkean asteen funktioita.

### Muunnokset (Transformation)

Muunnoksessa data muutetaan seuraavan funktion tarvitsemaan muotoon muuttamatta alkuperäistä dataa.

## Yhteenveto

Funktionaalisten ohjelmointitekniikoiden käyttö vaatii ohjelmoijalta kurinalaisuutta ja pieteettiä. Palkintona saadaan kuitenkin helpommin testattava ja paremmin muutoksia sietävä ohjelmisto.
