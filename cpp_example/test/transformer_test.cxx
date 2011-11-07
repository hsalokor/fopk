#include "gtest/gtest.h"
#include <boost/shared_ptr.hpp>
#include <boost/algorithm/string.hpp>

using namespace std;
using namespace boost;

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

TEST(TransformerTest, testLines)
{
    const vector<string> l = lines("line 1\nline 2");
    EXPECT_EQ("line 1", l[0]);
    EXPECT_EQ("line 2", l[1]);
}

TEST(TransformerTest, testWords)
{
    const vector<string> w = words("alpha beta");
    EXPECT_EQ("alpha", w[0]);
    EXPECT_EQ("beta", w[1]);
}

TEST(TransformerTest, testValidAddress)
{
    shared_ptr<Address> address = toAddress("Mystreet 35 B 74\n00180 Helsinki");
    EXPECT_EQ("Mystreet 35 B 74", address->streetAddress());
    EXPECT_EQ("00180", address->postalCode());
    EXPECT_EQ("Helsinki", address->postOffice());
}

TEST(TransformerTest, testInvalidAddress)
{
    shared_ptr<Address> address = toAddress("\nINVALID\nADDRESS\nFORMAT");
    EXPECT_EQ(NO_ADDRESS, address);
    EXPECT_EQ("", address->streetAddress());
    EXPECT_EQ("", address->postalCode());
    EXPECT_EQ("", address->postOffice());
}
