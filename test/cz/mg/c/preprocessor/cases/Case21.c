#include <stdio.h>

#define ONE 1
#define PRINT() printf("%i\n", ONE)
#undef ONE

int main() {
    PRINT();
}
