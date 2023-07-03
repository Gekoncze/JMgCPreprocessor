#include <stdio.h>

#ifdef defined
#define FOO 1
#else
#define FOO -1
#endif

int main() {
    printf("%i\n", FOO);
}
