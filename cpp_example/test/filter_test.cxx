#include "gtest/gtest.h"
#include <boost/function.hpp>
#include <boost/bind.hpp>
#include <boost/foreach.hpp>

using namespace std;
using namespace boost;

class StrContains
{
public:
    StrContains(const string& expected) : m_expected(expected) {}
    bool operator()(const string& value) const
    {
        return value.find(m_expected) != string::npos;
    }
private:
    const string m_expected;
};

vector<string> filter(const function<bool(string)> predicate,
                      const vector<string>& values)
{
    vector<string> output;
    BOOST_FOREACH (string value, values)
    {
        if (predicate(value))
            output.push_back(value);
    }
    return output;
}

bool exact(const string& value, const string& expected)
{
    return value == expected;
}

bool contains(const string& value, const string& expected)
{
    return value.find(expected) != string::npos;
}

TEST(FilterTest, testFilteringByPredicate)
{
    vector<string> values;
    values.push_back("foo");
    values.push_back("bar");

    vector<string> filtered = filter(bind(exact, _1, "foo"), values);

    EXPECT_EQ(1, filtered.size());
    EXPECT_EQ("foo", filtered[0]);
}

TEST(FilterTest, testFilterComposition)
{
    vector<string> values;
    values.push_back("aaa");
    values.push_back("axa");
    values.push_back("byb");

    vector<string> filtered = filter(bind(contains, _1, "a"),
                                filter(bind(contains, _1, "x"), values));
    EXPECT_EQ(1, filtered.size());
    EXPECT_EQ("axa", filtered[0]);
}

TEST(FilterTest, testFunctionObjectMatcher)
{
    StrContains matcher("aaa");
    EXPECT_EQ(true, matcher("aaa"));
    EXPECT_EQ(false, matcher("bbb"));
}

TEST(FilterTest, testFilterCompositionWithFunctionObject)
{
    vector<string> values;
    values.push_back("aaa");
    values.push_back("axa");
    values.push_back("byb");

    vector<string> filtered = filter(StrContains("a"),
                                filter(StrContains("x"), values));

    EXPECT_EQ(1, filtered.size());
    EXPECT_EQ("axa", filtered[0]);
}

