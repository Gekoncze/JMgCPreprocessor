#include <stdio.h>

#define CONNECT(a,b) a##b

int main() {
    int CONNECT(foo,bar) = 0;
    printf("%i\n", foobar);
}
