#include <stdio.h>

#define PLUS(x,y) x + y

#define ONE

int main() {
    int r = PLUS(
#ifdef ONE
        1
#else
        2
#endif
        ,
        7
    );
    printf("Result: %i\n", r);
}
