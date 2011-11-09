# Funktionaalisempi ohjelmointi imperatiivisellä kielellä

Funktionaalisessa ohjelmoinnissa rakennetaan ohjelmisto siten, että se koostuu tilattomista funktioista, joiden tulos riippuu pelkästään syötteestä. Sen pohjana on lambda-kalkyyli ja matematiikasta tuttu funktion käsite. Erona imperatiiviseen ohjelmointiin on se, että funktionaalisessa ohjelmoinnissa arvioidaan lausekkeita käskyjen antamisen sijaan. 

## Funktionaalisen ohjelmoinnin edut

Imperatiivisessä ohjelmoinnissa mikä tahansa käsky voi muokata ohjelman tilaa. Tätä kutsutaan sivuvaikutukseksi (side-effect). Ohjelmiston ymmärtämisen kannalta rajoittamattomat sivuvaikutukset ovat haitallisia. Esimerkiksi säikeisessä (multi-threaded) ohjelmistossa käsiteltävä tieto voi muuttua kesken proseduurien suoritusten, mikä voi johtaa vaikeasti selvitettäviin ongelmiin. Koska funktionaalisessa ohjelmoinnissa funktion paluuarvo riippuu vain syötteestä ja tieto on oletusarvoisesti muuttumatonta, tämän tyyppisiä ongelmia ei esiinny.

Soveltamalla muutamia funktionaalisen ohjelmoinnin käsitteitä voidaan imperatiivisellä kielellä kirjoitettujen ohjelmistojen rakennetta selkiyttää ja vähentää virheitä merkittävästi. Lisäksi ohjelmiston rinnakkaistaminen helpottuu, koska tietoa ei tarvitse lukita säikeiden välillä.

## Funktionaaliset ohjelmointitekniikat

Lähes kaikki esitellyt tekniikat tähtäävät ohjelman ylläpitämän tilan vähentämiseen. Mitä vähemmän ylläpidettyä tilaa on, sitä vähemmän on myös odottamattomia sivuvaikutuksia. Täydelliseen tilattomuuteen ei yleensä imperatiivisella ohjelmointikielellä kirjoitetussa ohjelmassa ole mahdollisuutta, eikä sen saavuttamiseksi kannata käyttää liikaa vaivaa.

Seuraavissa kappaleissa esittelemme funktionaalisten kielien käsitteitä ja esimerkkejä sekä Javalla että C++:lla. Esimerkit on kirjoitettu siten että ne kuvaavat esiteltyä tekniikkaa, eivätkä ne suoraan pohjaudu tosimaailman tilanteisiin. Ne on pyritty tekemään luettaviksi ja toimivat samalla dokumenttina esitellystä tekniikasta.

Koska näitä funktionaalisten ohjelmointikielien ominaisuuksia ei ole suoraan rakennettu näihin kieliin, monet tekniikoista saattavat vaikuttaa oudoilta tai jopa tarkoituksettomilta, mutta niiden hyödyntäminen johtaa moniin samoihin etuihin joista funktionaalisen ohjelmointikielten ohjelmoijat nauttivat.

### Tilattomat funktiot (Stateless function)

Proseduraalisessa ohjelmointikielissä metodit voidaan kirjoittaa siten, että ne eivät muokkaa omaa syötettään tai ohjelman tilaa. Funktiota käytettäessä on tärkeää välttää nolla-arvojen (null) palauttamista, sillä tällöin funktioketjun suorittaminen päättyy poikkeukseen.

#### Javalla

Edelläolevassa esimerkissä on suodin jossa syötettä ei suoraan muokata, vaan palautetaan uusi lista joka täyttää ehdon. Suotimen ehto on rajapinta, joka usein toteutetaan nimettömänä (anonymous) luokkana.

*Suodin*

