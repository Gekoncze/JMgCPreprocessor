#include <stdio.h>

#define ONE \
    1

int main() {
    printf(
        "%s at line %i: %i\n",
        __FILE__, __LINE__, ONE
    );
}
