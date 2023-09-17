#include <stdio.h>

#define CONNECT(a, b) a ## b

int main () {
    printf ("%i\n", CONNECT(7,7));
    return 0;
}