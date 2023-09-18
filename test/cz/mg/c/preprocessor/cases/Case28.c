#include <stdio.h>

#define FOO 1
#define FOO() 2

int main() {
    printf("%i %i\n", FOO, FOO());
    return 0;
}