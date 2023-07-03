#include <stdio.h>

#define TEST(x) foo## x
#define TEST2(x) foo##x

int main()
{
    int TEST(bar) = 1;
    TEST2(bar)++;
    foobar++;
    printf("test: %i\n", foobar);
    return 0;
}
