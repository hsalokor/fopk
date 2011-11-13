#include <gtest/gtest.h>
#include <boost/shared_ptr.hpp>
#include <boost/function.hpp>
#include <boost/foreach.hpp>
#include <boost/bind.hpp>

using namespace std;
using namespace boost;

bool exact(const string& value, const string& expected)
{
    return value == expected;
}

bool contains(const string& value, const string& expected)
{
    return value.find(expected) != string::npos;
}

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

shared_ptr<vector<string> > filter(function<bool(string)> predicate,
                                   const shared_ptr<vector<string> > values)
{
    shared_ptr<vector<string> > results(new vector<string>());
    BOOST_FOREACH(string value, *values.get())
    {
        if (predicate(value))
            results->push_back(value);
    }
    return results;
}

TEST(FilterTest, testFilteringWithBind)
{
    shared_ptr<vector<string> > values(new vector<string>());
    values->push_back("foo");
    values->push_back("bar");

    shared_ptr<vector<string> > filtered = filter(bind(exact, _1, "foo"), values);

    EXPECT_EQ(1, filtered->size());
    EXPECT_EQ("foo", filtered->at(0));
}

TEST(FilterTest, testFilterComposition)
{
    shared_ptr<vector<string> > values(new vector<string>());
    values->push_back("aaa");
    values->push_back("axa");
    values->push_back("byb");

    shared_ptr<vector<string> > filtered =
        filter(bind(contains, _1, "a"),
            filter(bind(contains, _1, "x"), values));
    EXPECT_EQ(1, filtered->size());
    EXPECT_EQ("axa", filtered->at(0));
}

TEST(FilterTest, testFunctionObjectMatcher)
{
    StrContains matcher("aaa");
    EXPECT_EQ(true, matcher("aaa"));
    EXPECT_EQ(false, matcher("bbb"));
}

TEST(FilterTest, testFilterCompositionWithFunctionObject)
{
    shared_ptr<vector<string> > values(new vector<string>());
    values->push_back("aaa");
    values->push_back("axa");
    values->push_back("byb");

    shared_ptr<vector<string> > filtered =
        filter(StrContains("a"),
            filter(StrContains("x"), values));

    EXPECT_EQ(1, filtered->size());
    EXPECT_EQ("axa", filtered->at(0));
}

