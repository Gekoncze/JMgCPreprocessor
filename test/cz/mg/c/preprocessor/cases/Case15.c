#include <stdio.h>

#if !(-7)
#define RESULT 1
#else
#define RESULT -1
#endif

int main(){
    printf("%i\n", RESULT);
}
