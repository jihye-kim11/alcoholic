CC = aarch64-linux-gnu-gcc

CFLAGS  =
LDFLAGS = -static

default: pushtest

pushtest: pushtest.o
        $(CC) $(LDFLAGS) -o pushtest pushtest.o

pushtest.o: pushtest.c
        $(CC) $(CFLAGS) -o pushtest.o -c pushtest.c

clean:
        rm pushtest.o pushtest

