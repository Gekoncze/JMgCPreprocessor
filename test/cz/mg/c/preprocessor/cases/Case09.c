#include <stdio.h>

#define OPERATION(x, o, y) x o y
#define MINUS(x, y) OPERATION(x, -, y)
#define OPERATION(x, o, y) y o x

int main() {
    int r = MINUS(2, 7);
    printf("result: %i\n", r);
}
