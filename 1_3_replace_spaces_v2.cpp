// clang++ -std=c++11 -stdlib=libc++ 1_4_replace_spaces_v2.cpp

/*
    Write a method to replace all spaces in a string with '%20'. 
    You may assume that the string has sufficient space at the 
    end of the string to hold the additional characters, 
    and that you are given the "true" length of the string. 
    (Note: if implementing in Java, please use a character array 
    so that you can perform this operation in place.)    
*/


/*
    The solution in the book is more simple because isntead of shifting
    to the right it calculates the new string length and then add chars
    backward starting from the new end of the string, so it doesn't have
    to bother with calculating and updating current shift amount. 
*/

/*
    Things to remember:
        - Always initialize stack variables
        - strlen() doesn't count '\0'
        - Even if algorithm to be tested doesn't allocate any data, it is 
          a good idea to free all the generated test data before exiting.
          This way testing code can be reused in batch testing mode that
          also happens to report any memory leaks.
*/


/*
    NOTE: This version does use "true" length of the input string 
*/

#include <stdio.h>
#include <string.h>

#include <cstdlib>
#include <string>
#include <tuple>
#include <ctime>

using std::string;
using std::tuple;
using std::tie;
using std::srand;
using std::rand;
using std::time;

char* replace_spaces(char* s, size_t len, const char* sub) {
    if (len == 0) {
        return s;
    }

    if (s == NULL || *s == '\0') {
        return s;
    }

    if (sub == NULL || *sub == '\0') {
        return s;
    }

    size_t subLen = strlen(sub);
    if (subLen == 0) {
        return s;
    }

    // Find the number of spaces to replace and the rightmost non space char (rnsc)
    int totalSpaces = 0;
    for (size_t i = 0; i < len; ++i) {
        if (s[i] == ' ') {
            ++totalSpaces;
        }
    }

    if (totalSpaces == 0) {
        return s;
    }

    // we reuse space occupied by each space char
    size_t shiftAmount = totalSpaces * subLen - totalSpaces; 

    // Here, we trust that there is enough free space in the string  to perform subsitiuiton

    // Starting from the rnsc, got to the end of the string, 
    // shifting current char to the right into the final position
    for (char* cur = s + len - 1; cur >= s; --cur) {
        if (*cur != ' ') {
            cur[shiftAmount] = *cur;
        } else {
            // if char is space, replace it with the substitute string and recalculate amount of shift
            for (const char* curSub = sub + subLen - 1; curSub >= sub; curSub--, shiftAmount--) {
                cur[shiftAmount] = *curSub;
            }
 
            // we reuse space occupied by each space char
            shiftAmount++;
        }
    }

    return s;
}

// Use a bit of C++ to make it simplier to generate testing data
tuple<const string, size_t, const string> generate_test_string(const string& sub) {
    const size_t max = rand() % 4096 + 1;  
    string s1, s2;
    size_t padLength = 0;

    for (size_t i = 0; i < max; ++i) {
        bool isLetter = rand() > RAND_MAX / 8;
        if (isLetter) {
            s1.push_back('a');
            s2.push_back('a');
        } else {
            s1.append(" a");
            s2.append(sub + "a");
            padLength += sub.length() - 1;
        }
    }

    const size_t len = s1.length(); 
    s1.append(padLength, ' ');

    return make_tuple(s1, len, s2);
}

bool compare_test(const string& s, size_t len, const string& sub, const string& expected) {
    char* s1 = strdup(s.c_str());    
    if (strcmp(replace_spaces(s1, len, sub.c_str()), expected.c_str()) != 0) {
        fprintf(stderr, "FAIL.\nInput: '%s'\nLen: %lU\nSub: '%s'\nGot: '%s'\nExpected: '%s'\n", 
            s.c_str(), len, sub.c_str(), s1, expected.c_str());
        return false;
    }

    free(s1);

    return true;
}

bool random_test(int totalIters) {
    srand(time(0));

    string s, expected;
    int len;
    for (int i = 0; i < totalIters; ++i) {
        const size_t subLength = rand() % 3 + 1;
        const string sub = string(subLength, '_');

        tie(s, len, expected) = generate_test_string(sub);
        if (!compare_test(s, len, sub, expected)) {
            return false;
        }
    }

    return true;
}

int main(int argc, char** argv) {
    if (!compare_test("", 0, "", "")) {
        return 1;
    }

    if (!compare_test("a", 1, "_", "a")) {
        return 1;
    }

    if (!compare_test(" a ", 2, "__", "__a")) {
        return 1;
    }

    if (!compare_test("  a  ", 3, "__", "____a")) {
        return 1;
    }

    if (!compare_test("b  a  ", 4, "__", "b____a")) {
        return 1;
    }

    if (!random_test(10000)) {
        return 1;
    }

    return 0;
}