```java
package functional.java.examples;

import java.util.ArrayList;
import java.util.List;

public class Filter {
	public List<String> apply(List<String> values, Condition<String> predicate) {
		ArrayList<String> output = new ArrayList<String>();
		for (String string : values) {
			if (predicate.apply(string)) {
				output.add(string);
			}
		}
		return output;
	}

	public interface Condition<T> {
		public boolean apply(T input);
	}
}
```

Seuraavaksi suotimen testi ja esimerkki suotimen käytöstä [Googlen guava-kirjastolla](http://code.google.com/p/guava-libraries/). Aiemmin esitelty Condition-rajapinta vastaa täysin guava-kirjaston monikäyttöistä Predicate-rajapintaa.

*Suotimen testi*

````java
package functional.java.examples;

import static com.google.common.collect.Iterables.filter;
import static java.util.Arrays.asList;
import static junit.framework.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

import com.google.common.base.Predicate;

import functional.java.examples.Filter.Condition;

public class FilterTest {
	private static final List<String> PETS = asList("cat", "dog", "bunny", "tiger");
	private static final List<String> BEASTS = asList("tiger", "lion", "rhino", "bear");

	@Test
	public void filterBeasts() {
		List<String> petsWithoutBeasts = new Filter().apply(PETS, new Condition<String>() {
			@Override
			public boolean apply(String input) {
				return isNoBeast(input);
			}
		});
		assertNoBeasts(petsWithoutBeasts);
	}
	
	@Test
	public void filterBeastsWithGuava() {
		Iterable<String> petsWithoutBeasts = filter(PETS, new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				return isNoBeast(input);
			}
		});
		assertNoBeasts(petsWithoutBeasts);
	}
	
	private void assertNoBeasts(Iterable<String> petsWithoutBeasts) {
		for (String pet : petsWithoutBeasts) {
			for (String beast : BEASTS) {
				assertFalse(pet.equals(beast));
			}
		}
	}
	
	private static boolean isNoBeast(String input) {
		for (String beast : BEASTS) {
			if(input.equals(beast)) {
				return false;
			}
		}
		return true;
	}
}
````

#### C++:lla

```cpp
#include <boost/function.hpp>
#include <boost/bind.hpp>
#include <boost/foreach.hpp>

using namespace std;
using namespace boost;

bool exact(const string& value, const string& expected)
{
    return value == expected;
}

vector<string> filter(const vector<string>& values,
                      const function<bool(string)> predicate)
{
    vector<string> output;
    BOOST_FOREACH (string value, values)
    {
        if (predicate(value))
            output.push_back(value);
    }
    return output;
}
```

### Muuttumaton data (Immutable data)

Yksi helpoimpia tapoja vähentää sivuvaikutuksien syntymistä on estää ohjelmakoodin muuttujien suora muokkaaminen. Imperatiivisissä kielissä tämä tarkoittaa sitä että muuttujat alustetaan arvoilla vain kerran, eikä niille anneta myöhemmin uusia arvoja.

#### Javalla

Tyypillinen Java-bean-rakenne ohjaa väärään suuntaan ja sen sijaan kannattaa suosia final-avainsanaa. Muuttumattomat oliot vaativat avukseen apuluokkia, jotta niiden muodostaminen onnistuu kivuttomasti. Usein käytetty tapa on rakentaja-olio (Builder-pattern).

*dataluokan rajapinta*

```java
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
```

Huomaa, että ContactInformation-rajapinnalla on oma tyhjä vakio NO_CONTACT_INFORMATION, jota voidaan käyttää sen sijaan että palauttaisi nolla-arvon. Tällöin nolla-arvon tarkistuksien sijaan voidaan verrata suoraan NO_CONTACT_INFORMATION-vakioon.

*muuttumaton dataluokka*

```java
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
```

Mikäli muuttumattiomien olioiden muodostimen (constructor) parametrejä on paljon, apuna voidaan käyttää rakentaja-olioa. Rakentaja-olio pitää rakentamiseen tarvittavat arvot tallessa ja palauttaa rakennettavaan olioon arvoja asetettaessa itsensä. Täten rakentajan metodit voidaan ketjuttaa toistensa perään. 

