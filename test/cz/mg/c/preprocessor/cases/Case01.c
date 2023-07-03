#include <stdio.h>

#define INCREMENTAL(a) a##=

int main() {
    int v = 0;

    v INCREMENTAL(+) 3;
    printf("%i\n", v);

    v INCREMENTAL(-) 2;
    printf("%i\n", v);
}
