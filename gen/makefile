CFLAGS = -Wall -I../src/
LDFLAGS=-L ../src -lptask -lpthread -lrt -Wunused-function -Wunused-variable
CC=gcc
LD=gcc
.SUFFIXES:
.SUFFIXES: .o .c

EXECS =  main  

SRCS_main = main.c syntethicTask1.c syntethicTask2.c  hgr.c
OBJS_main = ${SRCS_main:.c=.o}

all: $(EXECS)

-include Makefile.deps

depend:
	$(CC) $(CFLAGS) -MM *.[ch] > Makefile.deps

.c.o: 
	$(CC) $(DEPENDS) $(CFLAGS) -c $<

main: $(OBJS_main) ../src/libptask.a
	$(LD) -o $@ $^ $(LDFLAGS) 

clean:
	rm -rf *.o
	rm -rf $(EXECS)

cleanedit:
	rm -rf *~






