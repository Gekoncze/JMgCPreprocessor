#include <stdio.h>

#define FOO 1
#define BAR 2
#define FOOBAR 3

#define CONCATENATE FOO ## BAR

int main() {
    printf("%i\n", CONCATENATE);
    return 0;
}