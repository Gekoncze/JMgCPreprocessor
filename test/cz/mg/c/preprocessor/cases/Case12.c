#include <stdio.h>

#define QUOTE "
#define TEXT(x) QUOTE x QUOTE

int main(){
    const char* t = TEXT(foo);
    printf("%s\n", t);
}
