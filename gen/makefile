CFLAGS = -Wall -I../src/
LDFLAGS=-L ../src -lptask -lpthread -lrt 
CC=gcc
LD=gcc
.SUFFIXES:
.SUFFIXES: .o .c

EXECS =  main  

SRCS_main = main.c syntethicTask1.c syntethicTask2.c syntethicTask3.c syntethicTask4.c syntethicTask5.c  _hgr.c
SRCS_special= _hgr_computation.c 

OBJS_main = ${SRCS_main:.c=.o}

SPECIAL_OBJS = ${SRCS_special:.c=.o}
$(SPECIAL_OBJS): EXTRA_FLAGS := -O0 

all: $(EXECS) 

-include Makefile.deps

depend:
	$(CC) $(CFLAGS) -MM *.[ch] > Makefile.deps

.c.o: 
	$(CC) $(DEPENDS) $(CFLAGS) $(EXTRA_FLAGS) -o $@ -c $<

main: $(OBJS_main) $(SPECIAL_OBJS) ../src/libptask.a
	$(LD) -o $@ $^ $(LDFLAGS) 

clean:
	rm -rf *.o
	rm -rf $(EXECS)

cleanedit:
	rm -rf *~











