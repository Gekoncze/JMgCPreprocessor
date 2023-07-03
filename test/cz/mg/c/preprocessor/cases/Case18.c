#include <stdio.h>

void FOO()
{
    printf("Foo!\n");
}

#define FOO() BAR()
#define BAR() FOO()

int main()
{
    FOO();
    return 0;
}
