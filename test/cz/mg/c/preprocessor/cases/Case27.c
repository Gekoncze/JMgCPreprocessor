#include <stdio.h>

#define FOO

#if defined FOO
#define NUMBER() 11
#endif

int main () {
    printf ("%i\n", NUMBER ());
    return 0;
}