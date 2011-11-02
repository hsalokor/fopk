#include "gtest/gtest.h"
#include <boost/function.hpp>
#include <boost/bind.hpp>
#include <boost/foreach.hpp>

using namespace std;
using namespace boost;

const vector<string> lines(const string& input)
{

}

TEST(TransformerTest, testLines)
{
    const vector<string> lines = lines("line 1\nline 2");
    EXPECT_EQ("line1", lines[0]);
    EXPECT_EQ("line2", lines[1]);
}

TEST(TransformerTest, testWords)
{
}

TEST(FilterTest, testAddress)
{
}
