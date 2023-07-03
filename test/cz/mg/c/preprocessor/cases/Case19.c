#include <stdio.h>

#define FOO 77
#define FOOBAR 88
#define CALL(x) x##BAR

int main(){
    printf("%i\n", CALL(FOO));
    return 0;
}