*rakentaja*

```java
package functional.java;

public class AddressBuilder {
	private String buildStreetAddress;
	private String buildPostCode;
	private String buildPostOffice;

	public AddressBuilder withStreetAddress(String streetAddress) {
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
```

Javassa ei ole sisäänrakennettua tapaa saada muuttumattomia tietorakenteita, kuten listoja (List) tai taulukkoja (Map). Tähän tarkoitukseen kannattaa käyttää esimerkiksi [Googlen guava-kirjastoa](http://code.google.com/p/guava-libraries/), josta löytyy mm. ImmutableList- ja ImmutableMap-luokat.

#### C++:lla

C++:ssa ei ole muuttumattomia tietorakenteita, mutta *const*-avainsanan käytöllä voidaan estää esimerkiksi syötteenä välitettävän listan muokkaaminen. Const-asiasanaa voidaan käyttää arvon, osoittimen tai metodin yhteydessä. Const-määre arvon yhteydessä estää arvon muokkaamisen, kun taas osoittimen const-asiasana rajoittaa vain ja ainoastaan osoittimen muokkaamista. Metodin yhteydessä const-asiasana estää sekä luokan jäsenten muokkaamisen, että sellaisten metodien kutsumisen joissa ei ole const-määrettä.

Nolla-arvojen määrittäminen on mahdollista myös C++:ssa, mutta olioarvoja palautettaessa on syytä olla tarkkana. Päätetään esimerkiksi periä Address-luokasta NoAdress-aliluokka ja määrittää kumpaankin isValid() metodi. Mikäli olio palautetaan arvona (value), palautettava objekti leikkautuu (slicing). Leikkautumisella tarkoitetaan sitä, että kaikki peritty toiminnallisuus katoaa, koska muistia on varattu vain kantaluokan koon verran. (TBD: selvennä)

```cpp
class Address
{
public:
    bool isValid() { return true; }
    ...
};

class NoAddress : public Address
{
public:
    bool isValid() { return false; }
    ...
};

// Palautettu olio ei leikkautumisen vuoksi ikinä palauta isValid()-kutsulle
// arvoa false!
Address toAddress(string input)
{
    ...
}
```

Edellä mainittu esimerkki toimii, mikäli paluuarvo on osoitin, viite tai esimerkiksi shared_ptr<Address>. Java-esimerkkiä vastaava jaettua tyhjää oliota käyttävä esimerkki on alla:

*Esimerkki C++:lla*

```cpp
class Address
{
public:
    Address(string streetAddress, string postalCode, string postOffice)
        : m_streetAddress(streetAddress),
          m_postalCode(postalCode),
          m_postOffice(postOffice) {}

    const string& streetAddress() const { return m_streetAddress; }
    const string& postalCode() const { return m_postalCode; }
    const string& postOffice() const { return m_postOffice; }

private:
    const string m_streetAddress;
    const string m_postalCode;
    const string m_postOffice;
};

static const shared_ptr<Address> NO_ADDRESS =
    shared_ptr<Address>(new Address("", "", ""));

const vector<string> lines(const string& input)
{
    vector<string> lines;
    split(lines, input, is_any_of("\n"));
    return lines;
}

const vector<string> words(const string& input)
{
    vector<string> words;
    split(words, input, is_any_of(" "));
    return words;
}

const shared_ptr<Address> toAddress(string input)
{
    vector<string> addrLines = lines(input);
    if (addrLines.size() != 2) return NO_ADDRESS;

    vector<string> codeAndOffice = words(addrLines[1]);
    if (codeAndOffice.size() != 2) return NO_ADDRESS;

    return shared_ptr<Address>(new Address(addrLines[0],
                                           codeAndOffice[0],
                                           codeAndOffice[1]));
}
```

### Koostaminen (Composition)

Koostamisessa funktion palautusarvot sopivat suoraan seuraavan funktion syötteeksi. Tällä tavalla funktioita voidaan helposti ketjuttaa toisiinsa, sekä niistä tulee lyhyitä ja helposti uudelleenkäytettäviä.

Javassa ja C++:ssa koostaminen tehdään funktio-olioilla, jotka alustetaan syötteellä ja tuottavat saman tuloksen. Funktio-olioita voidaan antaa syötteeksi toisille funktio-olioille jolloin saadaan aikaan korkeamman asteen funktioita.

#### Javalla

Funktion rajapinta on  yksinkertainen ja se löytyy mm. [guava-kirjastosta](http://code.google.com/p/guava-libraries/).

```java
package functional.java;

public interface Function<F, T> {
	public T apply(F input);
}
````

Alla olevassa esimerkissä on käytetty funktioita ja staattisia metodeita siten että niistä muodostuu oma kielensä. Funktioiden käyttö on siirretty staattisten metodien taakse, jotta vältyttäisiin "new"-sanan toistamiselta. Guava-kirjastossa on monia apuluokkia funktioiden käyttämiseen, kuten [Functions-luokka](http://google-collections.googlecode.com/svn/trunk/javadoc/index.html?com/google/common/base/Functions.html) jota alla oleva esimerkki käyttää.

Functions.compose-metodilla muodostettu koostefuktio arvioidaan vasta kun sen apply-metodia kutsutaan. Siten on mahdollista muodostaa pitkiä kutsuketjuja laskematta yhtäkään tulosta.

*muuntajaluokka*

```java
package functional.java.examples;

import static com.google.common.base.Functions.compose;
import static functional.java.examples.ContactInformation.NO_CONTACT_INFORMATION;
import static java.util.Arrays.asList;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Function;

public class AddressTransformer implements Function<String, ContactInformation> {
	@Override
	public ContactInformation apply(String input) {
		try {
			return toAddress(Lines.from(input));
		} catch (ArrayIndexOutOfBoundsException e) {
			return NO_CONTACT_INFORMATION;
		}
	}

	private Address toAddress(final List<String> addressLines) {
		AddressBuilder addressBuilder = new AddressBuilder().withStreetAddress(First.of(addressLines));
		addressBuilder.withPostCode(firstWord(Second.of(addressLines)));
		addressBuilder.withPostOffice(secondWord(Second.of(addressLines)));
		return addressBuilder.build();
	}
	
	public static String firstWord(String input) {
		return compose(new First(), new Words()).apply(input);
	}
	
	public static String secondWord(String input) {
		return compose(new Second(), new Words()).apply(input);
	}

	public static class Lines implements Function<String, List<String>> {
		@Override
		public List<String> apply(String input) {
			return asList(input.split("\n"));
		}
		
		public static List<String> from(String input) {
			return new Lines().apply(input);
		}
	}

	public static class Words implements Function<String, List<String>> {
		@Override
		public List<String> apply(String input) {
			return Arrays.asList(input.split(" "));
		}
	}

	public static class First implements Function<List<String>, String> {
		@Override
		public String apply(List<String> input) {
			return input.get(0);
		}
		
		public static String of(List<String> input) {
			return new First().apply(input);
		}
	}
	
	public static class Second implements Function<List<String>, String> {
		@Override
		public String apply(List<String> input) {
			return input.get(1);
		}
		
		public static String of(List<String> input) {
			return new Second().apply(input);
		}
	}
}
```

*muuntajaluokan testi*

```java
package functional.java.examples;

import static functional.java.examples.ContactInformation.NO_CONTACT_INFORMATION;
import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class AddressTransformerTest {
	private final static String ADDRESS = "Testitie 5\n00999 OLEMATON";
	private final static String MISSING_CITY = "Testitie 5\n00999OLEMATON";
	private final static String MISSING_SECOND_LINE = "FUBAR";

	@Test
	public void hasCorrectAddressFields() {
		final ContactInformation address = new AddressTransformer().apply(ADDRESS);
		assertEquals("Testitie 5", address.getStreetAddress());
		assertEquals("00999", address.getPostCode());
		assertEquals("OLEMATON", address.getPostOffice());
	}

	@Test
	public void hasNoContactInformationWithMissingCity() {
		final ContactInformation address = new AddressTransformer().apply(MISSING_CITY);
		assertEquals(NO_CONTACT_INFORMATION, address);
	}

	@Test
	public void hasNoContactInformationWithMissingSecondLine() {
		final ContactInformation address = new AddressTransformer().apply(MISSING_SECOND_LINE);
		assertEquals(NO_CONTACT_INFORMATION, address);
	}
}
```

### Tyyppimuunnokset (Type-transformation)

Tyyppimuunnoksessa data muutetaan seuraavan funktion tarvitsemaan muotoon muuttamatta alkuperäistä dataa.

#### Javalla

Yksi tapa tehdä tyyppimuunnoksia (ja muuttumatonta dataa) on edustaja (proxy). Sen sijaan että oliolla on omia muuttujia, se toimii näkymänä toisten olioiden tietosisältöön. Myös edustajia voidaan ketjuttaa toisiinsa siten että syntyy kutsuketju alkuperäiseen syötteeseen saakka.

*edustaja*

```java
package functional.java.examples;

import static functional.java.examples.AddressTransformer.*;

public class PostalAddress implements ContactInformation {
	private final String address;

	public PostalAddress(String address) {
		this.address = address;
	}

	@Override
	public String getStreetAddress() {
		return First.of(Lines.from(address));
	}

	@Override
	public String getPostCode() {
		return firstWord(Second.of(Lines.from(address)));
	}

	@Override
	public String getPostOffice() {
		return secondWord(Second.of(Lines.from(address)));
	}
}
```

*tyyppimuunoksen testi*

```java
package functional.java.examples;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class PostalAddressTest {
	private final static String ADDRESS = "Testitie 5\n00999 OLEMATON";
	private final static ContactInformation postalAddress = new PostalAddress(ADDRESS);
	
	@Test
	public void hasCorrectStreetAddress() {
		assertEquals("Testitie 5", postalAddress.getStreetAddress());
	}

	@Test
	public void hasCorrectPostCode() {
		assertEquals("00999", postalAddress.getPostCode());
	}
	
	@Test
	public void hasCorrectPostOffice() {
		assertEquals("OLEMATON", postalAddress.getPostOffice());
	}
}
```

## Yhteenveto

Monet funktionaaliset tekniikat käytettynä imperatiivisissä kielissä tuottavat lisää laskentaa paikkoihin jossa perinteisesti on ylläpidetty tilaa. Tästä ei kannata kuitenkaan huolestua, sillä mikäli suorituskykyongelmia löytyy ne voidaan ratkaista esimerkiksi oikein sijoitetun välimuistin avulla tai säikeistämällä. Mitä muuta välimuisti onkaan kuin funktio joka muistaa syötteellä saadun arvon!

Funktionaalisten ohjelmointitekniikoiden käyttö vaatii ohjelmoijalta kurinalaisuutta. Erityisesti ulkoisia kirjastoja käytettäessä voi olla hankalaa ohjelmoida funktionaalisella tavalla. Halu lipsua muuttumattoman datan käytöstä tai muunnoksien tekemisestä saattaa kasvaa aikataulupaineiden kerääntyessä. Funktionaalisella tiellä pysyminen kuitenkin kannattaa, sillä funktionaalisesti kirjoitettu koodi on helposti luettavampaa ja muokattavampaa kuin imperatiiviseen tapaan kirjoitettu. Tämän lisäksi se on helpompaa testata myös suurempina yksikköinä kuin luokkatasolla.

Me allekirjoittaneet toivomme kaikille lukijoille mukavia funktionaalisia koodaushetkiä!

*Tuomas Hakkarainen ja Harri Salokorpi*
