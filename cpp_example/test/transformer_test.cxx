#include "gtest/gtest.h"
#include <boost/function.hpp>
#include <boost/bind.hpp>
#include <boost/foreach.hpp>

using namespace std;
using namespace boost;

const vector<string> lines(const string& input)
{
    vector<string> lines;
    return lines;
}

TEST(TransformerTest, testLines)
{
    const vector<string> l = lines("line 1\nline 2");
    EXPECT_EQ("line1", l[0]);
    EXPECT_EQ("line2", l[1]);
}

TEST(TransformerTest, testWords)
{
}

TEST(FilterTest, testAddress)
{
}
