CC = aarch64-linux-gnu-gcc

CFLAGS =
LDFLAGS = -static

default: switchtest


switchtest: switchtest.o
            $(CC) $(LDFLAGS) -o switchtest switchtest.o

switchtest.o: switchtest.c
            $(CC) $(CFLAGS) -o switchtest.o -c switchtest.c

clean:
            rm switchtest.o switchtest
